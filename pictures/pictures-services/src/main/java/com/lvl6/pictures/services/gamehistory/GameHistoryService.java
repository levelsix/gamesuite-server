package com.lvl6.pictures.services.gamehistory;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.lvl6.pictures.dao.GameHistoryDao;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.services.roundpendingcompletion.RoundPendingCompletionService;

public interface GameHistoryService {

    public abstract List<GameHistory> getCompletedGamesForUser(String userId);
    public abstract List<GameHistory> getGameHistoryForUser(String userId,
	    boolean nonCompletedGamesOnly, boolean completedGamesOnly,
	    Date completedAfterThisTime, List<String> specificGameHistoryIds);

    public abstract boolean getOngoingGamesForUser(String userId,
	    Set<String> allPictureNames, Set<String> allUserIds,
	    List<GameHistory> allMyTurn, List<GameHistory> allNotMyTurn);
    public abstract boolean groupOngoingGamesForUser(String userId, List<GameHistory> myTurn,
	    List<GameHistory> notMyTurn, List<GameHistory> pendingGamesMyTurn,
	    List<GameHistory> pendingGamesNotMyTurn, Set<String> allUserIds);

    public abstract Set<String> getPictureNamesFromOngoingGames(String userId,
	    List<GameHistory> myTurn, List<GameHistory> pendingGamesMyTurn);

    public abstract GameHistory constructNewGame(String playerOneId,
	    String playerTwoId, Date startDate);

    public abstract GameHistory getGameHistoryById(String gameId);

    public abstract GameHistory saveGameHistory(GameHistory gh);
    
    public abstract GameHistory deleteRoundPendingCompletion(GameHistory gh);
    

    public abstract GameHistoryDao getGameHistoryDao();

    public abstract void setGameHistoryDao(GameHistoryDao gameHistoryDao);
    
    public abstract RoundPendingCompletionService getRoundPendingCompletionService();
    
    public abstract void setRoundPendingCompletionService(RoundPendingCompletionService rpcs);

}
