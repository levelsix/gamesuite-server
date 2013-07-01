package com.lvl6.pictures.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.eventprotos.ForceLogoutProto.ForceLogoutResponseProto;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.events.response.ForceLogoutResponseEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolRequest;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.authorizeddevice.AuthorizedDeviceService;
import com.lvl6.gamesuite.common.services.user.LoginService;
import com.lvl6.gamesuite.common.services.user.UserSignupService;
import com.lvl6.gamesuite.user.utils.EmailUtil;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginRequestProto;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginRequestProto.LoginType;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto.Builder;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto.LoginConstants;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto.LoginConstants.CurrencyConstants;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto.LoginConstants.QuestionTypeScoringConstants;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto.LoginConstants.RoundConstants;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginResponseProto.LoginResponseStatus;
import com.lvl6.pictures.events.request.LoginRequestEvent;
import com.lvl6.pictures.events.response.LoginResponseEvent;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.GameResultsProto;
import com.lvl6.pictures.noneventprotos.TriviaGameFormatProto.OngoingGameProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicAuthorizedDeviceProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.noneventprotos.UserProto.CompleteUserProto;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.properties.PicturesPoConstants;
import com.lvl6.pictures.services.currency.CurrencyService;
import com.lvl6.pictures.services.gamehistory.GameHistoryService;
import com.lvl6.pictures.services.questionbase.QuestionBaseService;

@Component
public class LoginController extends EventController {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

    @Autowired
    protected UserSignupService userSignupService;

    @Autowired
    protected AuthorizedDeviceService authorizedDeviceService;

    @Autowired
    protected LoginService loginService;

    @Autowired
    protected GameHistoryService gameHistoryService;

    @Autowired
    protected CreateNoneventProtoUtils noneventProtoUtils;

    @Autowired
    protected CurrencyService currencyService; 

    @Autowired
    protected QuestionBaseService questionBaseService;

    @Override
    public RequestEvent createRequestEvent() {
	return new LoginRequestEvent();
    }

    @Override
    public int getEventType() {
	return CommonEventProtocolRequest.C_LOGIN_EVENT_VALUE;
    }

    @Override
    protected void processRequestEvent(RequestEvent event) throws Exception {
	LoginRequestProto reqProto = ((LoginRequestEvent) event).getLoginRequestProto();
	//log.info("reqProto=" + reqProto);

	BasicUserProto sender = reqProto.getSender(); //sender might not have userId
	LoginType lt = reqProto.getLoginType();
	List<String> facebookFriendIds = reqProto.getFacebookFriendIdsList();
	//boolean initializeAccount = reqProto.getInitializeAccount();
	DateTime now = new DateTime();
	//this is what the login request event .java file does...
	String udid = reqProto.getSender().getBadp().getUdid();

	//response to send back to client
	LoginResponseProto.Builder responseBuilder = LoginResponseProto.newBuilder();
	responseBuilder.setStatus(LoginResponseStatus.FAIL_OTHER);
	//CompleteUserProto.Builder cupb = CompleteUserProto.newBuilder();
	LoginResponseEvent resEvent = new LoginResponseEvent(udid);
	resEvent.setTag(event.getTag());

	//sender object might not have userId if user deleted app or something
	List<String> userIdList = new ArrayList<String>();
	List<User> userList = new ArrayList<User>();
	User u = null; 

	try {
	    boolean validRequestArgs = isValidRequestArguments(responseBuilder, sender,
		    lt, now); 
	    boolean validRequest = false;
	    boolean successful = false;

	    if (validRequestArgs) {
		validRequest = isValidRequest(responseBuilder,
			sender, lt, now, userIdList, userList);
	    }

	    if (validRequest) {
		u = getUser(userIdList, userList);
		successful = writeChangesToDb(responseBuilder, sender, lt,
			now, u);
	    }

	    if (successful) {
		//need to set in responseBuilder the collection of picture names
		Set<String> allPictureNames = new HashSet<String>();

		//set the recipient
		//responseBuilder.setRecipient(cupb); //done in writeChangesToDb

		String userId = responseBuilder.getRecipient().getUserId(); 
		// GET ALL THE COMPLETED GAMES THAT FINISHED SOME TIME AGO,
		// OR MIN DEFAULT NUMBER OF GAMES
		setCompletedGames(responseBuilder, userId);

		// GET ALL THE GAMES THAT ARE THE USER'S TURN
		// GET ALL THE GAMES THAT ARE THE OPPONENT'S TURN
		setOngoingGames(responseBuilder, userId, allPictureNames);

		// CONSTRUCT THE NEW TRIVIA QUESTIONS
		setNewQuestions(responseBuilder, userId, allPictureNames);

		//TODO: CONSTRUCT THE LOGIN CONSTANTS
		responseBuilder.addAllPictureNames(allPictureNames);

		if (LoginType.LOGIN_TOKEN == lt) {
		    responseBuilder.setStatus(LoginResponseStatus.SUCCESS_LOGIN_TOKEN);
		}
		if (LoginType.EMAIL_PASSWORD == lt) {
		    responseBuilder.setStatus(LoginResponseStatus.SUCCESS_EMAIL_PASSWORD);
		}
		if (LoginType.FACEBOOK == lt) {
		    // CONSTRUCT THE BASIC USER PROTOS FOR THIS USER'S FACEBOOK FRIENDS
		    responseBuilder.setStatus(LoginResponseStatus.SUCCESS_FACEBOOK_ID);
		}
		if (LoginType.NO_CREDENTIALS == lt) {
		    responseBuilder.setStatus(LoginResponseStatus.SUCCESS_NO_CREDENTIALS);
		}
		setFacebookFriends(responseBuilder, facebookFriendIds);
	    }

	    //set the login constants
	    setConstants(responseBuilder);
	    
	    LoginResponseProto resProto = responseBuilder.build();
	    resEvent.setLoginResponseProto(resProto);

	    log.info("Writing event: " + resEvent);
	    getEventWriter().processPreDBResponseEvent(resEvent, udid);
	} catch (Exception e) {
	    log.error("exception in LoginController processRequestEvent", e);

	    try {
		//try to tell client that something failed
		responseBuilder.setStatus(LoginResponseStatus.FAIL_OTHER);
		resEvent.setLoginResponseProto(responseBuilder.build());
		getEventWriter().processPreDBResponseEvent(resEvent, udid);

	    } catch (Exception e2) {
		log.error("exception2 in RetrieveNewQuestionsController" +
				" processRequestEvent", e2);
	    }
	}
    }

    //only status in responseBuilder is set if at all
    private boolean isValidRequestArguments(Builder responseBuilder, 
	    BasicUserProto sender, LoginType lt, DateTime now) {
	if (null == lt) {
	    log.error("unexpected error: login type is null. sender=" + sender
		    + ", loginType=" + lt);
	    return false;
	}

	BasicAuthorizedDeviceProto badp = sender.getBadp();
	if (LoginType.LOGIN_TOKEN == lt) {
	    String loginToken = badp.getLoginToken();
	    DateTime expiry = new DateTime(badp.getExpirationDate());
	    String userId = sender.getUserId();
	    String badpUserId = badp.getUserId();

	    if (null == badpUserId || !badpUserId.equalsIgnoreCase(userId) || 
		    null == loginToken || loginToken.isEmpty() || 
		    !badp.hasExpirationDate() || expiry.isBefore(now.getMillis()) ) {
		log.error("user error: login-token login is invalid." +
			  "\t badpUserId=" + badpUserId + "\t userId=" + userId +
			  "\t loginToken=" + loginToken + "\t expiry=" + expiry.toDate() +
			  "\t now=" + now.toDate() + "\t expiryMillis" + expiry.toDate().getTime());
		//log.info("badp=" + badp);
		responseBuilder.setStatus(LoginResponseStatus.INVALID_LOGIN_TOKEN);
		return false;
	    } else {
		return true;
	    }
	}

	String facebookId = sender.getFacebookId();
	if (LoginType.FACEBOOK == lt) {
	    if (null == facebookId || facebookId.isEmpty()) {
		log.error("user error: facebookId is invalid. facebookId=" + facebookId);
		responseBuilder.setStatus(LoginResponseStatus.INVALID_FACEBOOK_ID);
		return false;
	    } else {
		return true;
	    }
	}

	String email = sender.getEmail();
	String password = sender.getPassword();
	if (LoginType.EMAIL_PASSWORD == lt) {
	    if (null == email || null == password || email.isEmpty() || password.isEmpty() ||
		    !EmailUtil.isValidEmailAddressFormat(email)) {
		log.error("user error: email, password invalid. email=" + email);
		responseBuilder.setStatus(LoginResponseStatus.INVALID_EMAIL_PASSWORD);
		return false;
	    } else {
		return true;
	    }
	}

	String nameStrangersSee = sender.getNameStrangersSee();
	if (LoginType.NO_CREDENTIALS == lt) {
	    if (null == nameStrangersSee || nameStrangersSee.isEmpty()) {
		log.error("user error: nameStrangersSee not set. nameStrangersSee=" + nameStrangersSee);
		responseBuilder.setStatus(LoginResponseStatus.INVALID_NO_CREDENTIALS);
		return false;
	    } else {
		return true;
	    }
	}
	log.error("unexpected error: loginType=" + lt + ", sender=" + sender + ", now=" + now);
	return false;
    }

    //only status in responseBuilder is set if at all
    private boolean isValidRequest(Builder responseBuilder, BasicUserProto sender,
	    LoginType lt, DateTime now, List<String> userIdList, List<User> userList) {
	if (LoginType.LOGIN_TOKEN == lt) {
	    return isValidLoginToken(responseBuilder, sender, now, userIdList);
	}
	if (LoginType.FACEBOOK == lt) {
	    return isValidFacebookLogin(responseBuilder, sender, userList);
	}
	if (LoginType.EMAIL_PASSWORD == lt) {
	    return isValidEmailPasswordLogin(responseBuilder, sender, userList);
	}
	if (LoginType.NO_CREDENTIALS == lt) {
	    return isValidNoCredentialsLogin(responseBuilder, sender, userList);
	}
	log.error("unexpected error: loginType=" + lt);
	return false;
    }

    private boolean isValidLoginToken(Builder responseBuilder, BasicUserProto sender,
	    DateTime now, List<String> userIdList) {
	log.info("login token validation");
	BasicAuthorizedDeviceProto badp = sender.getBadp();
	String userId = badp.getUserId();
	String udid = badp.getUdid();
	String loginToken = badp.getLoginToken();

	//soo many things that can be checked (should be checked?)...
	//(e.g. all values client sent match db)
	//could search by the BasicAuthorizedDeviceId...
	AuthorizedDevice bad = getAuthorizedDeviceService()
		.checkForExistingAuthorizedDevice(userId, udid);
	//checking the token client sent matches token in database, and db/client-sent user ids match
	if (null != bad && null != bad.getToken() && bad.getToken().equals(loginToken) &&
		null != userId && bad.getUserId().equals(userId)) {
	    userIdList.add(userId);
	    return true;
	}
	log.error("unexpected error: authorizedDevice in the db=" + bad + ", sender=" + sender);
	responseBuilder.setStatus(LoginResponseStatus.INVALID_LOGIN_TOKEN);
	return false;
    }

    private boolean isValidFacebookLogin(Builder responseBuilder, BasicUserProto sender,
	    List<User> userObjList) {
	log.info("facebook login validation");
	String facebookId = sender.getFacebookId();
	String nameNull = null;
	String emailNull = null;
	String udidNull = null;
	List<User> userList = getUserSignupService().checkForExistingUser(facebookId, nameNull,
		emailNull, udidNull);
	if (null != userList && userList.size() == 1) {
	    //could check if some values matched...but what if user deleted app
	    //set userId because what if user deleted app?
	    userObjList.addAll(userList);
	    return true;
	}
	log.error("unexpected error: users in db with facebookId=" + facebookId + 
		",  userList" + userList);
	responseBuilder.setStatus(LoginResponseStatus.INVALID_FACEBOOK_ID);
	return false;
    }

    private boolean isValidEmailPasswordLogin(Builder responseBuilder,
	    BasicUserProto sender, List<User> userObjList) {
	log.info("email, password validation");
	//verify said person exists and email, password match
	String email = sender.getEmail();
	String password = sender.getPassword();
	String nameStrangersSee = sender.getNameStrangersSee();

	String facebookIdNull = null;
	String udidNull = null;

	List<User> userList = getUserSignupService().checkForExistingUser(
		facebookIdNull, nameStrangersSee, email, udidNull);
	if (null == userList || userList.size() != 1) {
	    //don't want to print out password
	    log.error("(?)user error: senderEmail=" + email + ", nameStrangersSee=" +
		    nameStrangersSee + ", (in db) userList=" + userList);
	    responseBuilder.setStatus(LoginResponseStatus.FAIL_OTHER);
	    return false;
	}
	User inDb = userList.get(0);
	//check the user's email password match
	if (loginService.validCredentials(inDb, nameStrangersSee, email, password)) {
	    userObjList.add(inDb);
	    return true;
	}

	log.error("user error: incorrect email and password. email=" + email);
	responseBuilder.setStatus(LoginResponseStatus.INVALID_EMAIL_PASSWORD);
	return false;
    }

    private boolean isValidNoCredentialsLogin(Builder responseBuilder,
	    BasicUserProto sender, List<User> userObjList) {
	log.info("no credentials (aka just name) validation") ;
	String facebookIdNull = null;
	String nameStrangersSee = sender.getNameStrangersSee();
	String emailNull = null;
	String udidNull = null;

	List<User> userList = getUserSignupService().checkForExistingUser(
		facebookIdNull, nameStrangersSee, emailNull, udidNull);
	if (null == userList || userList.size() != 1) {
	    responseBuilder.setStatus(LoginResponseStatus.FAIL_OTHER);
	    log.error("(?)user error: nameStrangersSee=" + nameStrangersSee +
		    ", (in db) userList=" + userList);
	    return false;
	}
	userObjList.addAll(userList);
	return true;
    }

    private User getUser(List<String> userIdList, List<User> userList) {
	if (!userList.isEmpty()) {
	    return userList.get(0);
	}
	String userId = userIdList.get(0);
	return getLoginService().getUserById(userId);

    }

    private boolean writeChangesToDb(Builder responseBuilder, BasicUserProto sender, LoginType lt,
	    DateTime now, User u) {

	String userId = u.getId();
	boolean accountInitialized = u.isAccountInitialized();
	Currency monies = null;
	if (accountInitialized) {
	    // construct the user (with his existing currency and all)
	    monies = getCurrencyService().getCurrencyForUser(userId);

	} else {
	    //give the user the initial coins and stuff, record that he got it
	    monies = getCurrencyService().initializeUserCurrency(userId, now.toDate());
	    u.setAccountInitialized(true);
	}
	if (null == monies) {
	    //every user should have currency!
	    log.error("unexpected error: user does not have currency. userProto=" + sender);
	}

	// RECORD THE USER LOGGING IN (UPDATING THE USER TABLE)
	AuthorizedDevice ad = updateUserLogin(sender, u, now);

	//KICK OFF ALL OTHER PEOPLE WITH THIS USER ACCOUNT
	//idea get the udid's of the authorized devices user has and send a message to those udids
	kickOffOtherDevicesSharingAccount(userId, ad);

	CompleteUserProto cupb = 
		getNoneventProtoUtils().createCompleteUserProto(u, ad, monies);
	//set the recipient
	responseBuilder.setRecipient(cupb);
	
	//log.info("\t\t completeUserProto=" + cupb);

	return true;
    }

    private AuthorizedDevice updateUserLogin(BasicUserProto sender, User u, DateTime now) {
	BasicAuthorizedDeviceProto badp = sender.getBadp(); //would the client have this?

	String udid = badp.getUdid();
	String deviceId = badp.getDeviceId();

	return getLoginService().updateUserLastLogin(u, now, udid, deviceId);
    }

    private void kickOffOtherDevicesSharingAccount(String userId, AuthorizedDevice ad) {
	List<AuthorizedDevice> otherDevices = 
		getAuthorizedDeviceService().devicesSharingUserAccount(userId, ad);

	//send responses to clients telling them to log out immediately
	//TODO: IMPROVE THIS IF POSSIBLE (userId for client to make sure this event is intended for them)
	for (AuthorizedDevice anAuthorizedDevice : otherDevices) {
	    String udid = anAuthorizedDevice.getUdid();
	    ForceLogoutResponseEvent flre = new ForceLogoutResponseEvent(udid);
	    ForceLogoutResponseProto.Builder flrpb =
		    ForceLogoutResponseProto.newBuilder().setUserId(userId);
	    flre.setForceLogoutResponseProto(flrpb.build());
	    getEventWriter().processPreDBResponseEvent(flre, udid);
	}

    }

    private void setFacebookFriends(Builder responseBuilder, List<String> facebookFriendIds) {
	//log.info("\t\t facebookFriendIds=" + facebookFriendIds);
	if (null == facebookFriendIds || facebookFriendIds.isEmpty()) {
	    return;
	}
	List<BasicUserProto> bupList = new ArrayList<BasicUserProto>();
	
	List<User> uList = getLoginService().getFacebookUsers(facebookFriendIds);
	//construct the protos for the users
	for (User u : uList) {
	    AuthorizedDevice adNull = null;
	    BasicUserProto bup = getNoneventProtoUtils().createBasicUserProto(u, adNull, null);
	    bupList.add(bup);
	}

	log.info("\t\t sent facebookFriends=" + uList);
	responseBuilder.addAllFacebookFriendsWithAccounts(bupList);
    }

    private void setCompletedGames(Builder responseBuilder, String userId) {
	//arguments to getGameHistoryForUser(...)
	boolean nonCompletedGamesOnly = false;
	boolean completedGamesOnly = true;
	DateTime now = new DateTime();
	int days = PicturesPoConstants.GAME_HISTORY__DEFAULT_COMPLETED_GAMES_MIN_DAYS_DISPLAYED;
	Date completedAfterThisTime = now.minusDays(days).toDate();
	List<String> specificGameHistoryIdsNull = null;

	List<GameHistory> completedGames =
		getGameHistoryService().getGameHistoryForUser(userId, nonCompletedGamesOnly,
			completedGamesOnly, completedAfterThisTime, specificGameHistoryIdsNull);

	//if user has no recent completed don't do anything
	if (null == completedGames || completedGames.isEmpty()) {
	    return;
	}

	Map<String, BasicUserProto> idsToBups = getNoneventProtoUtils()
		.createIdsToBasicUserProtos(completedGames);

	List<GameResultsProto> ghpList = getNoneventProtoUtils().createGameResultsProtos(
		completedGames, idsToBups);

	responseBuilder.addAllCompletedGames(ghpList);
    }

    //allPicNames is filled up and returned
    private void setOngoingGames(Builder responseBuilder, String userId,
	    Set<String> allPictureNames) {
	//send to client
	List<GameHistory> myTurn = new ArrayList<GameHistory>();
	List<GameHistory> notMyTurn = new ArrayList<GameHistory>();
	List<GameHistory> pendingGamesMyTurn = new ArrayList<GameHistory>();
	List<GameHistory> pendingGamesNotMyTurn = new ArrayList<GameHistory>();
	Set<String> allUserIds = new HashSet<String>();

	boolean anyOngoingGames = getGameHistoryService().groupOngoingGamesForUser(
		userId, myTurn, notMyTurn, pendingGamesMyTurn,
		pendingGamesNotMyTurn, allUserIds);

	if (!anyOngoingGames) {
	    return;
	}

	//need to set in responseBuilder the collection of picture names
	Set<String> picNames = getGameHistoryService().getPictureNamesFromOngoingGames(
		userId, myTurn, pendingGamesMyTurn);
	allPictureNames.addAll(picNames);

	//create the ongoing game protos
	Map<String, BasicUserProto> idsToBups = 
		getNoneventProtoUtils().createIdsToBasicUserProtos(allUserIds);
	List<GameHistory> allMyTurn = new ArrayList<GameHistory>();
	List<GameHistory> allNotMyTurn = new ArrayList<GameHistory>();
	allMyTurn.addAll(myTurn);
	allMyTurn.addAll(pendingGamesMyTurn);
	allNotMyTurn.addAll(notMyTurn);
	allNotMyTurn.addAll(pendingGamesNotMyTurn);

	boolean isUserTurn = true;
	List<OngoingGameProto> myTurnProtos = getNoneventProtoUtils().createOngoingGameProtosForUser(
		allMyTurn, idsToBups, userId, isUserTurn);

	isUserTurn = false;
	List<OngoingGameProto> notMyTurnProtos = getNoneventProtoUtils().createOngoingGameProtosForUser(
		allNotMyTurn, idsToBups, userId, isUserTurn);

	//initially user does not have any games
	if (null != myTurnProtos && !myTurnProtos.isEmpty()) {
	    responseBuilder.addAllMyTurn(myTurnProtos);
	}
	if (null != notMyTurnProtos && !myTurnProtos.isEmpty()) {
	    responseBuilder.addAllNotMyTurn(notMyTurnProtos);
	}
    }

    private void setNewQuestions(Builder responseBuilder, String userId,
	    Set<String> allPictureNames) {
	List<QuestionProto> newQuestions = new ArrayList<QuestionProto>();
	//get all the questions the user has not seen yet
	//TODO: IDEALLY ONES THAT HAVE NOT BEEN GIVEN TO THE USER ALREADY
	//BUT GO RANDOM FOR NOW
	//get all the other questions prioritized by 
	//TODO: IDEALLY
	//1) time user last answered it
	//2) number of times user answered it
	//or some heuristic regarding the two
	//BUT GO RANDOM FOR NOW

	int amount =
		PicturesPoConstants.QUESTION_BASE__DEFAULT_NUM_QUESTIONS_TO_GET;
	List<QuestionBase> questions =
		getQuestionBaseService().getRandomQuestions(amount, allPictureNames);

	if (null != questions && !questions.isEmpty()) {
	    for(QuestionBase qb : questions) {
		QuestionProto proto =
			getNoneventProtoUtils().createQuestionProto(qb);
		newQuestions.add(proto);
	    }
	    //log.info("newQuestions=" + newQuestions);
	    //set responseBuilder
	    responseBuilder.addAllNewQuestions(newQuestions);
	}
    }
    
    
    private void setConstants(Builder responseBuilder) {
	LoginConstants.Builder lcb = LoginConstants.newBuilder();
	CurrencyConstants cc = getCurrencyConstants();
	RoundConstants rc = getRoundConstants();
	QuestionTypeScoringConstants qtsc = getQuestionTypeScoringConstants();
	
	lcb.setCurrencyConstants(cc);
	lcb.setRoundConstants(rc);
	lcb.setScoreTypes(qtsc);
    }
    
    private CurrencyConstants getCurrencyConstants() {
	CurrencyConstants.Builder ccb = CurrencyConstants.newBuilder();
	ccb.setDefaultInitialRubies(PicturesPoConstants.CURRENCY__DEFAULT_INITIAL_RUBIES);
	ccb.setDefaultInitialTokens(PicturesPoConstants.CURRENCY__DEFAULT_INITIAL_TOKENS);
	int secondsTillRefill = 60 * PicturesPoConstants.CURRENCY__MINUTES_FOR_TOKEN_REGENERATION;
	ccb.setNumSecondsUntilRefill(secondsTillRefill);
	return ccb.build();
    }
    
    private RoundConstants getRoundConstants() {
	RoundConstants.Builder rcb = RoundConstants.newBuilder();
	rcb.setDefaultRoundsPerGame(PicturesPoConstants.ROUND_HISTORY__DEFAULT_ROUNDS_PER_PLAYER_PER_GAME);
	rcb.setDefaultMinutesPerRound(PicturesPoConstants.ROUND_HISTORY__DEFAULT_MINUTES_PER_ROUND);
	return rcb.build();
    }
    
    private QuestionTypeScoringConstants getQuestionTypeScoringConstants() {
	QuestionTypeScoringConstants.Builder qtscb =
		QuestionTypeScoringConstants.newBuilder();
	qtscb.setMcqCorrect(PicturesPoConstants.MCQ__POINTS_FOR_CORRECT_ANSWER);
	qtscb.setMcqIncorrect(PicturesPoConstants.MCQ__POINTS_FOR_INCORRECT_ANSWER);
	
	qtscb.setAcqCorrect(PicturesPoConstants.ACQ__POINTS_FOR_CORRECT_ANSWER);
	qtscb.setAcqIncorrect(PicturesPoConstants.ACQ__POINTS_FOR_INCORRECT_ANSWER);
	return qtscb.build();
    }
	    

    public UserSignupService getUserSignupService() {
	return userSignupService;
    }

    public void setUserSignupService(UserSignupService userSignupService) {
	this.userSignupService = userSignupService;
    }

    public AuthorizedDeviceService getAuthorizedDeviceService() {
	return authorizedDeviceService;
    }

    public void setAuthorizedDeviceService(
	    AuthorizedDeviceService authorizedDeviceService) {
	this.authorizedDeviceService = authorizedDeviceService;
    }

    public LoginService getLoginService() {
	return loginService;
    }

    public void setLoginService(LoginService loginService) {
	this.loginService = loginService;
    }

    public GameHistoryService getGameHistoryService() {
	return gameHistoryService;
    }

    public void setGameHistoryService(GameHistoryService gameHistoryService) {
	this.gameHistoryService = gameHistoryService;
    }

    public CreateNoneventProtoUtils getNoneventProtoUtils() {
	return noneventProtoUtils;
    }

    public void setNoneventProtoUtils(CreateNoneventProtoUtils noneventProtoUtils) {
	this.noneventProtoUtils = noneventProtoUtils;
    } 

    public CurrencyService getCurrencyService() {
	return currencyService;
    }

    public void setCurrencyService(CurrencyService currencyService) {
	this.currencyService = currencyService;
    }

    public QuestionBaseService getQuestionBaseService() {
	return questionBaseService;
    }

    public void setQuestionBaseService(QuestionBaseService questionBaseService) {
	this.questionBaseService = questionBaseService;
    }

}