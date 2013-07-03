package com.lvl6.pictures.services.roundhistory;

import java.util.Date;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.pictures.dao.RoundHistoryDao;
import com.lvl6.pictures.po.QuestionAnswered;
import com.lvl6.pictures.po.RoundHistory;

@Component
public class RoundHistoryServiceImpl implements RoundHistoryService {

    private static final Logger log = LoggerFactory.getLogger(RoundHistoryServiceImpl.class);


    @Autowired
    protected RoundHistoryDao roundHistoryDao;


    @Override
    public RoundHistory createRoundHistory(String userId,
	    int roundNumber, Date roundStarted, Date roundEnded,
	    Set<QuestionAnswered> questionsAnswered, int score) {
	RoundHistory rh = new RoundHistory();
	rh.setUserId(userId);
	rh.setScore(score);
	log.info("setting questions answered");
	rh.setQuestionsAnswered(questionsAnswered);
	log.info("finished setting questions answered");
	rh.setRoundEnded(roundEnded);
	rh.setRoundStarted(roundStarted);
	rh.setRoundNumber(roundNumber);

	rh = roundHistoryDao.save(rh);
	return rh;
    }



    @Override
    public RoundHistoryDao getRoundHistoryDao() {
	return roundHistoryDao;
    }

    @Override
    public void setRoundHistoryDao(RoundHistoryDao roundHistoryDao) {
	this.roundHistoryDao = roundHistoryDao;
    }
}