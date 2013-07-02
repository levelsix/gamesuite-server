package com.lvl6.pictures.services.roundpendingcompletion;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.pictures.dao.QuestionBaseDao;
import com.lvl6.pictures.dao.RoundPendingCompletionDao;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.po.RoundPendingCompletion;

@Component
public class RoundPendingCompletionServiceImpl implements RoundPendingCompletionService {

    private static final Logger log = LoggerFactory.getLogger(RoundPendingCompletionServiceImpl.class);

    @Resource(name = "questionIdsToQuestions")
    protected Map<String, QuestionBase> questionIdsToQuestions;

    @Autowired
    protected RoundPendingCompletionDao roundPendingCompletionDao;

    @Autowired
    protected QuestionBaseDao qbDao;


    public QuestionBaseDao getQbDao() {
	return qbDao;
    }

    public void setQbDao(QuestionBaseDao qbDao) {
	this.qbDao = qbDao;
    }

    @Override
    public RoundPendingCompletion createUnfinishedRound(String userId,
	    int roundNumber, List<String> questionBaseIds) {
	//log.info("questionBaseIds=" + questionBaseIds);
	Set<QuestionBase> qbList = new HashSet<QuestionBase>();

	RoundPendingCompletion rpc = new RoundPendingCompletion();
	rpc.setUserId(userId);
	rpc.setRoundNumber(roundNumber);

	rpc = roundPendingCompletionDao.save(rpc);
	Iterable<QuestionBase> questions = qbDao.findAll(questionBaseIds);
	//get the questionbase from questionIdsToQuestions
	Iterator<QuestionBase> it = questions.iterator();
	while(it.hasNext()) {
	    QuestionBase qb = it.next();
	    //log.info("questionBase=" + qb);
	    qbList.add(qb);
	}

	//log.info("questionBaseList=" + qbList);
	rpc.setQuestions(qbList);
	//log.info("pre roundPendingCompletion=" + rpc);
	rpc = roundPendingCompletionDao.save(rpc);
	//log.info("post roundPendingCompletion=" + rpc);

	return rpc;
    }

    @Override
    public RoundPendingCompletion restartRoundPendingCompletion(
	    Date startDate, RoundPendingCompletion rpc) {
	rpc.setStartDate(startDate);
	rpc = roundPendingCompletionDao.save(rpc);
	return rpc;
    }

    @Override
    public RoundPendingCompletion updateRoundPendingCompletion(RoundPendingCompletion rpc,
	    int secondsRemaining, int currentQuestionNumber,
	    int currentScore) {
	rpc.setSecondsRemaining(secondsRemaining);
	rpc.setCurrentQuestionNumber(currentQuestionNumber);
	rpc.setCurrentScore(currentScore);

	rpc = roundPendingCompletionDao.save(rpc);
	return rpc;
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
    public RoundPendingCompletionDao getRoundPendingCompletionDao() {
	return roundPendingCompletionDao;
    }
    @Override
    public void setRoundPendingCompletionDao(RoundPendingCompletionDao roundPendingCompletionDao) {
	this.roundPendingCompletionDao = roundPendingCompletionDao;
    }

    @Override
    public String toString() {
	return "RoundPendingCompletionServiceImpl [questionIdsToQuestions="
		+ questionIdsToQuestions + ", roundPendingCompletionDao="
		+ roundPendingCompletionDao + "]";
    }


}