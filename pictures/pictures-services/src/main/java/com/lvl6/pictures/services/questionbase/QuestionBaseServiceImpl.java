package com.lvl6.pictures.services.questionbase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.services.utils.RandomNumberUtils;

@Component
public class QuestionBaseServiceImpl implements QuestionBaseService {
  
  private static Logger log = LoggerFactory.getLogger(new Object() { }.getClass().getEnclosingClass());
  
  @Resource(name = "questionIdsToQuestions")
  protected Map<String, QuestionBase> questionIdsToQuestions;

  @Autowired
  protected RandomNumberUtils randNumUtils;
  
  @Override
  public List<QuestionBase> getRandomQuestions(int amount,
      Set<String> allPictureNames) {
    List<QuestionBase> qbList = new ArrayList<QuestionBase>();
    
    if (null == getQuestionIdsToQuestions() || getQuestionIdsToQuestions().isEmpty()) {
      log.error("db error: There are no questions to retrieve from the database.");
      return null;
    }
    
    int upperBound = getQuestionIdsToQuestions().size();
    int limit = amount;
    Collection<Integer> randIndexNums =
        getRandNumUtils().generateNRandomIntsBelowInt(upperBound, limit);
    
    QuestionBase[] questions = (QuestionBase[]) getQuestionIdsToQuestions().values().toArray();
    
    for(int index : randIndexNums) {
      QuestionBase qb = questions[index];
      
      Set<String> picNames = qb.getPictureNames();
      allPictureNames.addAll(picNames);
      
    }
    
    return qbList;
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
  public RandomNumberUtils getRandNumUtils() {
    return randNumUtils;
  }

  @Override
  public void setRandNumUtils(RandomNumberUtils randNumUtils) {
    this.randNumUtils = randNumUtils;
  }

}