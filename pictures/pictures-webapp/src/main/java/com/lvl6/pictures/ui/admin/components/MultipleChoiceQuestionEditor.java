package com.lvl6.pictures.ui.admin.components;


import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.pictures.po.MultipleChoiceQuestion;

public class MultipleChoiceQuestionEditor extends Panel implements QuestionEditor<MultipleChoiceQuestion> {

	
	
	private static final Logger log = LoggerFactory.getLogger(MultipleChoiceQuestionEditor.class);
	
	public MultipleChoiceQuestionEditor(String id) {
		super(id);
		setupForm();
	}

	private static final long serialVersionUID = 1L;
	
	MultipleChoiceQuestion question = new MultipleChoiceQuestion();
	CheckBox cb1 = new CheckBox("cb1");
	CheckBox cb2 = new CheckBox("cb2");
	CheckBox cb3 = new CheckBox("cb3");
	CheckBox cb4 = new CheckBox("cb4");
	
	TextField<String> answer1 = new TextField<String>("answer1");
	TextField<String> answer2 = new TextField<String>("answer2");
	TextField<String> answer3 = new TextField<String>("answer3");
	TextField<String> answer4 = new TextField<String>("answer4");
	
	TextArea<String> mcQuestionText = new TextArea<>("mcQuestionText");
	
	protected void setupForm() {
		form.add(new FeedbackPanel("feedbackPanel"));
		form.add(cb1);
		form.add(cb2);
		form.add(cb3);
		form.add(cb4);
		form.add(answer1);
		form.add(answer2);
		form.add(answer3);
		form.add(answer4);
		form.add(mcQuestionText);
		add(form);
	}
	
	
	Form<MultipleChoiceQuestion> form = new Form<MultipleChoiceQuestion>("mcAnswersForm") {
		private static final long serialVersionUID = -2476376077544895775L;
		
		@Override
		protected void onSubmit() {
			
		}
		
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public MultipleChoiceQuestion getQuestion() {
		return question;
	}

	@Override
	public void setQuestion(MultipleChoiceQuestion question) {
		this.question = question;
	}
	

}
