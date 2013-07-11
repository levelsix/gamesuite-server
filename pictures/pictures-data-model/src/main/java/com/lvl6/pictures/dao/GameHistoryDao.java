package com.lvl6.pictures.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lvl6.pictures.po.GameHistory;

public interface GameHistoryDao extends JpaRepository<GameHistory, String> {
    //seems to be (column conjunction (column conjunction...(columnN conjunction columnN+1)))
    //finds incompleted games
    public List<GameHistory> findByEndTimeIsNullAndPlayerOneIdOrPlayerTwoId(
	    String playerOneId, String playerTwoId);

    //find completed games
    public List<GameHistory> findByEndTimeIsNotNullAndPlayerOneIdOrPlayerTwoId(
	    String playerOneId, String playerTwoId);

    //find recent completed games (not sql but hql)
    @Query("select " +
    		"gh " +
	   "from GameHistory gh " +
	   "where gh.endTime != null " +
	    	"and gh.endTime > ?1 " +
	    	"and (gh.playerOneId = ?2 or gh.playerTwoId = ?3)")
    public List<GameHistory> findByEndTimeGreaterThanAndPlayerOneIdOrPlayerTwoIdOrderByEndTimeDesc(
	    Date endTime, String playerOneId, String playerTwoId);

    public GameHistory findById(String gameId);

    public List<GameHistory> findByIdIn(Collection<String> ids);

}
