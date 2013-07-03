package com.lvl6.pictures.services.questionanswered;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.pictures.dao.QuestionAnsweredDao;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.PicturesQuestionWithTextAnswer;
import com.lvl6.pictures.po.QuestionAnswered;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.properties.PicturesPoConstants;

@Component
public class QuestionAnsweredServiceImpl implements QuestionAnsweredService {

    private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());

    @Resource(name = "questionIdsToQuestions")
    protected Map<String, QuestionBase> questionIdsToQuestions;

    @Autowired
    protected QuestionAnsweredDao questionAnsweredDao;


    @Override
    public Set<QuestionAnswered> saveQuestionAnswered(
	    Map<String, QuestionAnswered> questionIdsToQuestionAnswered) {
	Set<QuestionAnswered> qaSet = new HashSet<QuestionAnswered>();

	if (null == getQuestionIdsToQuestions() || getQuestionIdsToQuestions().isEmpty()) {
	    log.error("db error: There are no questions to retrieve from the database.");
	    return null;
	}

	for(String qId : questionIdsToQuestionAnswered.keySet()) {
	    QuestionBase qb = questionIdsToQuestions.get(qId);
	    //get unfinished question answered
	    QuestionAnswered qa = questionIdsToQuestionAnswered.get(qId);
	    qa.setQuestion(qb);

	    qaSet.add(qa);
	}

	List<QuestionAnswered> qaList = questionAnsweredDao.save(qaSet);
	qaSet.clear();
	qaSet.addAll(qaList);
	return qaSet;
    }


    @Override
    public int computeScore(Set<QuestionAnswered> qaSet) {
	int score = 0;

	for (QuestionAnswered qa : qaSet) {
	    int points = getScore(qa);
	    //answerType is defined in TriviaQuestionFormat
	    score += points;
	}
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



    @Override
    public Map<String, QuestionBase> getQuestionIdsToQuestions() {
	return questionIdsToQuestions;
    }

    @Override
    public void setQuestionIdsToQuestions(
	    Map<String, QuestionBase> questionIdsToQuestions) {
	this.questionIdsToQuestions = questionIdsToQuestions;
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