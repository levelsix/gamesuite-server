package com.lvl6.pictures.persistence.test;

import java.util.HashSet;
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

import com.lvl6.pictures.dao.MultipleChoiceQuestionDao;
import com.lvl6.pictures.dao.PictureQuestionWithTextAnswerDao;
import com.lvl6.pictures.po.AnswerType;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;




@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-db-pictures-context.xml")
public class TestPicturesJpa {
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	protected PictureQuestionWithTextAnswerDao pictureDao;
	
	@Resource 
	protected MultipleChoiceQuestionDao multipleChoiceQuestionDao;
	
	@Transactional
  @Rollback(true)
	@Test
	public void testSetup() {
		log.info("Testing Pictures JPA");
		PicturesQuestionWithTextAnswer q = new PicturesQuestionWithTextAnswer();
		q.setAnswer("The Answer");
		Set<String> images = new HashSet<String>();
		images.add("image1");
		images.add("image2");
		images.add("image3");
		images.add("image4");
		q.setImages(images);
		q.setCreatedBy("JUnit");
		q = getPictureDao().save(q);
		log.info("Created Picture question: {}", q.getId());
	}
	
	@Transactional
  @Rollback(true)
	@Test
	public void testMultipleChoice() {
	  MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
	  String question = "Which drink is made from the leaves of a plant?";
	  mcq.setQuestion(question);
	  mcq.setCreatedBy("JUnit");
	  
	  MultipleChoiceAnswer mcaA = new MultipleChoiceAnswer();
    String answerA = "milk";
    mcaA.setAnswer(answerA);
    mcaA.setAnswerType(AnswerType.TEXT);
    mcaA.setCorrect(false);
    
    MultipleChoiceAnswer mcaB = new MultipleChoiceAnswer();
    String answerB = "orange juice";
    mcaB.setAnswer(answerB);
    mcaB.setAnswerType(AnswerType.TEXT);
    mcaB.setCorrect(false);
    
    MultipleChoiceAnswer mcaC = new MultipleChoiceAnswer();
    String answerC = "coffee";
    mcaC.setAnswer(answerC);
    mcaC.setAnswerType(AnswerType.TEXT);
    mcaC.setCorrect(false);
    
    MultipleChoiceAnswer mcaD = new MultipleChoiceAnswer();
    String answerD = "tea";
    mcaD.setAnswer(answerD);
    mcaD.setAnswerType(AnswerType.TEXT);
    mcaD.setCorrect(true);
    
    
    Set<MultipleChoiceAnswer> answers = new HashSet<MultipleChoiceAnswer>();
    answers.add(mcaA);
    answers.add(mcaB);
    answers.add(mcaC);
    answers.add(mcaD);
	  mcq.setAnswers(answers);
	  
	  
	  getMultipleChoiceDao().save(mcq);
	}
	
//	@Transactional
//  @Rollback(true)
//  @Test
//  public void testQuestionsAnswered() {
//	  
//	  int roundNumber = 1;
//	  int questionNumber = 1;
//	  int questionNumber2 = 2;
//	  Date answeredDate = new Date();
//	  String answeredByUser = "me";
//	  
//    QuestionAnswered qa = new QuestionAnswered();
//    qa.setRoundNumber(roundNumber);
//    qa.setQuestionNumber(questionNumber);
//    qa.setAnsweredDate(answeredDate);
//    
//    QuestionAnswered qa2 = new QuestionAnswered();
//    qa2.setRoundNumber(roundNumber);
//    qa2.setQuestionNumber(questionNumber2);
//    qa2.setAnsweredDate(answeredDate);
//    qa2.setAnsweredByUser(answeredByUser);
//  }
//
//	@Transactional
//  @Rollback(true)
//  @Test
//  public void testRoundHistory() {
//    
//  }
//	
//	@Transactional
//  @Rollback(true)
//	@Test
//	public void testGameHistory() {
//	  
//	}
	
	

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


}
