package com.lvl6.pictures.po;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class RoundHistory extends BasePersistentObject {

	
	@NotNull
	@Index(name = "round_number_index")
	protected int roundNumber;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Index(name = "round_started_index")
	protected Date roundStarted;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Index(name = "round_ended_index")
	protected Date roundEnded;
	
	
	//Fetching lazily because fetching GameHistory fetches 
	//RoundHistory and some might be completed, in which case
	//score and correctAnswers will suffice for QuestionAnswered
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)	
	protected Set<QuestionAnswered> questionsAnswered;


	//to prevent recalculating scores from individual QuestionAnswered
	//everytime RoundHistory is fetched
	protected int score;
	

	//to prevent recalculating correct answers from individual
	//QuestionAnswered everytime RoundHistory is fetched
	protected int correctAnswers;
  
	@NotNull
	@Index(name = "user_id_index")
	protected String userId;
	
	

//	public List<PicturesQuestionWithTextAnswer> getPicturesQuestionWithTextAnswer() {
//	  List<PicturesQuestionWithTextAnswer> pqwtaList =
//	      
//	}


  public int getRoundNumber() {
		return roundNumber;
	}


	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}


	public Date getRoundStarted() {
		return roundStarted;
	}


	public void setRoundStarted(Date roundStarted) {
		this.roundStarted = roundStarted;
	}


	public Date getRoundEnded() {
		return roundEnded;
	}


	public void setRoundEnded(Date roundEnded) {
		this.roundEnded = roundEnded;
	}


	public Set<QuestionAnswered> getQuestionsAnswered() {
		return questionsAnswered;
	}


	public void setQuestionsAnswered(Set<QuestionAnswered> questionsAnswered) {
		this.questionsAnswered = questionsAnswered;
	}


	public int getScore() {
	  return score;
	}


	public void setScore(int score) {
	  this.score = score;
	}


	public int getCorrectAnswers() {
	  return correctAnswers;
	}


	public void setCorrectAnswers(int correctAnswers) {
	  this.correctAnswers = correctAnswers;
	}
  

  public String getUserId() {
    return userId;
  }


  public void setUserId(String userId) {
    this.userId = userId;
  }

}
