package com.lvl6.pictures.po;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
public class GameHistory extends BasePersistentObject {
  
  @NotNull
  @Index(name = "player_one_id_index")
  protected String playerOneId;

  @NotNull
  @Index(name = "player_two_id_index")
  protected String playerTwoId;
  
  @Temporal(TemporalType.TIMESTAMP)
  protected Date startTime = new Date();
  
  //if null then game is not completed
  @Temporal(TemporalType.TIMESTAMP)
  protected Date endTime;
  
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  protected Set<RoundHistory> roundHistory;

  
  
  public Set<RoundHistory> getRoundHistoryForUser(String userId) {
    Set<RoundHistory> rhSetForUser = new HashSet<RoundHistory>();
    
    for (RoundHistory rh : roundHistory) {
      if (rh.getUserId().equals(userId)) {
        rhSetForUser.add(rh);
      }
    }
    
    return rhSetForUser;
  }
  
  public String getPlayerOneId() {
    return playerOneId;
  }

  public void setPlayerOneId(String playerOneId) {
    this.playerOneId = playerOneId;
  }

  public String getPlayerTwoId() {
    return playerTwoId;
  }

  public void setPlayerTwoId(String playerTwoId) {
    this.playerTwoId = playerTwoId;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Set<RoundHistory> getRoundHistory() {
    return roundHistory;
  }

  public void setRoundHistory(Set<RoundHistory> roundHistory) {
    this.roundHistory = roundHistory;
  }

}