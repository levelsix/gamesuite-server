package com.lvl6.pictures.controllers.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.AuthorizedDevice;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.gamesuite.common.services.authorizeddevice.AuthorizedDeviceService;
import com.lvl6.pictures.controller.CompletedRoundController;
import com.lvl6.pictures.controller.CreateAccountViaFacebookController;
import com.lvl6.pictures.controller.LoginController;
import com.lvl6.pictures.controller.StartRoundController;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.dao.CurrencyDao;
import com.lvl6.pictures.dao.GameHistoryDao;
import com.lvl6.pictures.dao.MultipleChoiceQuestionDao;
import com.lvl6.pictures.dao.PictureQuestionWithTextAnswerDao;
import com.lvl6.pictures.dao.QuestionAnsweredDao;
import com.lvl6.pictures.eventprotos.CompletedRoundEventProto.CompletedRoundRequestProto;
import com.lvl6.pictures.eventprotos.CreateAccountEventProto.CreateAccountViaFacebookRequestProto;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginRequestProto;
import com.lvl6.pictures.eventprotos.LoginEventProto.LoginRequestProto.LoginType;
import com.lvl6.pictures.eventprotos.StartRoundEventProto.StartRoundRequestProto;
import com.lvl6.pictures.events.request.CompletedRoundRequestEvent;
import com.lvl6.pictures.events.request.CreateAccountViaFacebookRequestEvent;
import com.lvl6.pictures.events.request.LoginRequestEvent;
import com.lvl6.pictures.events.request.StartRoundRequestEvent;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionAnsweredProto;
import com.lvl6.pictures.noneventprotos.TriviaQuestionFormatProto.QuestionProto;
import com.lvl6.pictures.noneventprotos.TriviaRoundFormatProto.CompleteRoundResultsProto;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.po.AnswerType;
import com.lvl6.pictures.po.GameHistory;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.properties.PicturesPoConstants;
import com.lvl6.pictures.services.gamehistory.GameHistoryService;
import com.lvl6.pictures.services.questionbase.QuestionBaseService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-application-context.xml")
public class TestPicturesController {

	
	private static final Logger log = LoggerFactory.getLogger(TestPicturesController.class);
	
	@Autowired
	protected CreateAccountViaFacebookController cavfc;
	
	@Autowired
	protected StartRoundController src;
	
	@Autowired
	protected CompletedRoundController crc;
	
	@Autowired
	protected LoginController lc;
	
	@Resource
	protected PictureQuestionWithTextAnswerDao pictureDao;

	@Resource
	protected MultipleChoiceQuestionDao multipleChoiceQuestionDao;

	@Resource
	protected QuestionAnsweredDao qaDao;

	@Resource
	protected UserDao userDao;

	@Resource
	protected CurrencyDao currencyDao;
	
	@Autowired
	protected AuthorizedDeviceService authorizedDeviceService;
	
	@Autowired
	protected CreateNoneventProtoUtils createNoneventProtoUtils;
	
	@Autowired
	protected QuestionBaseService questionBaseService;
	
	@Autowired
	protected GameHistoryDao gameHistoryDao;
	
	@Autowired
	protected GameHistoryService gameHistoryService;

	public PicturesQuestionWithTextAnswer createPicturesQuestionWithTextAnswer() {
		PicturesQuestionWithTextAnswer q = new PicturesQuestionWithTextAnswer();
		q.setAnswer("The Answer");
		Set<String> images = new HashSet<String>();
		images.add("image1");
		images.add("image2");
		images.add("image3");
		images.add("image4");
		q.setImages(images);
		q.setCreatedBy("JUnit");
		q = pictureDao.saveAndFlush(q);

		return q;
	}

	public MultipleChoiceQuestion createMultipleChoiceQuestion(AnswerType at) {
		MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
		String question = "Which drink is made from the leaves of a plant?";
		mcq.setQuestion(question);
		mcq.setCreatedBy("JUnit");

		MultipleChoiceAnswer mcaA = new MultipleChoiceAnswer();
		String answerA = "milk";
		mcaA.setAnswer(answerA);
		mcaA.setAnswerType(at);
		mcaA.setCorrect(false);

		MultipleChoiceAnswer mcaB = new MultipleChoiceAnswer();
		String answerB = "orange juice";
		mcaB.setAnswer(answerB);
		mcaB.setAnswerType(at);
		mcaB.setCorrect(false);

		MultipleChoiceAnswer mcaC = new MultipleChoiceAnswer();
		String answerC = "coffee";
		mcaC.setAnswer(answerC);
		mcaC.setAnswerType(at);
		mcaC.setCorrect(false);

		MultipleChoiceAnswer mcaD = new MultipleChoiceAnswer();
		String answerD = "tea";
		mcaD.setAnswer(answerD);
		mcaD.setAnswerType(at);
		mcaD.setCorrect(true);

		Set<MultipleChoiceAnswer> answers = new HashSet<MultipleChoiceAnswer>();
		answers.add(mcaA);
		answers.add(mcaB);
		answers.add(mcaC);
		answers.add(mcaD);
		mcq.setAnswers(answers);

		int size = mcq.getAnswers().size(); 
		assertTrue("size is not >= 2 and <= 6...?!?! size=" + size +
			"\t answers=" + mcq.getAnswers(),
			size >=2 && size <= 6);
		
		mcq = multipleChoiceQuestionDao.saveAndFlush(mcq);
		return mcq;
	}
	
	//returns the user id
	public void createFacebookUser(String facebookId, 
		String nameFriendsSee, String email,
		String udid, String deviceId) {
	    CreateAccountViaFacebookRequestProto.Builder requestBuilder =
		    CreateAccountViaFacebookRequestProto.newBuilder();
	    requestBuilder.setFacebookId(facebookId);
	    requestBuilder.setNameFriendsSee(nameFriendsSee);
	    requestBuilder.setEmail(email);
	    requestBuilder.setUdid(udid);
	    requestBuilder.setDeviceId(deviceId);
	    
	    CreateAccountViaFacebookRequestEvent cavfre =
		    new CreateAccountViaFacebookRequestEvent();
	    cavfre.setCreateAccountViaFacebookRequestProto(requestBuilder.build());
	    cavfre.setTag(1);
	    
	    //tell server to execute the CreateAccountEvent
	    cavfc.handleEvent(cavfre);
		    
	}
	
	public BasicUserProto getBasicUserProto(User u) {
	    String userId = u.getId();
	    AuthorizedDevice exempt = null;
	    String password = null;
	    //get the authorized device
	    List<AuthorizedDevice> adList = getAuthorizedDeviceService()
		    .devicesSharingUserAccount(userId, exempt);
	    assertTrue("expected one device. devices=" + adList,
		    null != adList && 1 == adList.size());
	    
	    AuthorizedDevice ad = adList.get(0);
	    return getCreateNoneventProtoUtils().createBasicUserProto(u, ad, password);
	}
	
	//returns the question
	public List<QuestionBase> genInitialGameStartRoundEvent(BasicUserProto sender,
		String opponentId) {
	    StartRoundRequestProto.Builder requestBuilder =
		    StartRoundRequestProto.newBuilder();
	    requestBuilder.setSender(sender);
	    requestBuilder.setIsRandomPlayer(false);
	    requestBuilder.setOpponent(opponentId);
	    requestBuilder.setRoundNumber(1);
	    requestBuilder.setIsPlayerOne(true);
	    requestBuilder.setStartTime((new Date()).getTime());
	    
	    //get the questions
	    List<QuestionProto> newQuestions = new ArrayList<QuestionProto>(); 
	    Set<String> allPictureNames = new HashSet<String>();
	    int amount = 1;
	    List<QuestionBase> questions =
	        getQuestionBaseService().getRandomQuestions(amount, allPictureNames);
	    
	    for(QuestionBase qb : questions) {
		QuestionProto proto =
			getCreateNoneventProtoUtils().createQuestionProto(qb);
		newQuestions.add(proto);
	    }
	    
	    //add questions to request
	    requestBuilder.addAllQuestions(newQuestions);
	    
	    StartRoundRequestEvent srre = new StartRoundRequestEvent();
	    srre.setTag(1);
	    srre.setStartRoundRequestProto(requestBuilder.build());
	    
	    src.handleEvent(srre);
	    return questions;
	}
	
	private CompleteRoundResultsProto genCompleteRoundResultsProto(List<QuestionBase> qs) {
	    CompleteRoundResultsProto.Builder crrpb = 
		    CompleteRoundResultsProto.newBuilder();
	    
	    //crrpb.setRoundId(value)
	    crrpb.setRoundNumber(1);
	    crrpb.setStartTime((new Date()).getTime());
	    crrpb.setEndTime((new Date()).getTime() + 1000 * 60 * 2);
	    List<Integer> scoreList = new ArrayList<Integer>();
	    
	    //constructing the list from the list of questions given as input
	    List<QuestionAnsweredProto> qapList = 
		    genQuestionAnsweredProto(qs, scoreList);
	    crrpb.setScore(scoreList.get(0));
	    crrpb.addAllAnswers(qapList);
	    
	    log.info("\t\t\t\t\t questionBaseList=" + qs +
		    "\t completedRoundResultsProto=" + crrpb.build());
	    log.info("completedRoundResultsProto=" + crrpb.build());
	    return crrpb.build();
	}
	
	private List<QuestionAnsweredProto> genQuestionAnsweredProto(
		List<QuestionBase> qs, List<Integer> scoreList) {
	    List<QuestionAnsweredProto> qapList =
		    new ArrayList<QuestionAnsweredProto>();
	    int score = 0;
	    for (int i = 0; i < qs.size(); i++) {
		QuestionBase qb = qs.get(i);
		String id = qb.getId();
		QuestionAnsweredProto.AnswerType at =
			QuestionAnsweredProto.AnswerType.CORRECT;
		int questionNumber = i + 1;
		long timeAnswered = (new Date()).getTime();
		
		score += getScoreForCorrectlyAnsweringQuestion(qb);
		
		QuestionAnsweredProto.Builder qapb =
			QuestionAnsweredProto.newBuilder();
		qapb.setQuestionId(id);
		qapb.setAnswerType(at);
		qapb.setQuestionNumber(questionNumber);
		qapb.setTimeAnswered(timeAnswered);
		
		qapList.add(qapb.build());
	    }
	    
	    scoreList.add(score);
	    return qapList;
	}
	
	private int getScoreForCorrectlyAnsweringQuestion(QuestionBase qb) {
	    if (qb instanceof MultipleChoiceQuestion) {
		return PicturesPoConstants.MCQ__POINTS_FOR_CORRECT_ANSWER;
	    } else if (qb instanceof PicturesQuestionWithTextAnswer) {
		return PicturesPoConstants.ACQ__POINTS_FOR_CORRECT_ANSWER;
	    } 
	    return 0;
	}
	
	public void genInitialGameCompletedRoundEvent(BasicUserProto bupOne,
		String bupOneId, BasicUserProto bupTwo, String bupTwoId,
		List<QuestionBase> qs) {
	    CompletedRoundRequestProto.Builder requestBuilder =
		    CompletedRoundRequestProto.newBuilder();
	    
	    requestBuilder.setSender(bupOne);
	    requestBuilder.setOpponent(bupTwo);
	    List<GameHistory> ghList =  getGameHistoryDao().findByEndTimeIsNullAndPlayerOneIdOrPlayerTwoId(
		    bupOneId, bupTwoId);
	    assertTrue("expected existing gamehistory. ghList=" + ghList,
		    null != ghList && 1 == ghList.size());
	    String gameId = ghList.get(0).getId();
	    
	    //set gameid and results
	    requestBuilder.setGameId(gameId);
	    CompleteRoundResultsProto crrp = genCompleteRoundResultsProto(qs);
	    requestBuilder.setResults(crrp);
	    
	    CompletedRoundRequestEvent crre = new CompletedRoundRequestEvent();
	    crre.setTag(1);
	    crre.setCompletedRoundRequestProto(requestBuilder.build());
	    
	    //get server to process event
	    crc.handleEvent(crre);
	}
	
	private void genLoginEvent(BasicUserProto bup, List<String> facebookFriendIds) {
	    LoginRequestProto.Builder lrpb = LoginRequestProto.newBuilder();
	    lrpb.setSender(bup);
	    if (null != facebookFriendIds && !facebookFriendIds.isEmpty()) {
		lrpb.addAllFacebookFriendIds(facebookFriendIds);
	    }
	    lrpb.setLoginType(LoginType.FACEBOOK);
	    
	    LoginRequestEvent lre = new LoginRequestEvent();
	    lre.setTag(1);
	    lre.setLoginRequestProto(lrpb.build());
	    
	    lc.handleEvent(lre);
	}

	@Transactional
	@Rollback(true)
	@Test
	public void testRetrievingOngoingGamesInLoginController() {
	    log.info("Testing Pictures Controllers");
	    //create two users
	    String facebookIdOne = "a";
	    String nameFriendsSeeOne = "Articus";
	    String emailOne = "arturo@level6.com";
	    String udidOne = "udid1";
	    String deviceIdOne = "deviceId1";
	    createFacebookUser(facebookIdOne, nameFriendsSeeOne, emailOne, udidOne, deviceIdOne);

	    String facebookIdTwo = "b";
	    String nameFriendsSeeTwo = "Arthur";
	    String emailTwo = "arthur@level6.com";
	    String udidTwo = "udid2";
	    String deviceIdTwo = "deviceId2";
	    createFacebookUser(facebookIdTwo, nameFriendsSeeTwo, emailTwo, udidTwo, deviceIdTwo);
	    
	    //make sure these guys are there
	    List<User> uOneList = getUserDao().findByFacebookId(facebookIdOne);
	    List<User> uTwoList = getUserDao().findByFacebookId(facebookIdTwo);
	    
	    assertTrue("expected non null/empty list. uOneList=" + uOneList,
		    null != uOneList && 1 == uOneList.size());
	    assertTrue("expected non null/empty list. uTwoList=" + uTwoList,
		    null != uTwoList && 1 == uTwoList.size());

	    User uOne = uOneList.get(0);
	    String uOneId = uOne.getId();
	    User uTwo = uTwoList.get(0);
	    String uTwoId = uTwo.getId();
	    
	    log.info("userOne=" + uOne);
	    log.info("userTwo=" + uTwo);
	    
	    assertTrue("expected user to have id. id=" + uOne.getId(),
		    null != uOne.getId());
	    assertTrue("expected user to have id. id=" + uTwo.getId(),
		    null != uTwo.getId());
	    
	    BasicUserProto bupOne = getBasicUserProto(uOne);
	    BasicUserProto bupTwo = getBasicUserProto(uTwo);
	    //log both of them in
	    
	    log.info("\t\t logging in userone");
	    genLoginEvent(bupOne, null);
	    log.info("\t\t\t logging in userTwo");
	    genLoginEvent(bupTwo, null);
	    
	    //a user should challenge another in a game: StartRoundEvent
	    log.info("\t making playerOne start a game");
	    List<QuestionBase> qs = genInitialGameStartRoundEvent(bupOne, uTwoId);
	    
	    //said user should complete the round
	    log.info("\t making playerOne complete a round");
	    genInitialGameCompletedRoundEvent(bupOne, uOneId, bupTwo, uTwoId, qs);
	    
	    //player two should log in and have uOne as a friend
	    List<String> facebookFriendIds = new ArrayList<String>();
	    facebookFriendIds.add(uOne.getFacebookId());
	    log.info("\t\t (logging userTwo) before he logs " +
		    "in after userOne completed a round");
	    genLoginEvent(bupTwo, facebookFriendIds);
	}

	@Transactional
	@Rollback(true)
	@Test
	public void testGameHistoryServiceImpl() {
	    String userId = "c82d6abd-9736-4ac3-a3d9-6a3812e24cdf";
	    List<GameHistory> completedGames =
			getGameHistoryService().getCompletedGamesForUser(userId);
	    assertTrue("gameHistoryService returned null list.", null != completedGames);
	    assertTrue("wtf, spring query creation from method does not work as expected. " +
			"expected completed games: 0 \t actual: " + completedGames.size(),
			0 == completedGames.size());
	}
	
	@Transactional
	@Rollback(true)
	@Test
	public void testSpendingRubies() {
//	    log.info("Testing Pictures Controllers");
//	    //create two users
//	    String facebookIdOne = "a";
//	    String nameFriendsSeeOne = "Articus";
//	    String emailOne = "arturo@level6.com";
//	    String udidOne = "udid1";
//	    String deviceIdOne = "deviceId1";
//	    createFacebookUser(facebookIdOne, nameFriendsSeeOne, emailOne, udidOne, deviceIdOne);
//
//	    String facebookIdTwo = "b";
//	    String nameFriendsSeeTwo = "Arthur";
//	    String emailTwo = "arthur@level6.com";
//	    String udidTwo = "udid2";
//	    String deviceIdTwo = "deviceId2";
//	    createFacebookUser(facebookIdTwo, nameFriendsSeeTwo, emailTwo, udidTwo, deviceIdTwo);
//	    
//	    //make sure these guys are there
//	    List<User> uOneList = getUserDao().findByFacebookId(facebookIdOne);
//	    List<User> uTwoList = getUserDao().findByFacebookId(facebookIdTwo);
//	    
//	    assertTrue("expected non null/empty list. uOneList=" + uOneList,
//		    null != uOneList && 1 == uOneList.size());
//	    assertTrue("expected non null/empty list. uTwoList=" + uTwoList,
//		    null != uTwoList && 1 == uTwoList.size());
//
//	    User uOne = uOneList.get(0);
//	    String uOneId = uOne.getId();
//	    User uTwo = uTwoList.get(0);
//	    String uTwoId = uTwo.getId();
//	    
//	    BasicUserProto bupOne = getBasicUserProto(uOne);
//	    BasicUserProto bupTwo = getBasicUserProto(uTwo);
//	    
//	    int rubiesToSpend = 21; 
	    //spendRubies(bupOne, uOneId, rubiesToSpend);
	}
	
	private void spendRubies(BasicUserProto bup, String userId, int amount) {
	    
	}

	public PictureQuestionWithTextAnswerDao getPictureDao() {
		return pictureDao;
	}

	public void setPictureDao(PictureQuestionWithTextAnswerDao pictureDao) {
		this.pictureDao = pictureDao;
	}

	public MultipleChoiceQuestionDao getMultipleChoiceDao() {
		return multipleChoiceQuestionDao;
	}

	public void setMultipleChoiceDao(MultipleChoiceQuestionDao multipleChoiceDao) {
		this.multipleChoiceQuestionDao = multipleChoiceDao;
	}

	public QuestionAnsweredDao getQaDao() {
		return qaDao;
	}

	public void setQaDao(QuestionAnsweredDao qaDao) {
		this.qaDao = qaDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public CurrencyDao getCurrencyDao() {
		return currencyDao;
	}

	public void setCurrencyDao(CurrencyDao currencyDao) {
		this.currencyDao = currencyDao;
	}

	public CreateAccountViaFacebookController getCavfc() {
	    return cavfc;
	}

	public void setCavfc(CreateAccountViaFacebookController cavfc) {
	    this.cavfc = cavfc;
	}

	public StartRoundController getSrc() {
	    return src;
	}

	public void setSrc(StartRoundController src) {
	    this.src = src;
	}

	public CompletedRoundController getCrc() {
	    return crc;
	}

	public void setCrc(CompletedRoundController crc) {
	    this.crc = crc;
	}
	
	public LoginController getLc() {
	    return lc;
	}

	public void setLc(LoginController lc) {
	    this.lc = lc;
	}

	public MultipleChoiceQuestionDao getMultipleChoiceQuestionDao() {
	    return multipleChoiceQuestionDao;
	}

	public void setMultipleChoiceQuestionDao(
		MultipleChoiceQuestionDao multipleChoiceQuestionDao) {
	    this.multipleChoiceQuestionDao = multipleChoiceQuestionDao;
	}

	public AuthorizedDeviceService getAuthorizedDeviceService() {
	    return authorizedDeviceService;
	}

	public void setAuthorizedDeviceService(
		AuthorizedDeviceService authorizedDeviceService) {
	    this.authorizedDeviceService = authorizedDeviceService;
	}

	public CreateNoneventProtoUtils getCreateNoneventProtoUtils() {
	    return createNoneventProtoUtils;
	}

	public void setCreateNoneventProtoUtils(
		CreateNoneventProtoUtils createNoneventProtoUtils) {
	    this.createNoneventProtoUtils = createNoneventProtoUtils;
	}

	public QuestionBaseService getQuestionBaseService() {
	    return questionBaseService;
	}

	public void setQuestionBaseService(QuestionBaseService questionBaseService) {
	    this.questionBaseService = questionBaseService;
	}

	public GameHistoryDao getGameHistoryDao() {
	    return gameHistoryDao;
	}

	public void setGameHistoryDao(GameHistoryDao gameHistoryDao) {
	    this.gameHistoryDao = gameHistoryDao;
	}

	public GameHistoryService getGameHistoryService() {
	    return gameHistoryService;
	}

	public void setGameHistoryService(GameHistoryService gameHistoryService) {
	    this.gameHistoryService = gameHistoryService;
	}

}
