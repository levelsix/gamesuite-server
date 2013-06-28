package com.lvl6.pictures.persistence.test;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.lvl6.gamesuite.common.dao.UserDao;
import com.lvl6.gamesuite.common.po.User;
import com.lvl6.pictures.dao.CurrencyDao;
import com.lvl6.pictures.dao.MultipleChoiceQuestionDao;
import com.lvl6.pictures.dao.PictureQuestionWithTextAnswerDao;
import com.lvl6.pictures.dao.QuestionAnsweredDao;
import com.lvl6.pictures.po.AnswerType;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;
import com.lvl6.pictures.po.QuestionAnswered;
import com.lvl6.pictures.po.QuestionBase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-db-pictures-context.xml")
public class TestPicturesJpa {

	
	private static final Logger log = LoggerFactory.getLogger(TestPicturesJpa.class);

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
		pictureDao.saveAndFlush(q);

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
		
		multipleChoiceQuestionDao.saveAndFlush(mcq);
		return mcq;
	}

	@Transactional
	@Rollback(true)
	@Test
	public void testSetup() {
		log.info("Testing Pictures JPA");
		PicturesQuestionWithTextAnswer q = createPicturesQuestionWithTextAnswer();
		getPictureDao().save(q);
		log.info("Created Picture question: {}", q.getId());

		List<PicturesQuestionWithTextAnswer> pqwtaList = getPictureDao().findAll();
		assertTrue("Expected: not null. Actual: " + pqwtaList.size(), null != pqwtaList
				&& pqwtaList.size() >= 1);
	}

	@Transactional
	@Rollback(true)
	@Test
	public void testMultipleChoice() {
		// making sure saving multiple choice questions to the database works
		MultipleChoiceQuestion mcq = createMultipleChoiceQuestion(AnswerType.TEXT);
		MultipleChoiceQuestion mcqImages = createMultipleChoiceQuestion(AnswerType.PICTURE);
		List<MultipleChoiceQuestion> mcqList = new ArrayList<MultipleChoiceQuestion>();
		mcqList.add(mcq);
		mcqList.add(mcqImages);
		getMultipleChoiceDao().save(mcqList);
		List<MultipleChoiceQuestion> inDb = getMultipleChoiceDao().findAll();

		assertTrue("multiple choice questions. Expected: " + mcqList.size() + ". Actually: " + inDb.size(),
				inDb.size() >= 2);

		MultipleChoiceQuestion mcq1 = inDb.get(0);
		MultipleChoiceQuestion mcq2 = inDb.get(1);
		for(MultipleChoiceQuestion q : inDb) {
			log.info("Question: {}", q.toString());
		}
		assertTrue("Expected: not null. Actually: " + mcq1, null != mcq1.getId() && !(mcq1.getId().isEmpty()));
		assertTrue("Expected: not null. Actually: " + mcq2, null != mcq2.getId() && !(mcq2.getId().isEmpty()));
	}

	@Transactional
	@Rollback(true)
	@Test
	public void testQuestionsAnswered() {
		// checking whether saving question answered to the database works
		PicturesQuestionWithTextAnswer pqwta = createPicturesQuestionWithTextAnswer();
		MultipleChoiceQuestion mcq = createMultipleChoiceQuestion(AnswerType.TEXT);

		int roundNumber = 1;
		int questionNumber = 1;
		int questionNumber2 = 2;
		Date answeredDate = new Date();
		String answeredByUser = "me";
		int answerType = 1;

		QuestionAnswered qa = new QuestionAnswered();
		qa.setRoundNumber(roundNumber);
		qa.setQuestionNumber(questionNumber);
		qa.setAnsweredDate(answeredDate);
		qa.setAnsweredByUser(answeredByUser);
		qa.setQuestion(pqwta);
		qa.setAnswerType(answerType);

		QuestionAnswered qa2 = new QuestionAnswered();
		qa2.setRoundNumber(roundNumber);
		qa2.setQuestionNumber(questionNumber2);
		qa2.setAnsweredDate(answeredDate);
		qa2.setAnsweredByUser(answeredByUser);
		qa2.setQuestion(mcq);
		qa2.setAnswerType(answerType);

		List<QuestionAnswered> qaList = new ArrayList<QuestionAnswered>();
		qaList.add(qa);
		qaList.add(qa2);

		getQaDao().save(qaList);

		List<QuestionAnswered> inDb = getQaDao().findAll();

		assertTrue("Expected: qaList. Actually: " + inDb, inDb.size() >= 2);
		assertTrue("Expected: not null. Actually: " + qa, null != qa.getId() && !(qa.getId().isEmpty()));
		assertTrue("Expected: not null. Actually: " + mcq, null != mcq.getId() && !(mcq.getId().isEmpty()));

		int pqwtaCount = 0;
		int mcqCount = 0;
		for (QuestionAnswered qaInDb : inDb) {
			QuestionBase qb = qaInDb.getQuestion();
			if (qb instanceof MultipleChoiceQuestion) {
				mcqCount += 1;
				// log.info("question base: " + qb);
			} else if (qb instanceof PicturesQuestionWithTextAnswer) {
				pqwtaCount += 1;
				// log.info("question base: " + qb);
			} else {
				assertTrue("unknown question base", 1 == 0);
			}
		}

		assertTrue("Expected: pqwtaCount=1. Actual: pqwtaCount=" + pqwtaCount, pqwtaCount >= 1);
		assertTrue("Expected: mcqCount=1. Actual: mcqCount=" + mcqCount, mcqCount >= 1);

		// seeing if saving a question id (tied to an existing question) works
		// or
		// if the whole thing is needed
		// int questionNumber3 = 3;
		// QuestionAnswered qa3 = new QuestionAnswered();
		// qa3.setRoundNumber(roundNumber);
		// qa3.setQuestionNumber(questionNumber3);
		// qa3.setAnsweredDate(answeredDate);
		// qa3.setAnsweredByUser(answeredByUser);
		// qa3.setQuestion(pqwta);
		// TRY SAVING TO DB
		// getQaDao().save(qa3);
		// DOESN'T WORK, NEED TO RETRIEVE FROM DB
	}

	@Transactional
	@Rollback(true)
	@Test
	public void testUserCurrency() {
		User u = new User();
		String nameStrangersSee = "food picker";
		Date signupDate = new Date();
		Date lastLogin = new Date();
		u.setNameStrangersSee(nameStrangersSee);
		u.setSignupDate(signupDate);
		u.setLastLogin(lastLogin);

		userDao.save(u);

		assertTrue("user=" + u, null != u.getId());

		Currency monies = new Currency();
		int tokens = 10;
		Date lastTokenRefillTime = signupDate;
		int rubies = 10;
		monies.setTokens(tokens);
		monies.setLastTokenRefillTime(lastTokenRefillTime);
		monies.setRubies(rubies);
		monies.setUserId(u.getId());

		assertTrue("monies=" + monies, null == monies.getId());
		currencyDao.save(monies);
		assertTrue("monies=" + monies, null != monies.getId());

		// retrieve from db
		Currency currencyInDb = currencyDao.findByUserId(u.getId());

		assertTrue("currencyInDb=" + currencyInDb, null != currencyInDb && null != currencyInDb.getId());
	}

	//
	// @Transactional
	// @Rollback(true)
	// @Test
	// public void testRoundHistory() {
	//
	// }
	//
	// @Transactional
	// @Rollback(false)
	// @Test
	// public void testGameHistory() {
	// int numQuestionsSeen = 4;
	// int numQuestionsAnsweredCorrectly = 0;
	// int score = 0;
	// int roundNumber = 1;
	//
	// }

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

}
