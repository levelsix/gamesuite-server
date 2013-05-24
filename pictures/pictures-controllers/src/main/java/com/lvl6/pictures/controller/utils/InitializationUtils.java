package com.lvl6.pictures.controller.utils;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.pictures.dao.QuestionBaseDao;
import com.lvl6.pictures.po.QuestionBase;

@Component
public class InitializationUtils implements InitializingBean {

  @Resource(name = "questionIdsToQuestions")
  protected Map<String, QuestionBase> questionIdsToQuestions;

  @Autowired
  protected QuestionBaseDao questionBaseDao;
  
  
  @Override
  public void afterPropertiesSet() throws Exception {
    List<QuestionBase> allQuestions = getQuestionBaseDao().findAll();
    
    for (QuestionBase qb : allQuestions) {
      String id = qb.getId();
      questionIdsToQuestions.put(id, qb);
    }
  }
  
  
  
  public Map<String, QuestionBase> getQuestionIdsToQuestions() {
    return questionIdsToQuestions;
  }


  public void setQuestionIdsToQuestions(
      Map<String, QuestionBase> questionIdsToQuestions) {
    this.questionIdsToQuestions = questionIdsToQuestions;
  }

  
  public QuestionBaseDao getQuestionBaseDao() {
    return questionBaseDao;
  }

  
  public void setQuestionBaseDao(QuestionBaseDao questionBaseDao) {
    this.questionBaseDao = questionBaseDao;
  }
  
}