package com.lvl6.pictures.services.gamehistory;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lvl6.pictures.dao.GameHistoryDao;
import com.lvl6.pictures.po.GameHistory;

public class GameHistoryServiceImpl implements GameHistoryService {
  
  @Autowired
  protected GameHistoryDao gameHistoryDao;

  //completedAfterThisTime is set only if completedGamesOnly is set
  @Override
  public List<GameHistory> getGameHistoryForUser(String userId, boolean nonCompletedGamesOnly,
      boolean completedGamesOnly, Date completedAfterThisTime, List<String> specificGameHistoryIds) {
    List<GameHistory> returnVal = null;
    
    if (null != specificGameHistoryIds && !specificGameHistoryIds.isEmpty()) {
      returnVal = getGameHistoryDao().findByIdIn(specificGameHistoryIds);
      
    } else if (nonCompletedGamesOnly) {
      returnVal = getGameHistoryDao().findByPlayerOneIdOrPlayerTwoIdAndEndTimeIsNull(userId, userId);
      
    } else if (null != completedAfterThisTime && completedGamesOnly) {
      returnVal = 
          getGameHistoryDao().findByPlayerOneIdOrPlayerTwoIdAndEndTimeGreaterThanOrderByEndTimeDesc(
          userId, userId, completedAfterThisTime);
      
    } else if (completedGamesOnly) {
      //don't forsee this being used much
      returnVal = getGameHistoryDao().findByPlayerOneIdOrPlayerTwoIdAndEndTimeIsNotNull(userId, userId);
    }
    
    return returnVal;
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