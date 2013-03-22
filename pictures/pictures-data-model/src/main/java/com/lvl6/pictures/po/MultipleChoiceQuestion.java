package com.lvl6.pictures.po;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Table( name = "multiple_choice_question" )
public class MultipleChoiceQuestion extends QuestionBase {
	
	@NotNull
	protected String question;
	
	@OneToMany	
	@Size(min=2, max=6)
	protected Set<MultipleChoiceAnswer> answers;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Set<MultipleChoiceAnswer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<MultipleChoiceAnswer> answers) {
		this.answers = answers;
	}
	
	
}
