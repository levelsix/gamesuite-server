package com.lvl6.pictures.po;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.lvl6.gamesuite.common.po.BasePersistentObject;


@Entity
@Table( name = "multiple_choice_answer" )
public class MultipleChoiceAnswer extends BasePersistentObject {
	
	protected boolean isCorrect;
	
	@NotNull
	protected String answer;

	
	@NotNull
	@Enumerated(EnumType.STRING)
	protected AnswerType answerType;
	
	
	
	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
