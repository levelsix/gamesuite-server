package com.lvl6.pictures.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.controller.utils.TimeUtils;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.user.LoginService;
import com.lvl6.gamesuite.common.services.user.UserSignupService;
import com.lvl6.pictures.eventprotos.StartRoundEventProto.StartRoundRequestProto;
import com.lvl6.pictures.eventprotos.StartRoundEventProto.StartRoundResponseProto;
import com.lvl6.pictures.eventprotos.StartRoundEventProto.StartRoundResponseProto.Builder;
import com.lvl6.pictures.eventprotos.StartRoundEventProto.StartRoundResponseProto.StartRoundStatus;
import com.lvl6.pictures.events.request.StartRoundRequestEvent;
import com.lvl6.pictures.events.response.StartRoundResponseEvent;
import com.lvl6.pictures.noneventprotos.PicturesEventProtocolProto.PicturesEventProtocolRequest;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.RoundPendingCompletion;
import com.lvl6.pictures.properties.PicturesPoConstants;
import com.lvl6.pictures.services.currency.CurrencyService;
import com.lvl6.pictures.services.gamehistory.GameHistoryService;
import com.lvl6.pictures.services.roundpendingcompletion.RoundPendingCompletionService;


@Component
public class StartRoundController extends EventController {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

    @Autowired
    protected CurrencyService currencyService;

    @Autowired
    protected GameHistoryService gameHistoryService;

    @Autowired
    protected LoginService loginService;

    @Autowired
    protected RoundPendingCompletionService rpcService;

    @Autowired
    protected TimeUtils timeUtils;

    @Autowired
    protected UserSignupService userSignupService;


    @Override
    public RequestEvent createRequestEvent() {
	return new StartRoundRequestEvent();
    }

    @Override
    public int getEventType() {
	return PicturesEventProtocolRequest.C_START_ROUND_EVENT_VALUE;
    }

    @Override
    protected void processRequestEvent(RequestEvent event) throws Exception {
	//stuff client sent
	StartRoundRequestProto reqProto = 
		((StartRoundRequestEvent) event).getStartRoundRequestProto();
	
	//log.info("client sent request proto=" + reqProto);
	
	BasicUserProto sender = reqProto.getSender();
	String userId = sender.getUserId();
	boolean isRandomPlayer = reqProto.getIsRandomPlayer();
	String opponentId = reqProto.getOpponent();
	String gameId = reqProto.getGameId();
	int roundNumber = reqProto.getRoundNumber();
	boolean isPlayerOne = reqProto.getIsPlayerOne();
	long startTime = reqProto.getStartTime();
	Date startDate = new Date(startTime);
	List<QuestionProto> questions = reqProto.getQuestionsList();

	//response to send back to client
	Builder responseBuilder = StartRoundResponseProto.newBuilder();
	responseBuilder.setStatus(StartRoundStatus.FAIL_OTHER);
	responseBuilder.setRecipient(sender);
	StartRoundResponseEvent resEvent = new StartRoundResponseEvent(userId);
	resEvent.setTag(event.getTag());

	//try getting both users
	Set<String> userIds = new HashSet<String>();
	userIds.add(userId);
	if (null != opponentId && !opponentId.isEmpty()) {
	    userIds.add(opponentId);
	}

	try {
	    //read from db
	    Map<String, User> usersByIds = getLoginService().getUserIdsToUsers(userIds);
	    GameHistory gh = null;
	    if (null != gameId && !gameId.isEmpty()) {
		gh = getGameHistoryService().getGameHistoryById(gameId);
	    }
	    //need to get currency to make sure user has enough tokens to play
	    Currency c = getCurrencyService().getCurrencyForUser(userId);

	    //validate request
	    boolean validRequest = isValidRequest(responseBuilder, sender, userId,
		    c, usersByIds, isRandomPlayer, opponentId, roundNumber, gameId, gh, 
		    isPlayerOne, startDate, questions);

	    boolean successful = false;
	    if (validRequest) {
		//if randomPlayer, generate the other player
		if (isRandomPlayer) {
		    opponentId = getRandomPlayer(userId, usersByIds);
		}

		if (null == gh) {
		    gh = getGameHistoryService().constructNewGame(userId,
			    opponentId, startDate);
		}

		successful = writeChangesToDb(userId, opponentId, usersByIds, c,
			gh, roundNumber, questions, startDate);
	    }

	    if (successful) {
		//not using gameId because game might not have existed before now
		responseBuilder.setGameId(gh.getId());
		responseBuilder.setStatus(StartRoundStatus.SUCCESS);
	    }

	    //write to client
	    resEvent.setStartRoundResponseProto(responseBuilder.build());

	    log.info("Writing event: " + resEvent);
	    log.info("responseBuilder=" + responseBuilder);
	    getEventWriter().handleEvent(resEvent);

	} catch (Exception e) {
	    log.error("exception in StartRoundController processRequestEvent", e);

	    try {
		//try to tell client that something failed
		responseBuilder.setStatus(StartRoundStatus.FAIL_OTHER);
		resEvent.setStartRoundResponseProto(responseBuilder.build());
		getEventWriter().handleEvent(resEvent);

	    } catch (Exception e2) {
		log.error("exception in StartRoundController processRequestEvent", e2);
	    }
	}
    }

    private boolean isValidRequest(Builder responseBuilder, BasicUserProto sender,
	    String userId, Currency moneyForUser, Map<String, User> usersByIds,
	    boolean isRandomPlayer, String opponentId, int roundNumber, String gameId,
	    GameHistory gh, boolean isPlayerOne, Date startDate,
	    List<QuestionProto> qpList) {

	if (usersByIds.isEmpty() || !usersByIds.containsKey(userId) ||
		null == moneyForUser) {
	    log.error("unexpected error: something does not exist. userId=" + userId +
		    "\t currency=" + moneyForUser + "\t sender=" + sender);
	    return false;
	}

	if (!getTimeUtils().isSynchronizedWithServerTime(startDate)) {
	    log.error("user error: client time diverges from server time. clientTime="
		    + startDate + ", approximateServerTime=" + new Date());
	    responseBuilder.setStatus(StartRoundStatus.FAIL_CLIENT_TOO_APART_FROM_SERVER_TIME);
	    return false;
	}

	//make sure the user has enough tokens
	int numTokens = moneyForUser.getTokens();
	if (0 >= numTokens) {
	    log.error("user error: user does not have enough tokens to play a game." +
		    " currency=" + moneyForUser + "\t sender=" + sender);
	    responseBuilder.setStatus(StartRoundStatus.FAIL_NOT_ENOUGH_TOKENS);
	    return false;
	}

	if (isRandomPlayer) {
	    //random player only if user is player one and no game exists
	    if (isPlayerOne &&
		    (null == opponentId || opponentId.isEmpty()) &&
		    (null == gameId || gameId.isEmpty())) {
		return true;

	    } else {
		log.error("unexpected error: invalid request for random player. " +
			"\t isPlayerOne=" + isPlayerOne + "\t usersByIds=" + usersByIds +
			"\t opponentId=" + opponentId + "\t gameId=" + gameId + "\t sender="
			+ sender);
		return false;
	    }
	}

	//client wants to battle specific opponent
	if (2 != usersByIds.size() || null == qpList || qpList.isEmpty()) {
	    log.error("unexpected error: insufficient users or questions. " +
		    "usersByIds=" + usersByIds + "\t qpList=" + qpList);
	    return false;
	}

	//check to make sure the game has not ended yet
	int maxRoundsPerPlayer =
		PicturesPoConstants.ROUND_HISTORY__DEFAULT_ROUNDS_PER_PLAYER_PER_GAME;
	if (roundNumber > maxRoundsPerPlayer) {
	    log.error("unexpected error: game ended but user can continue game. " +
		    "maxRoundsPerPlayer=" + maxRoundsPerPlayer + "\t roundNumber=" +
		    roundNumber + "\t gameHistory=" + gh + "\t sender=" + sender);
	    responseBuilder.setStatus(StartRoundStatus.FAIL_GAME_ENDED);
	    return false;
	}

	//make sure the players are the correct opponents
	if (null != gh) {
	    String playerOne = gh.getPlayerOneId();
	    String playerTwo = gh.getPlayerTwoId();
	    //if one of them is player one the other must be player two
	    if (!(playerOne.equals(userId) || playerTwo.equals(userId)) &&
		    !(playerOne.equals(opponentId) || playerTwo.equals(opponentId)) ||
		    userId.equals(opponentId)) {
		log.error("unexpected error: invalid opponents. playerOne=" +
			playerOne + "\t playerTwo=" + playerTwo + "\t sender=" +
			sender + "\t opponentId=" + opponentId);
		responseBuilder.setStatus(StartRoundStatus.FAIL_WRONG_OPPONENTS);
		return false;
	    }
	    //make sure player starting the round is the one who should play next 
	    if (null != gh && !gh.isPlayerTurn(userId)) {
		log.error("unexpected error: it is not this user's turn. sender=" +
			sender + "\t playerOneId=" + playerOne + "\t playerTwoId=" +
			playerTwo + "\t gameHistoryId=" + gh.getId());
		responseBuilder.setStatus(StartRoundStatus.FAIL_NOT_USER_TURN);
		return false;
	    }
	    //make sure the client knows which round it is
	    int currentRoundNumber = gh.getCurrentRoundNumber();
	    if (roundNumber != currentRoundNumber) {
		log.error("unexpected error: client does not know the current round" +
			" number. currentRoundNumber=" + currentRoundNumber + "\t" +
			" client sent roundNumber=" + roundNumber);
		return false;
	    }
	}

	return true;
    }

    //add the random player into usersByIds and return the random user's id
    private String getRandomPlayer(String userId, Map<String, User> usersByIds) {
	User randPlayer = getUserSignupService().getRandomUser();
	String key = randPlayer.getId();
	usersByIds.put(key, randPlayer);

	return key;
    }

    private RoundPendingCompletion createUnfinishedRound(String userId, 
	    int roundNumber, List<QuestionProto> questionProtos) {
	List<String> questionIds = getQuestionIds(questionProtos);
	RoundPendingCompletion rpc = 
		getRpcService().createUnfinishedRound(userId, roundNumber, questionIds);
	return rpc;
    }

    private List<String> getQuestionIds(List<QuestionProto> questions) {
	List<String> questionIds = new ArrayList<String>();

	for (QuestionProto qp : questions) {
	    String qpId = qp.getQuestionId();
	    questionIds.add(qpId);
	}

	return questionIds;
    }

    private boolean writeChangesToDb(String userId, String opponentId,
	    Map<String, User> usersByIds, Currency c, GameHistory gh,
	    int roundNumber, List<QuestionProto> questions, Date startDate) {
	try {
	    RoundPendingCompletion rpc = gh.getUnfinishedRound();
	    boolean startingNewRound = true;

	    if (null != rpc) {
		startingNewRound = false;
		//record restarting the unfinished round
		log.info("restarting round pending completion. rpc=" + rpc);
		getRpcService().restartRoundPendingCompletion(startDate, rpc);

	    } else {
		//record the RoundPendingCompletion to represent the new round
		log.info("creating an unfinished round");
		rpc = createUnfinishedRound(userId, roundNumber,
			questions);
		log.info("created an unfinished round");
		gh.setUnfinishedRound(rpc);
	    }

	    //save the game history
	    log.info("saving to game history. gameHistory=" + gh);
	    getGameHistoryService().saveGameHistory(gh);

	    //don't spend the token if the round is a pending completion round
	    //if the gh has a round pending completion
	    if (startingNewRound) {
		log.info("updating user's currency. currency=" + c);
		getCurrencyService().spendTokenForUser(c, startDate);
	    }
	    return true;
	} catch (Exception e) {
	    log.error("unexpected error: problem with saving to db.", e);
	}
	return false;
    }

    public CurrencyService getCurrencyService() {
	return currencyService;
    }

    public void setCurrencyService(CurrencyService currencyService) {
	this.currencyService = currencyService;
    }

    public GameHistoryService getGameHistoryService() {
	return gameHistoryService;
    }

    public void setGameHistoryService(GameHistoryService gameHistoryService) {
	this.gameHistoryService = gameHistoryService;
    }

    public LoginService getLoginService() {
	return loginService;
    }

    public void setLoginService(LoginService loginService) {
	this.loginService = loginService;
    }

    public RoundPendingCompletionService getRpcService() {
	return rpcService;
    }

    public void setRpcService(RoundPendingCompletionService rpcService) {
	this.rpcService = rpcService;
    }

    public TimeUtils getTimeUtils() {
	return timeUtils;
    }

    public void setTimeUtils(TimeUtils timeUtils) {
	this.timeUtils = timeUtils;
    }

    public UserSignupService getUserSignupService() {
	return userSignupService;
    }

    public void setUserSignupService(UserSignupService userSignupService) {
	this.userSignupService = userSignupService;
    }

}