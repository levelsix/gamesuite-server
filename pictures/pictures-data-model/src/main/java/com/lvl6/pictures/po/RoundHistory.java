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
public class RoundHistory extends BasePersistentObject {

	
	@NotNull
	@Index(name = "round_number_index")
	protected int roundNumber;
	
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Index(name = "round_started_index")
	protected Date roundStarted;
	
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Index(name = "round_ended_index")
	protected Date roundEnded;
	
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)	
	protected Set<QuestionAnswered> questionsAnswered;


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
	
	
	
}
