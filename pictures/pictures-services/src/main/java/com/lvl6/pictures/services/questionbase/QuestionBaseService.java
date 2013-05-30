package com.lvl6.pictures.services.questionbase;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.services.utils.RandomNumberUtils;

public interface QuestionBaseService {
  
  public abstract List<QuestionBase> getRandomQuestions(int amount,
      Set<String> allPictureNames);
  
  public abstract Map<String, QuestionBase> getQuestionIdsToQuestions();

  public abstract void setQuestionIdsToQuestions(
      Map<String, QuestionBase> questionIdsToQuestions);

  public RandomNumberUtils getRandNumUtils();

  public void setRandNumUtils(RandomNumberUtils randNumUtils);

}