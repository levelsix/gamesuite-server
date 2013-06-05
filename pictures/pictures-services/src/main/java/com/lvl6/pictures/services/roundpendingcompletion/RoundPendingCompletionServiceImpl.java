package com.lvl6.pictures.services.roundpendingcompletion;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.pictures.dao.RoundPendingCompletionDao;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.po.RoundPendingCompletion;

@Component
public class RoundPendingCompletionServiceImpl implements RoundPendingCompletionService {
  
  //private static final Logger log = LoggerFactory.getLogger(RoundPendingCompletionServiceImpl.class);
  
  @Resource(name = "questionIdsToQuestions")
  protected Map<String, QuestionBase> questionIdsToQuestions;
  
  @Autowired
  protected RoundPendingCompletionDao roundPendingCompletionDao;
  
  
  @Override
  public RoundPendingCompletion createUnfinishedRound(String userId,
      int roundNumber, List<String> questionBaseIds) {
    Set<QuestionBase> qbList = new HashSet<QuestionBase>();
    
    RoundPendingCompletion rpc = new RoundPendingCompletion();
    rpc.setUserId(userId);
    rpc.setRoundNumber(roundNumber);
    
    //get the questionbase from questionIdsToQuestions
    for (String questionBaseId : questionBaseIds) {
      QuestionBase qb = questionIdsToQuestions.get(questionBaseId);
      qbList.add(qb);
    }
 
    rpc.setQuestions(qbList);
    roundPendingCompletionDao.save(rpc);
    
    return rpc;
  }
  
  @Override
  public void restartRoundPendingCompletion(Date startDate,
      RoundPendingCompletion rpc) {
    rpc.setStartDate(startDate);
    roundPendingCompletionDao.save(rpc);
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
  
  
}