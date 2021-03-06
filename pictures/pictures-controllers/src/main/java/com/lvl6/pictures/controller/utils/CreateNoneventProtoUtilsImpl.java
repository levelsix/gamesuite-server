package com.lvl6.pictures.controller.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.user.LoginService;
import com.lvl6.pictures.dao.RoundHistoryDao;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.GameResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.OngoingGameProto;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.PlayerGameResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.MultipleChoiceAnswerProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.MultipleChoiceQuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.PictureQuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.BasicRoundResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.CompleteRoundResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.UnfinishedRoundProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto.Builder;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.noneventprotos.UserProto.CompleteUserProto;
import com.lvl6.pictures.noneventprotos.UserProto.UserCurrencyProto;
import com.lvl6.pictures.po.AnswerType;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;
import com.lvl6.pictures.po.QuestionAnswered;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.po.RoundHistory;
import com.lvl6.pictures.po.RoundPendingCompletion;


@Component
public class CreateNoneventProtoUtilsImpl implements CreateNoneventProtoUtils {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
    
    @Autowired
    protected UserDao userDao; 

    @Autowired
    protected LoginService loginService;
    
    @Autowired
    protected RoundHistoryDao roundHistoryDao;


    public RoundHistoryDao getRoundHistoryDao() {
		return roundHistoryDao;
	}

	public void setRoundHistoryDao(RoundHistoryDao roundHistoryDao) {
		this.roundHistoryDao = roundHistoryDao;
	}

	@Override
    public Map<String, BasicUserProto> createIdsToBasicUserProtos(List<GameHistory> ghList) {
	Map<String, BasicUserProto> idsToBups = new HashMap<String, BasicUserProto>();

	Set<String> idSet = new HashSet<String>();
	for (GameHistory gh : ghList) {
	    String playerOneId = gh.getPlayerOneId();
	    String playerTwoId = gh.getPlayerTwoId();

	    idSet.add(playerOneId);
	    idSet.add(playerTwoId);
	}
	Map<String, User> idsToUsers = getLoginService().getUserIdsToUsers(idSet);

	AuthorizedDevice adNull = null;
	String passwordNull = null; 
	for (String id : idsToUsers.keySet()) {
	    User u = idsToUsers.get(id);

	    BasicUserProto bup = createBasicUserProto(u, adNull, passwordNull);
	    idsToBups.put(id, bup);
	}

	return idsToBups;
    }

    @Override
    public Map<String, BasicUserProto> createIdsToBasicUserProtos(Collection<String> userIds) {
	Map<String, BasicUserProto> idsToBups = new HashMap<String, BasicUserProto>();
	Set<String> userIdsSet = new HashSet<String>(userIds);
	Map<String, User> idsToUsers = getLoginService().getUserIdsToUsers(userIdsSet);
	AuthorizedDevice adNull = null;
	String passwordNull = null;
	for (String userId : idsToUsers.keySet()) {
	    User u = idsToUsers.get(userId);
	    BasicUserProto bup = createBasicUserProto(u, adNull, passwordNull);
	    idsToBups.put(userId, bup);
	}
	return idsToBups;
    }

    @Override
    public BasicUserProto createBasicUserProto (User aUser, AuthorizedDevice ad,
	    String password) {
	BasicUserProto.Builder bpb = BasicUserProto.newBuilder();
	String userId = aUser.getId();
	String nameStrangersSee = aUser.getNameStrangersSee();
	String nameFriendsSee = aUser.getNameFriendsSee();
	String email = aUser.getEmail();
	String facebookId = aUser.getFacebookId();

	bpb.setUserId(userId);
	if (null != nameStrangersSee && !nameStrangersSee.isEmpty()) {
	    bpb.setNameStrangersSee(nameStrangersSee);
	}
	if (null != nameFriendsSee && !nameFriendsSee.isEmpty()) {
	    bpb.setNameFriendsSee(nameFriendsSee);
	}
	if (null != email && !email.isEmpty()) {
	    bpb.setEmail(email);
	}
	if (null != facebookId && !facebookId.isEmpty()) {
	    bpb.setFacebookId(facebookId);
	}

	if (null != ad) {
	    BasicAuthorizedDeviceProto badp = createBasicAuthorizedDeviceProto(ad, userId); 
	    bpb.setBadp(badp);
	}

	if (null != password && !password.isEmpty()) {
	    bpb.setPassword(password);
	}

	return bpb.build();
    }

    @Override
    public CompleteUserProto createCompleteUserProto(User aUser,
	    AuthorizedDevice ad, Currency monies) {
	CompleteUserProto.Builder cupb = createCompleteUserProtoBuilder(
		aUser, ad, monies);
	return cupb.build();
    }
    
    @Override 
    public CompleteUserProto.Builder createCompleteUserProtoBuilder (User aUser,
	    AuthorizedDevice ad, Currency monies) {
	CompleteUserProto.Builder cupb = CompleteUserProto.newBuilder();

	String userId = aUser.getId();
	String nameStrangersSee = aUser.getNameStrangersSee();
	String nameFriendsSee = aUser.getNameFriendsSee();
	String email = aUser.getEmail();
	String facebookId = aUser.getFacebookId();
	Date lastLogin = aUser.getLastLogin();
	Date signupDate = aUser.getSignupDate();

	if (null != userId) {
	    cupb.setUserId(userId);
	}
	if (null != nameStrangersSee) {
	    cupb.setNameStrangersSee(nameStrangersSee);
	}
	if (null != nameFriendsSee) {
	    cupb.setNameFriendsSee(nameFriendsSee);
	}
	if (null != email) {
	    cupb.setEmail(email);
	}
	if (null != facebookId) {
	    cupb.setFacebookId(facebookId);
	}
	if (null != lastLogin) {
	    cupb.setLastLogin(lastLogin.getTime());
	}
	if (null != signupDate) {
	    cupb.setSignupDate(signupDate.getTime());
	}

	if (null != ad) {
	    BasicAuthorizedDeviceProto badp = createBasicAuthorizedDeviceProto(ad, userId); 
	    cupb.setBadp(badp);
	}
	if (null != monies) {
	    UserCurrencyProto ucp = createUserCurrencyProto(monies);
	    cupb.setCurrency(ucp);
	}
	return cupb;
    }

    @Override
    public BasicAuthorizedDeviceProto createBasicAuthorizedDeviceProto(AuthorizedDevice ad,
	    String userId) {
	Builder bad = BasicAuthorizedDeviceProto.newBuilder();
	String udid = ad.getUdid();
	String loginToken = ad.getToken();
	Date expirationDate = ad.getExpires();
	String deviceId = ad.getDeviceId();

	bad.setBasicAuthorizedDeviceId(ad.getId());
	bad.setUserId(userId);
	bad.setLoginToken(loginToken);
	bad.setExpirationDate(expirationDate.getTime());
	bad.setUdid(udid);
	bad.setDeviceId(deviceId);
	return bad.build();
    }

    @Override
    public UserCurrencyProto createUserCurrencyProto(Currency monies) {
	UserCurrencyProto.Builder ucpb = UserCurrencyProto.newBuilder();

	int numTokens = monies.getTokens();
	ucpb.setNumTokens(numTokens);

	Date lastTokenRefillTime = monies.getLastTokenRefillTime();
	if (null != lastTokenRefillTime) {
	    ucpb.setLastTokenRefillTime(lastTokenRefillTime.getTime());
	}

	int numRubies = monies.getRubies();
	ucpb.setNumRubies(numRubies);

	return ucpb.build();
    }

    //@Transactional
    @Override
    public List<OngoingGameProto> createOngoingGameProtosForUser(List<GameHistory> ghList,
	    Map<String, BasicUserProto> idsToBasicUserProtos, String userId, boolean isUserTurn) {
	List<OngoingGameProto> ongoingGames = new ArrayList<OngoingGameProto>();

	for(GameHistory gh : ghList) {
	    //log.info("gameHistory=" + gh);
	    OngoingGameProto ogp = createOngoingGameProtoForUser(gh, idsToBasicUserProtos, userId,
		    isUserTurn);
	    ongoingGames.add(ogp);
	    //log.info("ogp=" + ogp);
	}
	//log.info("ongoingGameProtoList=" + ongoingGames);
	return ongoingGames;
    }

    //@Transactional
    @Override
    public OngoingGameProto createOngoingGameProtoForUser(GameHistory gh,
	    Map<String, BasicUserProto> idsToBasicUserProtos, String userId, boolean isUserTurn) {
	OngoingGameProto.Builder ogpb = OngoingGameProto.newBuilder();

	//if it is user's turn, create BasicRoundProto if possible
	if (isUserTurn) {
	    //see if user started but didn't finish a round
	    RoundPendingCompletion rpc = gh.getUnfinishedRound();
	    if(null != rpc && rpc.getUserId().equals(userId)) {

		//user must have exited app when answering a round's questions or something
		UnfinishedRoundProto brp = createUnfinishedRoundProto(rpc);
		ogpb.setMyNewRound(brp);

	    } else if (gh.getPlayerTwoId().equals(userId)) {
		//creating for player two, grab player one's last round to construct
		//the new questions
		String playerOneId = gh.getPlayerOneId();

		//if player two has RoundPendingCompletion then this is a duplicate
		RoundHistory opponentsLastRound = gh.getLastRoundHistoryForUser(playerOneId);
		UnfinishedRoundProto brp = createUnfinishedRoundProto(opponentsLastRound);

		ogpb.setMyNewRound(brp);
	    }
	}
	GameResultsProto grp = createGameResultsProto(gh, idsToBasicUserProtos);
	ogpb.setGameSoFar(grp);
	return ogpb.build();
    }

    @Override
    public List<GameResultsProto> createGameResultsProtos(List<GameHistory> ghList,
	    Map<String, BasicUserProto> idsToBups) {

	List<GameResultsProto> returnValue = new ArrayList<GameResultsProto>();
	for (GameHistory gh : ghList) {
	    GameResultsProto grp = createGameResultsProto(gh, idsToBups);
	    returnValue.add(grp);
	}

	return returnValue;
    }

    @Override
    public GameResultsProto createGameResultsProto(GameHistory gh, Map<String, BasicUserProto> userIdToBasicUserProto) {
	GameResultsProto.Builder grpb = GameResultsProto.newBuilder();
	String gameId = gh.getId();

	String playerOneId = gh.getPlayerOneId();
	String playerTwoId = gh.getPlayerTwoId();
	Collection<RoundHistory> playerOneRhs = gh.getRoundHistoryForUser(playerOneId);
	Collection<RoundHistory> playerTwoRhs = gh.getRoundHistoryForUser(playerTwoId);
	BasicUserProto bupPlayerOne = null;
	BasicUserProto bupPlayerTwo = null;

	//get player one basic user proto
	if (!userIdToBasicUserProto.containsKey(playerOneId)) {
	    User playerOne = getUserDao().findById(playerOneId);
	    bupPlayerOne = createBasicUserProto(playerOne, null, null);
	} else {
	    bupPlayerOne = userIdToBasicUserProto.get(playerOneId);
	}
	//get player two basic user proto
	if (!userIdToBasicUserProto.containsKey(playerTwoId)) {
	    User playerTwo = getUserDao().findById(playerTwoId);
	    bupPlayerTwo = createBasicUserProto(playerTwo, null, null);
	} else {
	    bupPlayerTwo = userIdToBasicUserProto.get(playerTwoId);
	}

	PlayerGameResultsProto firstPlayer = createPlayerGameResultsProto(playerOneRhs, bupPlayerOne);
	PlayerGameResultsProto secondPlayer = createPlayerGameResultsProto(playerTwoRhs, bupPlayerTwo);

	grpb.setGameId(gameId);
	grpb.setFirstPlayer(firstPlayer);
	grpb.setSecondPlayer(secondPlayer);
	return grpb.build();
    }

    //All these RoundHistory (and QuestionAnswered within it) should be for one user
    @Override
    public PlayerGameResultsProto createPlayerGameResultsProto(Collection<RoundHistory> rhCollection,
	    BasicUserProto bup) {
	PlayerGameResultsProto.Builder pgrpb = PlayerGameResultsProto.newBuilder();

	pgrpb.setBup(bup);
	//add the round result protos
	for(RoundHistory rh : rhCollection) {
	    //rh.roundEnded or rh.roundStarted for that matter maybe null, meaning the user has not begun
	    //this round
	    if (null == rh.getRoundEnded()) {
		continue;
	    } else {
		BasicRoundResultsProto brrp = createBasicRoundResultsProto(rh);
		pgrpb.addPreviousRoundsStats(brrp);
	    }
	}

	return pgrpb.build();
    }

    @Override
    public UnfinishedRoundProto createUnfinishedRoundProto(RoundPendingCompletion rpc) {
	UnfinishedRoundProto.Builder brpb = UnfinishedRoundProto.newBuilder();

	int roundNumber = rpc.getRoundNumber();
	//to be filled up with the question bases contained in rpc
	List<QuestionProto> qpList = new ArrayList<QuestionProto>();

	Set<QuestionBase> qbList = rpc.getQuestions();
	for (QuestionBase qb : qbList) {
	    QuestionProto qp = createQuestionProto(qb);
	    qpList.add(qp);
	}
	
	brpb.setRoundId(rpc.getId());
	brpb.setRoundNumber(roundNumber);
	brpb.setSecondsRemaning(rpc.getSecondsRemaining());
	brpb.setCurrentQuestionNumber(rpc.getCurrentQuestionNumber());
	brpb.setCurrentScore(rpc.getCurrentScore());
	brpb.addAllQuestions(qpList);
	return brpb.build();
    }

    //@Transactional
    @Override
    public UnfinishedRoundProto createUnfinishedRoundProto(RoundHistory rh) {
	UnfinishedRoundProto.Builder brpb = UnfinishedRoundProto.newBuilder();

	String roundHistoryId = rh.getId();
	//rh = roundHistoryDao.findOne(roundHistoryId);
	int roundNumber = rh.getRoundNumber();
	List<QuestionProto> qpList = new ArrayList<QuestionProto>();
	Set<QuestionAnswered> qaList = rh.getQuestionsAnswered();

	for (QuestionAnswered qa : qaList) {
	    QuestionProto qp = createQuestionProto(qa);

	    qpList.add(qp);
	}

	brpb.setRoundId(roundHistoryId);
	brpb.setRoundNumber(roundNumber);
	brpb.addAllQuestions(qpList);

	return brpb.build();
    }

    @Override
    public BasicRoundResultsProto createBasicRoundResultsProto(RoundHistory rh) {
	BasicRoundResultsProto.Builder brrpb = BasicRoundResultsProto.newBuilder();
	String id = rh.getId();
	//int numQuestionsSeen = rh.getQuestionsAnswered().size();
	//int numQuestionsAnsweredCorrectly = rh.getCorrectAnswers();
	int score = rh.getScore();
	int roundNumber = rh.getRoundNumber();

	brrpb.setRoundId(id);
	//brrpb.setNumQuestionsSeen(numQuestionsSeen);
	//brrpb.setNumQuestionsAnsweredCorrectly(numQuestionsAnsweredCorrectly);
	brrpb.setScore(score);
	brrpb.setRoundNumber(roundNumber);

	return brrpb.build();
    }

    public BasicRoundResultsProto createBasicRoundResultsProto(CompleteRoundResultsProto crrp) {
	BasicRoundResultsProto.Builder brpb = BasicRoundResultsProto.newBuilder();
	brpb.setRoundId(crrp.getRoundId());
	//brpb.setNumQuestionsSeen(crrp.getNumQuestionsSeen());
	//brpb.setNumQuestionsAnsweredCorrectly(crrp.getNumQuestionsAnsweredCorrectly());
	brpb.setScore(crrp.getScore());
	brpb.setRoundNumber(crrp.getRoundNumber());
	return brpb.build();
    }

    @Override
    public QuestionProto createQuestionProto(QuestionBase qb) {
	QuestionProto.Builder qpb = QuestionProto.newBuilder();

	String id = qb.getId();
	if (qb instanceof PicturesQuestionWithTextAnswer) {
	    PictureQuestionProto pqp = createPictureQuestionProto(
		    (PicturesQuestionWithTextAnswer) qb);
	    qpb.setPictures(pqp);
	} else if (qb instanceof MultipleChoiceQuestion) {
	    MultipleChoiceQuestionProto mcqp = createMultipleChoiceQuestionProto(
		    (MultipleChoiceQuestion) qb);
	    qpb.setMultipleChoice(mcqp);
	}

	qpb.setQuestionId(id);
	return qpb.build();
    }

    @Override
    public QuestionProto createQuestionProto(QuestionAnswered qa) {
	QuestionProto.Builder qpb = QuestionProto.newBuilder();

	QuestionBase qb = qa.getQuestion();
	String id = qb.getId();
	if (qb instanceof PicturesQuestionWithTextAnswer) {
	    PictureQuestionProto pqp = createPictureQuestionProto(
		    (PicturesQuestionWithTextAnswer) qb);
	    qpb.setPictures(pqp);

	} else if (qb instanceof MultipleChoiceQuestion) {
	    MultipleChoiceQuestionProto mcqp = createMultipleChoiceQuestionProto(
		    (MultipleChoiceQuestion) qb);
	    qpb.setMultipleChoice(mcqp);
	}

	qpb.setQuestionId(id);
	return qpb.build();
    }

    @Override
    public MultipleChoiceQuestionProto createMultipleChoiceQuestionProto(MultipleChoiceQuestion mcq) {
	MultipleChoiceQuestionProto.Builder mcqpb = MultipleChoiceQuestionProto.newBuilder();
	String question = mcq.getQuestion();
	Set<MultipleChoiceAnswer> mcaSet = mcq.getAnswers();

	mcqpb.setQuestion(question);
	for (MultipleChoiceAnswer mca : mcaSet) {
	    if (mca.isCorrect()) {
		String answerId = mca.getId();
		mcqpb.setAnswerId(answerId);
	    }
	    MultipleChoiceAnswerProto mcap = createMultipleChoiceAnswerProto(mca);
	    mcqpb.addAnswers(mcap);
	}
	return mcqpb.build();
    }

    @Override
    public MultipleChoiceAnswerProto createMultipleChoiceAnswerProto(MultipleChoiceAnswer mca) {
	MultipleChoiceAnswerProto.Builder mcapb = MultipleChoiceAnswerProto.newBuilder();
	String id = mca.getId();
	String answer = mca.getAnswer();
	AnswerType type = mca.getAnswerType();

	mcapb.setMultipleChoiceAnswerId(id);
	mcapb.setAnswer(answer);
	if (AnswerType.PICTURE == type){
	    mcapb.setType(MultipleChoiceAnswerProto.AnswerType.PICTURE);
	} else if (AnswerType.TEXT == type) {
	    mcapb.setType(MultipleChoiceAnswerProto.AnswerType.TEXT);
	}

	return mcapb.build();
    }

    @Override
    public PictureQuestionProto createPictureQuestionProto(PicturesQuestionWithTextAnswer pqwta) {
	PictureQuestionProto.Builder pqpb = PictureQuestionProto.newBuilder();

	Set<String> images = pqwta.getImages();
	String answer = pqwta.getAnswer();

	//images could be null
	for (String img : images) {
	    pqpb.addImageNames(img);
	}
	pqpb.setAnswer(answer);

	return pqpb.build();
    }


    @Override
    public UserDao getUserDao() {
	return userDao;
    }

    @Override
    public void setUserDao(UserDao userDao) {
	this.userDao = userDao;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

}
