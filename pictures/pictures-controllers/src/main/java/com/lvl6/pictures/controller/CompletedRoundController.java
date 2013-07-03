package com.lvl6.pictures.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.eventprotos.CompletedRoundEventProto.CompletedRoundRequestProto;
import com.lvl6.pictures.eventprotos.CompletedRoundEventProto.CompletedRoundResponseProto;
import com.lvl6.pictures.eventprotos.CompletedRoundEventProto.CompletedRoundResponseProto.Builder;
import com.lvl6.pictures.eventprotos.CompletedRoundEventProto.CompletedRoundResponseProto.CompletedRoundStatus;
import com.lvl6.pictures.events.request.CompletedRoundRequestEvent;
import com.lvl6.pictures.events.response.CompletedRoundResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionAnsweredProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.BasicRoundResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.CompleteRoundResultsProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.QuestionAnswered;
import com.lvl6.pictures.po.RoundHistory;
import com.lvl6.pictures.po.RoundPendingCompletion;
import com.lvl6.pictures.properties.PicturesPoConstants;
import com.lvl6.pictures.services.gamehistory.GameHistoryService;
import com.lvl6.pictures.services.questionanswered.QuestionAnsweredService;
import com.lvl6.pictures.services.roundhistory.RoundHistoryService;

@Component
public class CompletedRoundController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Autowired
  protected GameHistoryService gameHistoryService;

  @Autowired
  protected QuestionAnsweredService questionAnsweredService;
  
  @Autowired
  protected RoundHistoryService roundHistoryService;

  @Autowired
  protected CreateNoneventProtoUtils createNoneventProtoUtils;
  
  @Override
  public RequestEvent createRequestEvent() {
    return new CompletedRoundRequestEvent();
  }

  @Override
  public int getEventType() {
    return PicturesEventProtocolRequest.C_COMPLETED_ROUND_EVENT_VALUE;
  }
  
  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    //stuff client sent
    CompletedRoundRequestProto reqProto = 
        ((CompletedRoundRequestEvent) event).getCompletedRoundRequestProto();
    BasicUserProto sender = reqProto.getSender();
    BasicUserProto opponent = reqProto.getOpponent();
    String userId = sender.getUserId();
    String opponentId = opponent.getUserId();
    String gameId = reqProto.getGameId();
    CompleteRoundResultsProto crrp = reqProto.getResults();
    
    //response to send back to client
    Builder responseBuilder = CompletedRoundResponseProto.newBuilder();
    responseBuilder.setStatus(CompletedRoundStatus.FAIL_OTHER);
    responseBuilder.setSender(sender);
    responseBuilder.setOpponent(opponent);
    CompletedRoundResponseEvent resEvent = new CompletedRoundResponseEvent(userId);
    resEvent.setTag(event.getTag());
    
    
    try {
      //read from db, highly doubt gameId is null
      GameHistory gh = null;
      if (null != gameId && !gameId.isEmpty()) {
        gh = getGameHistoryService().getGameHistoryById(gameId);
      }
      
      //validate request
      boolean validRequest = isValidRequest(responseBuilder, sender, userId,
          gameId, gh, crrp);

      List<RoundHistory> rhList = new ArrayList<RoundHistory>();
      boolean successful = false;
      if (validRequest) {
	  //gameHistory object is updated
        successful = writeChangesToDb(userId, gh, crrp, rhList);
      }

      if (successful) {
        responseBuilder.setGameId(gameId);
        responseBuilder.setStatus(CompletedRoundStatus.SUCCESS);
        //set the results
        setResults(responseBuilder, rhList);
      }

      //write to client
      CompletedRoundResponseProto responseProto = responseBuilder.build();
      resEvent.setCompletedRoundResponseProto(responseProto);

      log.info("Writing event: " + resEvent);
      getEventWriter().handleEvent(resEvent);
      
      //notify opponent what happened
      if (successful) {
        //TODO: FIGURE OUT HOW TO SEND AN APNS 
        notifyOpponent(responseBuilder, sender, userId, opponent,
        	opponentId, gh);
      }
      
    } catch (Exception e) {
      log.error("exception in CompletedRoundController processRequestEvent", e);
      
      try {
        //try to tell client that something failed
        responseBuilder.setStatus(CompletedRoundStatus.FAIL_OTHER);
        resEvent.setCompletedRoundResponseProto(responseBuilder.build());
        getEventWriter().handleEvent(resEvent);
        
      } catch (Exception e2) {
        log.error("exception in CompletedRoundController processRequestEvent", e2);
      }
    }
  }

  private boolean isValidRequest(Builder responseBuilder, BasicUserProto sender,
      String userId, String gameId, GameHistory gh, CompleteRoundResultsProto crrp) {
    //does game exist?
    if (null == gh || null == gh.getUnfinishedRound()) {
      log.error("unexpected error: game does not exist. gameId=" + gameId +
          "\t gameHistory=" + gh);
      responseBuilder.setStatus(CompletedRoundStatus.FAIL_GAME_DOES_NOT_EXIST);
      return false;
    }
    
    //correct user completing a round?
    RoundPendingCompletion rpc = gh.getUnfinishedRound();
    String expectedId = rpc.getUserId();
    if (!userId.equals(expectedId)) {
      log.error("unexpected error: wrong user to finish round. userId=" +
          userId + "\t expectedId=" + expectedId);
      return false;
    }
    
    //game completed already?
    if (null != gh.getEndTime()) {
      log.error("unexpected error: game is already finished. startTime=" +
          gh.getStartTime() + "\t endTime=" + gh.getEndTime() + "\t gameId=" +
          gameId);
      responseBuilder.setStatus(CompletedRoundStatus.FAIL_GAME_ALREADY_COMPLETED);
      return false;
    }
    
    return true;
  }

  private boolean writeChangesToDb(String userId, GameHistory gh,
      CompleteRoundResultsProto crrp, List<RoundHistory> rhList) {
    try {
      //delete the unfinished round since the user just finished it
      gh.setUnfinishedRound(null);
      
      //create the just finished round
      int roundNumber = crrp.getRoundNumber();
      //save the questions answered by the user
      Set<QuestionAnswered> qaSet = constructQuestionAnswered(userId, 
          roundNumber, crrp.getAnswersList());
      log.info("going to create round history");
      RoundHistory finishedRound = createRoundHistory(userId, roundNumber,
          qaSet, crrp);
      //record the finished round
      log.info("adding finished round to game history");
      gh.getRoundHistory().add(finishedRound);
      log.info("finished adding round to game history");
      
      //save the game history, set the end date if the game history is over
      int currentRound = gh.getCurrentRoundNumber(); //accounts for finished round
      int maxRounds = PicturesPoConstants.ROUND_HISTORY__DEFAULT_ROUNDS_PER_PLAYER_PER_GAME;
      if (currentRound > maxRounds) {
        log.info("Another game has been completed!!!");
        Date endDate = new Date(crrp.getEndTime());
        gh.setEndTime(endDate);
      }
      log.info("log.info saving game history");
      getGameHistoryService().saveGameHistory(gh);
      log.info("saved game history");
      rhList.add(finishedRound);
      
      return true;
    } catch (Exception e) {
      log.error("unexpected error: problem with saving to db.");
    }
    return false;
  }

  private Set<QuestionAnswered> constructQuestionAnswered(String userId,
      int roundNumber, List<QuestionAnsweredProto> questions) {
    //link question ids to unfinished question answered objects
    Map<String, QuestionAnswered> questionIdsToQuestionAnswered =
        new HashMap<String, QuestionAnswered>();
    //construct all the questions the user answered 
    for (QuestionAnsweredProto qap : questions) {
      String questionId = qap.getQuestionId();
      int questionNumber = qap.getQuestionNumber();
      Date dateAnswered = new Date(qap.getTimeAnswered());
      int answerType = qap.getAnswerType().getNumber();
      
      QuestionAnswered qa = new QuestionAnswered();
      qa.setRoundNumber(roundNumber);
      qa.setQuestionNumber(questionNumber);
      qa.setAnsweredDate(dateAnswered);
      qa.setAnsweredByUser(userId);
      qa.setAnswerType(answerType);
      
      questionIdsToQuestionAnswered.put(questionId, qa);
    }
    
    //save these question answered objects
    Set<QuestionAnswered> answered = getQuestionAnsweredService()
        .saveQuestionAnswered(questionIdsToQuestionAnswered);
    
    return answered;
  } 
  
  //save the just completed round to the database
  private RoundHistory createRoundHistory(String userId, int roundNumber,
      Set<QuestionAnswered> qaSet, CompleteRoundResultsProto crrp) {
    Date roundStarted = new Date(crrp.getStartTime());
    Date roundEnded = new Date(crrp.getEndTime());
    int score = crrp.getScore();

    int computedScore = getQuestionAnsweredService().computeScore(qaSet);
    if (computedScore != score) {
      log.error("unexpected error: client score does not match " +
          " computed score. clientScore=" + score + "\t computedScore=" +
          + computedScore);
    }
    RoundHistory rh = getRoundHistoryService().createRoundHistory(userId,
        roundNumber, roundStarted, roundEnded, qaSet, score); 

    return rh;
  }
  
  private void setResults(Builder responseBuilder, List<RoundHistory> rhList) {
    RoundHistory rh = rhList.get(0);
    BasicRoundResultsProto brrp =
        getCreateNoneventProtoUtils().createBasicRoundResultsProto(rh);
    responseBuilder.setResults(brrp);
  }
  
  //different kinds of action keys and alert bodies based on
  //whether the game started or ended
  private void notifyOpponent(Builder responseBuilder, BasicUserProto sender,
	  String userId, BasicUserProto opponent, String opponentId,
	  GameHistory gh) {
      CompletedRoundResponseEvent resEvent = new CompletedRoundResponseEvent(opponentId);
      
      String actionKey = null;
      String alertBody = null;
      String playerOneId = null;
      int currentRound = 1;
      int maxRounds = PicturesPoConstants.ROUND_HISTORY__DEFAULT_ROUNDS_PER_PLAYER_PER_GAME;
      if (null == gh) {
	  //game started
	  playerOneId = userId;
      } else {
	  playerOneId = gh.getPlayerOneId();
	  currentRound = gh.getCurrentRoundNumber();
      }
      
      //notification for unfinished game
      if (currentRound <= maxRounds && playerOneId.equals(userId)) {
	  //notification for player two
	  actionKey = "View now";
	  alertBody = "Your rival, " + sender.getNameStrangersSee() +
		  ", finished a round. Check the pitiful score and do better!";
      } else if (currentRound <= maxRounds && !playerOneId.equals(userId)) {
	  //notification for player one
	  actionKey = "Continue";
	  alertBody = "Your foe, " + sender.getNameStrangersSee() +
		  ", finished a round. See the dismal score. " +
		  "Maintain your supremacy!";
      } else {
	  //round finished, notify player one
	  actionKey = "View now";
	  alertBody = "The game is over! See the victor!";
      }
      
      CompletedRoundResponseProto responseProto = responseBuilder.build();
      resEvent.setCompletedRoundResponseProto(responseProto);
      getApnsWriter().handleEvent(resEvent, actionKey, alertBody);
  }
  
  
  public GameHistoryService getGameHistoryService() {
    return gameHistoryService;
  }
  
  public void setGameHistoryService(GameHistoryService gameHistoryService) {
    this.gameHistoryService = gameHistoryService;
  }

  public QuestionAnsweredService getQuestionAnsweredService() {
    return questionAnsweredService;
  }

  public void setQuestionAnsweredService(QuestionAnsweredService questionAnsweredService) {
    this.questionAnsweredService = questionAnsweredService;
  }

  public RoundHistoryService getRoundHistoryService() {
    return roundHistoryService;
  }

  public void setRoundHistoryService(RoundHistoryService roundHistoryService) {
    this.roundHistoryService = roundHistoryService;
  }

  public CreateNoneventProtoUtils getCreateNoneventProtoUtils() {
    return createNoneventProtoUtils;
  }

  public void setCreateNoneventProtoUtils(
      CreateNoneventProtoUtils createNoneventProtoUtils) {
    this.createNoneventProtoUtils = createNoneventProtoUtils;
  }
  
}