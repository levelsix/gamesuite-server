package com.lvl6.pictures.ui.admin.components;

import com.lvl6.pictures.po.MultipleChoiceQuestion;

public class MultipleChoiceQuestionEditor implements QuestionEditor<MultipleChoiceQuestion> {

	MultipleChoiceQuestion question;
	
	@Override
	public MultipleChoiceQuestion getQuestion() {
		return question;
	}

	@Override
	public void setQuestion(MultipleChoiceQuestion question) {
		this.question = question;
	}

}
