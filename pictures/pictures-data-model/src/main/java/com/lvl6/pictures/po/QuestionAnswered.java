package com.lvl6.pictures.po;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Range;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
public class QuestionAnswered extends BasePersistentObject {

	@NotNull
	@Column(name = "round_number")
	@Index(name="question_answered_round_number_index")
	protected int roundNumber;

	//the nth question user saw
	@NotNull
	@Index(name="question_answered_question_number_index")
	protected int questionNumber;
	
	//for helping to select questions the user will see next (questions user hasn't seen recently)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "answered_date")
	@NotNull
	@Index(name="question_answered_answer_date_index")
	protected Date answeredDate;
	
	
	@NotNull
	@Index(name="question_answered_answered_by_index")
	protected String answeredByUser;
	
	
	@NotNull
	//@Index(name="question_answered_question_index")
	@ManyToOne
	protected QuestionBase question;
	
	
	@Range(min=1, max= 3)
	protected int answerType;

	
	public Set<String> getPictureNames() {
	  return question.getPictureNames();
	}
	

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


	public QuestionBase getQuestion() {
		return question;
	}


	public void setQuestion(QuestionBase question) {
		this.question = question;
	}


	public int getAnswerType() {
    return answerType;
  }


  public void setAnswerType(int answerType) {
    this.answerType = answerType;
  }


  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
	
}
