package com.lvl6.pictures.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

  //this is for when:
  //player one begins a round against someone
  //player two begins the round player one finished
  //deleted when either player finishes the round
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  protected RoundPendingCompletion unfinishedRound;

  
  /**
   * Groups the RoundHistory collection by userIds 
   * (i.e games for playerOne and playerTwo) 
   * @return
   */
  public Map<String, List<RoundHistory>> getIdsToRoundHistories() {
    Map<String, List<RoundHistory>> idsToRoundHistories = 
        new HashMap<String, List<RoundHistory>>();
    
    for(RoundHistory rh : roundHistory) {
      String userId = rh.getUserId();
      
      if (idsToRoundHistories.containsKey(userId)) {
        //preexisting round history group, in map, for user
        List<RoundHistory> rhList = idsToRoundHistories.get(userId);
        rhList.add(rh);
      } else {
        //create new round history group, in map, for user
        List<RoundHistory> rhList = new ArrayList<RoundHistory>();
        rhList.add(rh);
        idsToRoundHistories.put(userId, rhList);
      }
    }
    
    return idsToRoundHistories;
  }
  
  public Set<RoundHistory> getRoundHistoryForUser(String userId) {
    Set<RoundHistory> rhSetForUser = new HashSet<RoundHistory>();
    
    for (RoundHistory rh : roundHistory) {
      if (rh.getUserId().equals(userId)) {
        rhSetForUser.add(rh);
      }
    }
    
    return rhSetForUser;
  }
  
  public RoundHistory getLastRoundHistoryForUser(String userId) {
    RoundHistory lastRound = null;
    for (RoundHistory rh : roundHistory) {
      String rhUserId = rh.getUserId();
      if (!rhUserId.equals(userId)) {
        continue;
      }
      if(null == lastRound) {
        lastRound = rh;
        continue;
      }

      int prevRoundNum = lastRound.getRoundNumber();
      int currRoundNum = rh.getRoundNumber();

      if(currRoundNum > prevRoundNum) {
        lastRound = rh;
      }
    }
    return lastRound;
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

  public RoundPendingCompletion getUnfinishedRound() {
    return unfinishedRound;
  }

  public void setUnfinishedRound(RoundPendingCompletion unfinishedRound) {
    this.unfinishedRound = unfinishedRound;
  }

}