package com.lvl6.pictures.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import com.lvl6.pictures.eventprotos.RetrieveUserGamesEventProto.RetrieveUserGamesRequestProto;
import com.lvl6.pictures.eventprotos.RetrieveUserGamesEventProto.RetrieveUserGamesResponseProto;
import com.lvl6.pictures.eventprotos.RetrieveUserGamesEventProto.RetrieveUserGamesResponseProto.Builder;
import com.lvl6.pictures.eventprotos.RetrieveUserGamesEventProto.RetrieveUserGamesResponseProto.RetrieveUserGamesStatus;
import com.lvl6.pictures.events.request.RetrieveUserGamesRequestEvent;
import com.lvl6.pictures.events.response.RetrieveUserGamesResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.GameResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.OngoingGameProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.services.gamehistory.GameHistoryService;


@Component
public class RetrieveUserGamesController extends EventController {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Autowired
  protected GameHistoryService gameHistoryService;
  
  @Autowired
  protected CreateNoneventProtoUtils noneventProtoUtils;
  
  @Override
  public RequestEvent createRequestEvent() {
    return new RetrieveUserGamesRequestEvent();
  }

  @Override
  public int getEventType() {
    return PicturesEventProtocolRequest.C_RETRIEVE_USER_GAMES_EVENT_VALUE;
  }
  
  @Override
  protected void processRequestEvent(RequestEvent event) throws Exception {
    //stuff client sent
    RetrieveUserGamesRequestProto reqProto = 
        ((RetrieveUserGamesRequestEvent) event).getRetrieveUserGamesRequestProto();
    BasicUserProto sender = reqProto.getSender();
    String userId = sender.getUserId();
    Date timeForCompletedGames = new Date(reqProto.getTimeForCompletedGames());
    
    //response to send back to client
    Builder responseBuilder = RetrieveUserGamesResponseProto.newBuilder();
    responseBuilder.setSender(sender);
    responseBuilder.setStatus(RetrieveUserGamesStatus.FAIL_OTHER);
    
    RetrieveUserGamesResponseEvent resEvent =
        new RetrieveUserGamesResponseEvent(userId);
    resEvent.setTag(event.getTag());
    
    try {
      responseBuilder.setStatus(RetrieveUserGamesStatus.SUCCESS);
      Set<String> allPictureNames = new HashSet<String>();
      
      setCompletedGames(responseBuilder, userId, timeForCompletedGames);
      setOngoingGames(responseBuilder, userId, allPictureNames);
      
      //easier for client to get the pictures to display to the user
      responseBuilder.addAllPictureNames(allPictureNames);
      //write to client
      resEvent.setRetrieveUserGamesResponseProto(responseBuilder.build());
      log.info("Writing event: " + resEvent);
      getEventWriter().handleEvent(resEvent);
      
    } catch (Exception e) {
      log.error("exception in RetrieveUserGamesController processRequestEvent", e);
      
      try {
        //try to tell client that something failed
        responseBuilder.setStatus(RetrieveUserGamesStatus.FAIL_OTHER);
        resEvent.setRetrieveUserGamesResponseProto(responseBuilder.build());
        getEventWriter().handleEvent(resEvent);
        
      } catch (Exception e2) {
        log.error("exception in RetrieveUserGamesController processRequestEvent", e2);
      }
    }
  }

  //copy pasted from login controller
  private void setCompletedGames(Builder responseBuilder, String userId,
	  Date timeForCompletedGames) {
      List<GameHistory> completedGames =
		getGameHistoryService().getCompletedGamesForUser(userId);
	if (null == completedGames || completedGames.isEmpty()) {
	    log.info("there are no completed games");
	    return;
	}
	log.info("completedGames=" + completedGames.size());
	
	//create the BasicUserProtos for all the users in the completed games
	//so the client can display them if desired
	Map<String, BasicUserProto> idsToBups = getNoneventProtoUtils()
		.createIdsToBasicUserProtos(completedGames);
	List<GameResultsProto> ghpList = getNoneventProtoUtils().createGameResultsProtos(
		completedGames, idsToBups);

	responseBuilder.addAllCompletedGames(ghpList);
  }
  
  //copy pasted from login controller
  private void setOngoingGames(Builder responseBuilder, String userId,
	  Set<String> allPictureNames) {
      Set<String> allUserIds = new HashSet<String>();
	List<GameHistory> allMyTurn = new ArrayList<GameHistory>();
	List<GameHistory> allNotMyTurn = new ArrayList<GameHistory>();
	
	boolean anyOngoingGames = getGameHistoryService().getOngoingGamesForUser(
		userId, allPictureNames, allUserIds, allMyTurn, allNotMyTurn);
	if (!anyOngoingGames) {
	    log.info("no ongoing games for user");
	    return;
	}
	//create the BasicUserProtos for all the users in the completed games
	//so the client can display them if desired
	Map<String, BasicUserProto> idsToBups = 
		getNoneventProtoUtils().createIdsToBasicUserProtos(allUserIds);

	boolean isUserTurn = true;
	//create the ongoing game protos
	List<OngoingGameProto> myTurnProtos = getNoneventProtoUtils().createOngoingGameProtosForUser(
		allMyTurn, idsToBups, userId, isUserTurn);
	isUserTurn = false;
	List<OngoingGameProto> notMyTurnProtos = getNoneventProtoUtils().createOngoingGameProtosForUser(
		allNotMyTurn, idsToBups, userId, isUserTurn);

	//initially user does not have any games
	if (null != myTurnProtos && !myTurnProtos.isEmpty()) {
	    responseBuilder.addAllMyTurn(myTurnProtos);
	}
	if (null != notMyTurnProtos && !myTurnProtos.isEmpty()) {
	    responseBuilder.addAllNotMyTurn(notMyTurnProtos);
	}
  }

  public GameHistoryService getGameHistoryService() {
      return gameHistoryService;
  }

  public void setGameHistoryService(GameHistoryService gameHistoryService) {
      this.gameHistoryService = gameHistoryService;
  }

  public CreateNoneventProtoUtils getNoneventProtoUtils() {
      return noneventProtoUtils;
  }

  public void setNoneventProtoUtils(CreateNoneventProtoUtils noneventProtoUtils) {
      this.noneventProtoUtils = noneventProtoUtils;
  }

}