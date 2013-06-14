package com.lvl6.pictures.services.roundhistory;

import java.util.Date;
import java.util.Set;

import com.lvl6.pictures.dao.RoundHistoryDao;
import com.lvl6.pictures.po.QuestionAnswered;
import com.lvl6.pictures.po.RoundHistory;

public interface RoundHistoryService {
  
  public abstract RoundHistory createRoundHistory(String userId,
      int roundNumber, Date roundStarted, Date roundEnded,
      Set<QuestionAnswered> questionsAnswered, int score);
  
  
  public abstract RoundHistoryDao getRoundHistoryDao();
  
  public abstract void setRoundHistoryDao(RoundHistoryDao roundHistoryDao);
}