package com.lvl6.pictures.po;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
public class RoundPendingCompletion extends BasePersistentObject {
  
    private static final long serialVersionUID = 4947214752070572503L;

@NotNull
  int roundNumber;

  //The questions that will be displayed in this round
  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) 
  protected Set<QuestionBase> questions;
  
  @NotNull
  String userId;

  @Temporal(TemporalType.TIMESTAMP)
  @NotNull
  @Index(name="started_round_date_index")
  protected Date startDate = new Date();
  
  
  
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

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  
}