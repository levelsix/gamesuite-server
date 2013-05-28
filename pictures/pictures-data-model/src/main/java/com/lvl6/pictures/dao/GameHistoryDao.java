package com.lvl6.pictures.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lvl6.pictures.po.GameHistory;

public interface GameHistoryDao extends JpaRepository<GameHistory, String> {
  
  //finds incompleted games
  public List<GameHistory> findByPlayerOneIdOrPlayerTwoIdAndEndTimeIsNull(
      String playerOneId, String playerTwoId);
  
  //find completed games
  public List<GameHistory> findByPlayerOneIdOrPlayerTwoIdAndEndTimeIsNotNull(
      String playerOneId, String playerTwoId);
  
  //find recent completed games
  public List<GameHistory> findByPlayerOneIdOrPlayerTwoIdAndEndTimeGreaterThanOrderByEndTimeDesc(
      String playerOneId, String playerTwoId, Date endTime);
  
  public GameHistory findById(String gameId);
  
  public List<GameHistory> findByIdIn(Collection<String> ids);
  
}
