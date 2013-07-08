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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
public class GameHistory extends BasePersistentObject {

    private static final long serialVersionUID = -6098036693773081768L;

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
    //deleted when either player finishes the round, so can be null
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = true)
    protected RoundPendingCompletion unfinishedRound;


    /**
     * Groups the RoundHistory collection by userIds 
     * (i.e games for playerOne and playerTwo) 
     * @return
     */
    public Map<String, List<RoundHistory>> getUserIdsToRoundHistories() {
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

    public boolean isPlayerTurn(String userId) {
	if (null == userId || userId.isEmpty()) {
	    return false;
	}

	String playerWhoGoesNext = getPlayerWhoGoesNext();
	if (userId.equals(playerWhoGoesNext)) {
	    return true;
	} else {
	    return false;
	}
    }

    public String getPlayerWhoGoesNext() {
	//person who goes next is whoever has the unfinished round 
	if (null != unfinishedRound) {
	    String playerWithUnfinishedRound = unfinishedRound.getUserId();
	    return playerWithUnfinishedRound;
	}

	Map<String, Integer> userIdsToRoundsCompleted =
		getUserIdsToRoundsCompleted();

	int p1RoundsCompleted = userIdsToRoundsCompleted.get(playerOneId);
	int p2RoundsCompleted = userIdsToRoundsCompleted.get(playerTwoId);

	if (p1RoundsCompleted == p2RoundsCompleted) {
	    return playerOneId;
	} else if (p1RoundsCompleted > p2RoundsCompleted) {
	    return playerTwoId;
	} else {
	    throw new RuntimeException("playerTwo has more completed rounds" +
		    " than playerOne. playerOneId=" + playerOneId +
		    "\t playerTwoId" + playerTwoId + "\t gameHistoryId=" + getId());
	}
    }

    public String getOpponentId(String userId) {
	if (playerOneId.equals(userId)) {
	    return playerTwoId;
	} else {
	    return playerOneId;
	}
    }

    //links user ids to how many rounds they completed
    //(including 0 rounds completed)
    public Map<String, Integer> getUserIdsToRoundsCompleted() {
	Map<String, Integer> userIdsToRoundsCompleted =
		new HashMap<String, Integer>();

	Map<String, List<RoundHistory>> userIdsToRoundHistories =
		getUserIdsToRoundHistories();

	if (userIdsToRoundHistories.containsKey(playerOneId)) {
	    int numRounds = userIdsToRoundHistories.get(playerOneId).size();
	    userIdsToRoundsCompleted.put(playerOneId, numRounds);
	} else {
	    userIdsToRoundsCompleted.put(playerOneId, 0);
	}

	if (userIdsToRoundHistories.containsKey(playerTwoId)) {
	    int numRounds = userIdsToRoundHistories.get(playerTwoId).size();
	    userIdsToRoundsCompleted.put(playerTwoId, numRounds);
	} else {
	    userIdsToRoundsCompleted.put(playerTwoId, 0);
	}

	return userIdsToRoundsCompleted;
    }

    //return number of rounds playerOne has completed +
    // 1 if playerOne goes again
    public int getCurrentRoundNumber() {
	Map<String, Integer> userIdsToRoundsCompleted =
		getUserIdsToRoundsCompleted();

	int numRoundsPlayerOne = userIdsToRoundsCompleted.get(playerOneId);

	String playerWhoGoesNext = getPlayerWhoGoesNext();
	int playerOneGoesAgain = 0;
	if (playerOneId.equals(playerWhoGoesNext)) {
	    playerOneGoesAgain = 1;
	}

	return numRoundsPlayerOne + playerOneGoesAgain;
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


    @Override
    public String toString() {
	return ToStringBuilder.reflectionToString(this);
    }

}