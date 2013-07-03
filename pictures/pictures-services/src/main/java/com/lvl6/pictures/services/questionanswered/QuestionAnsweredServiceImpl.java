package com.lvl6.pictures.services.questionanswered;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.pictures.dao.QuestionAnsweredDao;
import com.lvl6.pictures.dao.QuestionBaseDao;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;
import com.lvl6.pictures.po.QuestionAnswered;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.properties.PicturesPoConstants;

@Component
public class QuestionAnsweredServiceImpl implements QuestionAnsweredService {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

//    @Resource(name = "questionIdsToQuestions")
//    protected Map<String, QuestionBase> questionIdsToQuestions;

    @Autowired
    protected QuestionAnsweredDao questionAnsweredDao;
    
    @Autowired
    protected QuestionBaseDao qbDao;


    @Override
    public Set<QuestionAnswered> saveQuestionAnswered(
	    List<String> questionIdList, List<QuestionAnswered> questionAnsweredList) {
	
	Set<QuestionAnswered> qaSetTemp = new HashSet<QuestionAnswered>();
	Set<QuestionAnswered> qaSet = new HashSet<QuestionAnswered>();
	
	//can't get the questionbase from questionIdsToQuestions due to
	//some hibernate crap
//	if (null == getQuestionIdsToQuestions() || getQuestionIdsToQuestions().isEmpty()) {
//	    log.error("db error: There are no questions to retrieve from the database.");
//	    return null;
//	}
	//need to save the question answered before setting the property 
	//which is objects, hopefully list returned has same ordering as input
	questionAnsweredList = questionAnsweredDao.save(questionAnsweredList);
	
	//getting questions directly from db because can't get the 
	//questionbase from questionIdsToQuestions due to hibernate
	//session info(?)
	Iterable<QuestionBase> questions = qbDao.findAll(questionIdList);
	Iterator<QuestionBase> it = questions.iterator();
	Map<String, QuestionBase> questionIdsToQuestions =
		new HashMap<String, QuestionBase>();
	while(it.hasNext()) {
	    QuestionBase qb = it.next();
	    String qbId = qb.getId();
	    //log.info("questionBase=" + qb);
	    questionIdsToQuestions.put(qbId, qb);
	}
	
	//go through questionId and questionAnswered lists pairing them up
	for(int i = 0; i < questionIdList.size(); i++) {
	    String qId = questionIdList.get(i);
	    QuestionBase qb = questionIdsToQuestions.get(qId);
	    //get unfinished question answered and set it's QuestionBase
	    QuestionAnswered qa = questionAnsweredList.get(i);
	    qa.setQuestion(qb);

	    //add into temp to save the again after setting the QuestionBase
	    qaSetTemp.add(qa);
	}

	List<QuestionAnswered> qaList = questionAnsweredDao.save(qaSetTemp);
	qaSet.addAll(qaList);
	return qaSet;
    }


    @Override
    public int computeScore(Set<QuestionAnswered> qaSet) {
	int score = 0;
	log.info("beginning score=" + score);
	for (QuestionAnswered qa : qaSet) {
	    int points = getScore(qa);
	    //answerType is defined in TriviaQuestionFormat
	    log.info("points won=" + points);
	    score += points;
	}
	log.info("end score=" + score);
	return score;
    }

    private int getScore(QuestionAnswered qa) {
	QuestionBase qb = qa.getQuestion();
	int answerType = qa.getAnswerType();
	int points = 0;

	if (1 == answerType) {//correct answer
	    if (qb instanceof MultipleChoiceQuestion) {
		points = PicturesPoConstants.MCQ__POINTS_FOR_CORRECT_ANSWER;
	    } else if (qb instanceof PicturesQuestionWithTextAnswer) {
		points = PicturesPoConstants.ACQ__POINTS_FOR_CORRECT_ANSWER;
	    }
	    //more question types here

	} else if (2 == answerType) {//incorrect answer
	    if (qb instanceof MultipleChoiceQuestion) {
		points = PicturesPoConstants.MCQ__POINTS_FOR_INCORRECT_ANSWER;
	    } else if (qb instanceof PicturesQuestionWithTextAnswer) {
		points = PicturesPoConstants.ACQ__POINTS_FOR_INCORRECT_ANSWER;
	    } 
	    //more question types here

	} else if (3 == answerType) {

	} else {
	    log.error("unexpected error: undefined answerType. answerType =" +
		    answerType);
	}

	return points;
    }



//    @Override
//    public Map<String, QuestionBase> getQuestionIdsToQuestions() {
//	return questionIdsToQuestions;
//    }
//
//    @Override
//    public void setQuestionIdsToQuestions(
//	    Map<String, QuestionBase> questionIdsToQuestions) {
//	this.questionIdsToQuestions = questionIdsToQuestions;
//    }
    
    public QuestionBaseDao getQbDao() {
	return qbDao;
    }

    public void setQbDao(QuestionBaseDao qbDao) {
	this.qbDao = qbDao;
    }



    @Override
    public QuestionAnsweredDao getQuestionAnsweredDao() {
	return questionAnsweredDao;
    }

    @Override
    public void setQuestionAnsweredDao(QuestionAnsweredDao qad) {
	questionAnsweredDao = qad;
    }

}