package com.lvl6.pictures.services.gamehistory;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.pictures.dao.GameHistoryDao;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.po.RoundHistory;
import com.lvl6.pictures.po.RoundPendingCompletion;

@Component
public class GameHistoryServiceImpl implements GameHistoryService {
  
  @Autowired
  protected GameHistoryDao gameHistoryDao;

  //completedAfterThisTime is set only if completedGamesOnly is set
  @Override
  public List<GameHistory> getGameHistoryForUser(String userId, 
      boolean nonCompletedGamesOnly, boolean completedGamesOnly,
      Date completedAfterThisTime, List<String> specificGameHistoryIds) {
    List<GameHistory> returnVal = null;
    
    if (null != specificGameHistoryIds && !specificGameHistoryIds.isEmpty()) {
      //find specific games
      returnVal = getGameHistoryDao().findByIdIn(specificGameHistoryIds);
      
    } else if (nonCompletedGamesOnly) {
      returnVal =
          getGameHistoryDao().findByPlayerOneIdOrPlayerTwoIdAndEndTimeIsNull(userId, userId);
      
    } else if (null != completedAfterThisTime && completedGamesOnly) {
      //completed games after a certain time
      returnVal = 
          getGameHistoryDao().findByPlayerOneIdOrPlayerTwoIdAndEndTimeGreaterThanOrderByEndTimeDesc(
          userId, userId, completedAfterThisTime);
      
    } else if (completedGamesOnly) {
      //don't forsee this being used much
      returnVal =
          getGameHistoryDao().findByPlayerOneIdOrPlayerTwoIdAndEndTimeIsNotNull(userId, userId);
    }
    
    return returnVal;
  }
  
  //returns false if there are no ongoing games
  @Override
  public boolean groupOngoingGamesForUser(String userId, List<GameHistory> myTurn,
      List<GameHistory> notMyTurn, List<GameHistory> pendingGamesMyTurn,
      List<GameHistory> pendingGamesNotMyTurn, Set<String> allUserIds) {
    boolean nonCompletedGamesOnly = true;
    boolean completedGamesOnly = false;
    Date completedAfterThisTime = null;
    List<String> specificGameHistoryIds = null;

    // GET ALL THE GAMES THAT ARE THE USER'S TURN
    // GET ALL THE GAMES THAT ARE THE OPPONENT'S TURN
    List<GameHistory> ongoingGames = getGameHistoryForUser(
        userId, nonCompletedGamesOnly, completedGamesOnly, 
        completedAfterThisTime, specificGameHistoryIds);
    
    if (null == ongoingGames || ongoingGames.isEmpty()) {
      return false;
    }

    //for each game sort it into one of the above lists
    for(GameHistory gh : ongoingGames) {
      allUserIds.add(gh.getPlayerOneId());
      allUserIds.add(gh.getPlayerTwoId());
      RoundPendingCompletion rpc = gh.getUnfinishedRound(); //could be null
      
      //check if anyone started a round in a game but didn't finish it
      //store them into pending*Turn
      if(unfinishedRoundExists(rpc, userId, pendingGamesMyTurn,
          pendingGamesNotMyTurn, gh)) {
        continue;
      }
      //store gh in either myTurn or notMyTurn
      determineTurnForGame(userId, gh, myTurn, notMyTurn);
    }
    return true;
  }
  
  //pendingGamesMyTurn or pendingGamesNotMyTurn will contain the return value
  private boolean unfinishedRoundExists(RoundPendingCompletion rpc, String userId,
      List<GameHistory> pendingGamesMyTurn, List<GameHistory> pendingGamesNotMyTurn,
      GameHistory gh) {
    
    if (null != rpc) {
      String rpcUserId = rpc.getUserId();
      if (rpcUserId.equals(userId)) {
        //this happens if the user started a round and then game crashed
        pendingGamesMyTurn.add(gh);
      } else {
        pendingGamesNotMyTurn.add(gh);
      }
      //pending round exists, and determined whose turn it is
      return true;
    }
    return false;
  }
  
  private void determineTurnForGame(String userId, GameHistory gh,
      List<GameHistory> myTurn, List<GameHistory> notMyTurn) {
    String playerOneId = gh.getPlayerOneId();
    String playerTwoId = gh.getPlayerTwoId();
    int playerOneRounds = 0;
    int playerTwoRounds = 0;
    boolean isPlayerOneTurn = false;
    boolean userIsPlayerOne = true;
    
    Map<String, List<RoundHistory>> idsToRoundHistories =
        gh.getUserIdsToRoundHistories();
    
    if (idsToRoundHistories.containsKey(playerOneId)) {
      playerOneRounds = idsToRoundHistories.get(playerOneId).size();
    }
    if (idsToRoundHistories.containsKey(playerTwoId)) {
      playerTwoRounds = idsToRoundHistories.get(playerTwoId).size();
    } 
    
    if (playerOneRounds == playerTwoRounds) {
      isPlayerOneTurn = true;
    } //else it's player two's turn
    
    if (!userId.equals(playerOneId)) {
      userIsPlayerOne = false;
    } //else user is player one
    
    if ((isPlayerOneTurn && userIsPlayerOne) ||
        (!isPlayerOneTurn && !userIsPlayerOne)) {
      //user's turn
      myTurn.add(gh);
    } else {
      notMyTurn.add(gh);
    }
  }
  
  @Override
  public Set<String> getPictureNamesFromOngoingGames(String userId,
      List<GameHistory> myTurn, List<GameHistory> pendingGamesMyTurn) {
    Set<String> allPicNames = new HashSet<String>();

    for(GameHistory gh: myTurn) {
      String playerOneId = gh.getPlayerOneId();
      if (playerOneId.equals(userId)) {
        continue;
      } else {
        RoundHistory rh = gh.getLastRoundHistoryForUser(userId);
        Set<String> picNames = rh.getPictureNames();
        allPicNames.addAll(picNames);
      }
    }
    
    for (GameHistory gh : pendingGamesMyTurn) {
      RoundPendingCompletion rpc = gh.getUnfinishedRound();
      Set<QuestionBase> qbSet = rpc.getQuestions();
      for (QuestionBase qb : qbSet) {
        Set<String> picNames = qb.getPictureNames();
        allPicNames.addAll(picNames);
      }
    }
    
    return allPicNames;
  }
  
  @Override
  public GameHistory getGameHistoryById(String gameId) {
    return gameHistoryDao.findById(gameId);
  }
  
  @Override
  public void saveGameHistory(GameHistory gh) {
    gameHistoryDao.save(gh);
  }
  
  @Override
  public GameHistoryDao getGameHistoryDao() {
    return gameHistoryDao;
  }

  @Override
  public void setGameHistoryDao(GameHistoryDao gameHistoryDao) {
    this.gameHistoryDao = gameHistoryDao;
  }
  
}