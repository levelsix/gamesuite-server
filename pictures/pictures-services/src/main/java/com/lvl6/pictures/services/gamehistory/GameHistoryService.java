package com.lvl6.pictures.services.gamehistory;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lvl6.pictures.dao.GameHistoryDao;
import com.lvl6.pictures.po.GameHistory;

public interface GameHistoryService {
  
  public abstract List<GameHistory> getGameHistoryForUser(String userId, boolean nonCompletedGamesOnly,
      boolean completedGamesOnly, Date completedAfterThisTime, List<String> specificGameHistoryIds);
  
  public abstract void groupOngoingGamesForUser(String userId, List<GameHistory> myTurn,
      List<GameHistory> notMyTurn, List<GameHistory> pendingGamesMyTurn,
      List<GameHistory> pendingGamesNotMyTurn, Set<String> allUserIds);
  
  public abstract Set<String> getPictureNamesFromOngoingGames(String userId,
      List<GameHistory> myTurn, List<GameHistory> pendingGamesMyTurn);
  
  public abstract GameHistory getGameHistoryById(String gameId);
  
  public abstract GameHistoryDao getGameHistoryDao();
  
  public abstract void setGameHistoryDao(GameHistoryDao gameHistoryDao);
}