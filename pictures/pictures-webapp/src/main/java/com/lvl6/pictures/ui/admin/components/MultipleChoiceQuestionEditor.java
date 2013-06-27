package com.lvl6.pictures.ui.admin.components;


import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.pictures.dao.MultipleChoiceQuestionDao;
import com.lvl6.pictures.po.MultipleChoiceAnswer;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.ui.admin.QuestionEditorPage;
import com.lvl6.spring.AppContext;

public class MultipleChoiceQuestionEditor extends Panel implements QuestionEditor<MultipleChoiceQuestion> {

	
	
	private static final Logger log = LoggerFactory.getLogger(MultipleChoiceQuestionEditor.class);
	
	public MultipleChoiceQuestionEditor(String id, QuestionBase qb) {
		super(id);
		log.info("Editing question: \n{}", qb);
		setQuestion((MultipleChoiceQuestion) qb);
		setupForm();
	}

	private static final long serialVersionUID = 1L;
	
	MultipleChoiceQuestion question = new MultipleChoiceQuestion();
	CheckBox cb1 = new CheckBox("cb1", new Model<Boolean>(false));
	CheckBox cb2 = new CheckBox("cb2", new Model<Boolean>(false));
	CheckBox cb3 = new CheckBox("cb3", new Model<Boolean>(false));
	CheckBox cb4 = new CheckBox("cb4", new Model<Boolean>(false));
	
	HiddenField<String> h1 = new HiddenField<String>("h1", new Model<String>(""));
	HiddenField<String> h2 = new HiddenField<String>("h2", new Model<String>(""));
	HiddenField<String> h3 = new HiddenField<String>("h3", new Model<String>(""));
	HiddenField<String> h4 = new HiddenField<String>("h4", new Model<String>(""));
	
	
	List<CheckBox> cbs = Arrays.asList(cb1, cb2, cb3, cb4);
	
	List<HiddenField<String>> hfs = Arrays.asList(h1, h2, h3, h4);
	
	
	TextField<String> answer1 = new TextField<String>("answer1", new Model<String>(""));
	TextField<String> answer2 = new TextField<String>("answer2", new Model<String>(""));
	TextField<String> answer3 = new TextField<String>("answer3", new Model<String>(""));
	TextField<String> answer4 = new TextField<String>("answer4", new Model<String>(""));
	
	List<TextField<String>> answers = Arrays.asList(answer1, answer2, answer3, answer4);
	
	TextArea<String> mcQuestionText = new TextArea<>("mcQuestionText", new Model<String>(""));
	
	
	protected void setupForm() {
		if(isNew()) {
			log.info("Adding new multiple choice question");
		}else {
			log.info("Editing multiple choice question {}", question.getId());
		}
		int index = 0;
		if(!question.getQuestion().equals("")) {
			mcQuestionText.setDefaultModel(new Model<String>(question.getQuestion()));
		}
		if(question.getAnswers() != null) {
		for(MultipleChoiceAnswer ans : question.getAnswers()) {
			TextField<String> tf = answers.get(index);
			CheckBox cb = cbs.get(index);
			HiddenField<String> h = hfs.get(index);
			tf.setDefaultModel(new Model<String>(ans.getAnswer()));
			cb.setDefaultModel(new Model<Boolean>(ans.isCorrect()));
			h.setModel(new Model<String>(ans.getId()));
			index++;
		}}
		form.add(new FeedbackPanel("feedbackPanel"));
		form.add(cb1);
		form.add(cb2);
		form.add(cb3);
		form.add(cb4);
		form.add(answer1);
		form.add(answer2);
		form.add(answer3);
		form.add(answer4);
		form.add(h1);
		form.add(h2);
		form.add(h3);
		form.add(h4);
		form.add(mcQuestionText);
		add(form);
	}











	private boolean isNew() {
		return question.getId() == null || question.getId().equals("");
	}
	
	
	Form<MultipleChoiceQuestion> form = new Form<MultipleChoiceQuestion>("mcAnswersForm") {
		private static final long serialVersionUID = -2476376077544895775L;
		
		@Override
		protected void onSubmit() {
			int index = 0;
			for(TextField<String> tf : answers) {
				MultipleChoiceAnswer ans;
				if(isNew()) {
					ans = new MultipleChoiceAnswer();
					question.getAnswers().add(ans);
					log.info("Creating new answer");
				}else {
					ans = getAnswer(hfs.get(index).getModelObject());
					log.info("Updating answer");
				}
				ans.setAnswer(tf.getModelObject());
				ans.setCorrect(cbs.get(index).getModelObject());
				log.info("Set answer isCorrect {} : {}", ans.isCorrect(), ans.getAnswer());
				index++;
			}
			question.setQuestion(mcQuestionText.getModelObject());
			log.info("Saving question: \n{}", question);
			question = AppContext.getApplicationContext().getBean(MultipleChoiceQuestionDao.class).save(question);
			PageParameters params = new PageParameters();
			params.add("q", question.getId());
			setResponsePage(QuestionEditorPage.class, params);
		}
		
	};
	
	
	
	protected MultipleChoiceAnswer getAnswer(String id) {
		for(MultipleChoiceAnswer mcqa : question.getAnswers()) {
			if(mcqa.getId().equals(id)) {
				return mcqa;
			}
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public MultipleChoiceQuestion getQuestion() {
		return question;
	}

	@Override
	public void setQuestion(MultipleChoiceQuestion question) {
		this.question = question;
	}
	

}
