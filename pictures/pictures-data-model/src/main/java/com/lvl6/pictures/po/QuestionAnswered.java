package com.lvl6.pictures.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
public class QuestionAnswered extends BasePersistentObject {

	@NotNull
	@Column(name = "round_number")
	@Index(name="question_answered_round_number_index")
	protected int roundNumber;

	
	@NotNull
	@Index(name="question_answered_question_number_index")
	protected int questionNumber;
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "answered_date")
	@NotNull
	@Index(name="question_answered_answer_date_index")
	protected Date answeredDate;
	
	
	@NotNull
	@Index(name="question_answered_answered_by_index")
	protected String answeredByUser;
	
	
	@NotNull
	@Index(name="question_answered_question_id_index")
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
