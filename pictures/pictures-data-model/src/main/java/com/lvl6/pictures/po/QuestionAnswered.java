package com.lvl6.pictures.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
public class QuestionAnswered extends BasePersistentObject {

	@NotNull
	@Column(name = "round_number")
	protected int roundNumber;

	
	@NotNull
	protected int questionNumber;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "answered_date")
	@NotNull
	protected Date answeredDate;
	
	
	@NotNull
	protected String answeredByUser;
	
	
	@NotNull
	protected String questionId;
	
	
	@NotNull
	protected boolean correct;


	public int getRoundNumber() {
		return roundNumber;
	}


	public void setRoundNumber(int roundNumber) {
		this.roundNumber = roundNumber;
	}


	public int getQuestionNumber() {
		return questionNumber;
	}


	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}


	public Date getAnsweredDate() {
		return answeredDate;
	}


	public void setAnsweredDate(Date answeredDate) {
		this.answeredDate = answeredDate;
	}


	public String getAnsweredByUser() {
		return answeredByUser;
	}


	public void setAnsweredByUser(String answeredByUser) {
		this.answeredByUser = answeredByUser;
	}


	public String getQuestionId() {
		return questionId;
	}


	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}


	public boolean isCorrect() {
		return correct;
	}


	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
	
	
}
