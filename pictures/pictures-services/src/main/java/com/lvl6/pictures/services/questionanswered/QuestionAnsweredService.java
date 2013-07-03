package com.lvl6.pictures.services.questionanswered;

import java.util.Map;
import java.util.Set;

import com.lvl6.pictures.dao.QuestionAnsweredDao;
import com.lvl6.pictures.po.QuestionAnswered;

public interface QuestionAnsweredService {
  
  public abstract Set<QuestionAnswered> saveQuestionAnswered(
      Map<String, QuestionAnswered> questionIdsToQuestionAnswered);
  
  public abstract int computeScore(Set<QuestionAnswered> qaSet);
  
//  public abstract Map<String, QuestionBase> getQuestionIdsToQuestions();
//  
//  public abstract void setQuestionIdsToQuestions(
//      Map<String, QuestionBase> questionIdsToQuestions);

  public abstract QuestionAnsweredDao getQuestionAnsweredDao();
  
  public abstract void setQuestionAnsweredDao(QuestionAnsweredDao qad);
  
}