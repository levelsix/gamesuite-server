package com.lvl6.pictures.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.services.user.LoginService;
import com.lvl6.pictures.eventprotos.SaveRoundProgressEventProto.SaveRoundProgressRequestProto;
import com.lvl6.pictures.eventprotos.SaveRoundProgressEventProto.SaveRoundProgressResponseProto;
import com.lvl6.pictures.eventprotos.SaveRoundProgressEventProto.SaveRoundProgressResponseProto.Builder;
import com.lvl6.pictures.eventprotos.SaveRoundProgressEventProto.SaveRoundProgressResponseProto.SaveRoundProgressStatus;
import com.lvl6.pictures.events.request.SaveRoundProgressRequestEvent;
import com.lvl6.pictures.events.response.SaveRoundProgressResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.UnfinishedRoundProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.RoundPendingCompletion;
import com.lvl6.pictures.services.gamehistory.GameHistoryService;
import com.lvl6.pictures.services.roundpendingcompletion.RoundPendingCompletionService;


@Component
public class SaveRoundProgressController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Autowired
  protected GameHistoryService gameHistoryService;

  @Autowired
  protected LoginService loginService;
  
  @Autowired
  protected RoundPendingCompletionService rpcService;
  

  
  @Override
  public RequestEvent createRequestEvent() {
    return new SaveRoundProgressRequestEvent();
  }

  @Override
  public int getEventType() {
    return PicturesEventProtocolRequest.C_SAVE_ROUND_PROGRESS_EVENT_VALUE;
  }
  
  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    //stuff client sent
    SaveRoundProgressRequestProto reqProto = 
        ((SaveRoundProgressRequestEvent) event).getSaveRoundProgressRequestProto();
    BasicUserProto sender = reqProto.getSender();
    String userId = sender.getUserId();
    String gameId = reqProto.getGameId();
    UnfinishedRoundProto roundToSave = reqProto.getRound();
    
    
    //response to send back to client
    Builder responseBuilder = SaveRoundProgressResponseProto.newBuilder();
    responseBuilder.setStatus(SaveRoundProgressStatus.FAIL_OTHER);
    responseBuilder.setRecipient(sender);
    responseBuilder.setGameId(gameId);
    SaveRoundProgressResponseEvent resEvent = new SaveRoundProgressResponseEvent(userId);
    resEvent.setTag(event.getTag());
    
    try {
      //read from db
      GameHistory gh = null;
      if (null != gameId && !gameId.isEmpty()) {
        gh = getGameHistoryService().getGameHistoryById(gameId);
      }
      
      //validate request
      boolean validRequest = isValidRequest(responseBuilder, sender, userId,
          gameId, gh, roundToSave);

      boolean successful = false;
      if (validRequest) {
        successful = writeChangesToDb(userId, gh, roundToSave);
      }

      if (successful) {
        //not using gameId because game might not have existed before now
        responseBuilder.setGameId(gh.getId());
        responseBuilder.setStatus(SaveRoundProgressStatus.SUCCESS);
      }

      //write to client
      resEvent.setSaveRoundProgressResponseProto(responseBuilder.build());

      log.info("Writing event: " + resEvent);
      getEventWriter().handleEvent(resEvent);
      
    } catch (Exception e) {
      log.error("exception in SaveRoundProgressController processRequestEvent", e);
      
      try {
        //try to tell client that something failed
        responseBuilder.setStatus(SaveRoundProgressStatus.FAIL_OTHER);
        resEvent.setSaveRoundProgressResponseProto(responseBuilder.build());
        getEventWriter().handleEvent(resEvent);
        
      } catch (Exception e2) {
        log.error("exception in SaveRoundProgressController processRequestEvent", e2);
      }
    }
  }

  private boolean isValidRequest(Builder responseBuilder, BasicUserProto sender,
      String userId, String gameId, GameHistory gh, UnfinishedRoundProto urp) {
    
    //see that game exists, the user ids match up
    if (null == gh) {
      log.error("unexpected error: no game exists.");
      responseBuilder.setStatus(SaveRoundProgressStatus.FAIL_OTHER);
      return false;
    }
    
    String playerOneId = gh.getPlayerOneId();
    String playerTwoId = gh.getPlayerTwoId();
    if (!playerOneId.equals(userId) && !playerTwoId.equals(userId)) {
      log.error("unexpected error: user trying to save round is not a participant" +
          " in this game. userId=" + userId + "\t gameHistory=" + gh);
      responseBuilder.setStatus(SaveRoundProgressStatus.FAIL_OTHER);
      return false;
    }
    
    if (null == urp || urp.getRoundNumber() <= 0 || urp.getSecondsRemaning() <= 0 ||
        urp.getCurrentQuestionNumber() <= 0 || urp.getCurrentScore() < 0 ||
        null == urp.getRoundId()) {
      log.error("client error: client did not set a property value." +
      		" unfinishedRoundProto=" + urp);
      responseBuilder.setStatus(SaveRoundProgressStatus.FAIL_OTHER);
      return false;
    }
    
    RoundPendingCompletion rpc = gh.getUnfinishedRound();
    String roundId = urp.getRoundId();
    if (null == rpc || !(rpc.getId().equals(roundId)) ) {
      log.error("unexpected error: no unfinished-round exists or id client sent " +
      		"is wrong. unfinished-round=" + rpc + "\t unfinishedRoundProto" + urp);
    }
    
    return true;
  }
  
//  private RoundPendingCompletion createUnfinishedRound(String userId, 
//      int roundNumber, List<QuestionProto> questionProtos) {
//    List<String> questionIds = getQuestionIds(questionProtos);
//    RoundPendingCompletion rpc = 
//        getRpcService().createUnfinishedRound(userId, roundNumber, questionIds);
//    return rpc;
//  }
  
//  private List<String> getQuestionIds(List<QuestionProto> questions) {
//    List<String> questionIds = new ArrayList<String>();
//    
//    for (QuestionProto qp : questions) {
//      String qpId = qp.getId();
//      questionIds.add(qpId);
//    }
//    
//    return questionIds;
//  }

  private boolean writeChangesToDb(String userId, GameHistory gh,
      UnfinishedRoundProto urp) {
    try {
      RoundPendingCompletion rpc = gh.getUnfinishedRound();
      
      //update the unfinished round
      int secondsRemaining = urp.getSecondsRemaning();
      int currentQuestionNumber = urp.getCurrentQuestionNumber();
      int currentScore = urp.getCurrentQuestionNumber();
      getRpcService().updateRoundPendingCompletion(rpc, secondsRemaining,
	      currentQuestionNumber, currentScore);
        
      
      //save the game history
      getGameHistoryService().saveGameHistory(gh);
      
      return true;
    } catch (Exception e) {
      log.error("unexpected error: problem with saving to db.", e);
    }
    return false;
  }
  
  
  public GameHistoryService getGameHistoryService() {
    return gameHistoryService;
  }
  
  public void setGameHistoryService(GameHistoryService gameHistoryService) {
    this.gameHistoryService = gameHistoryService;
  }
  
  public LoginService getLoginService() {
    return loginService;
  }

  public void setLoginService(LoginService loginService) {
    this.loginService = loginService;
  }
  
  public RoundPendingCompletionService getRpcService() {
    return rpcService;
  }

  public void setRpcService(RoundPendingCompletionService rpcService) {
    this.rpcService = rpcService;
  }

}