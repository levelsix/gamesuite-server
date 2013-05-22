package com.lvl6.pictures.po;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
public class RoundPendingCompletion extends BasePersistentObject {
  
  @NotNull
  int roundNumber;

  //The questions that will be displayed in this round
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) 
  protected Set<QuestionBase> questions;
  
  @NotNull
  String userId;

  
  
  public int getRoundNumber() {
    return roundNumber;
  }

  public void setRoundNumber(int roundNumber) {
    this.roundNumber = roundNumber;
  }

  public Set<QuestionBase> getQuestions() {
    return questions;
  }

  public void setQuestions(Set<QuestionBase> questions) {
    this.questions = questions;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
  
}