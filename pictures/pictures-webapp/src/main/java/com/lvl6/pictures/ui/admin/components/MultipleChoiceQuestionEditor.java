package com.lvl6.pictures.ui.admin.components;

import org.apache.wicket.markup.html.panel.Panel;

import com.lvl6.pictures.po.MultipleChoiceQuestion;

public class MultipleChoiceQuestionEditor extends Panel implements QuestionEditor<MultipleChoiceQuestion> {

	public MultipleChoiceQuestionEditor(String id) {
		super(id);
	}

	private static final long serialVersionUID = 1L;
	
	
	
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
