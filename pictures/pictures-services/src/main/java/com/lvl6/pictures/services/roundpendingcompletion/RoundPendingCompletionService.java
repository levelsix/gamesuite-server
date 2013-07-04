package com.lvl6.pictures.services.roundpendingcompletion;

import java.util.Date;
import java.util.List;

import com.lvl6.pictures.dao.RoundPendingCompletionDao;
import com.lvl6.pictures.po.RoundPendingCompletion;

public interface RoundPendingCompletionService {

    public abstract RoundPendingCompletion createUnfinishedRound(String userId,
	    int roundNumber, List<String> questionBaseIds);

    public abstract RoundPendingCompletion restartRoundPendingCompletion(
	    Date startDate, RoundPendingCompletion rpc);

    public abstract RoundPendingCompletion updateRoundPendingCompletion(
	    RoundPendingCompletion rpc, int secondsRemaining, 
	    int currentQuestionNumber, int currentScore);

    public abstract void deleteRoundPendingCompletion(RoundPendingCompletion rpc);
    
//    public Map<String, QuestionBase> getQuestionIdsToQuestions();
//    public void setQuestionIdsToQuestions(
//	    Map<String, QuestionBase> questionIdsToQuestions);

    public abstract RoundPendingCompletionDao getRoundPendingCompletionDao();
    public abstract void setRoundPendingCompletionDao(RoundPendingCompletionDao
	    roundPendingCompletionDao);

}