package com.lvl6.pictures.services.gamehistory;

import java.util.Date;
import java.util.List;

import com.lvl6.pictures.dao.GameHistoryDao;
import com.lvl6.pictures.po.GameHistory;

public interface GameHistoryService {
  
  public abstract List<GameHistory> getGameHistoryForUser(String userId, boolean nonCompletedGamesOnly,
      boolean completedGamesOnly, Date completedAfterThisTime, List<String> specificGameHistoryIds);
  
  public abstract GameHistoryDao getGameHistoryDao();
  
  public void setGameHistoryDao(GameHistoryDao gameHistoryDao);
}