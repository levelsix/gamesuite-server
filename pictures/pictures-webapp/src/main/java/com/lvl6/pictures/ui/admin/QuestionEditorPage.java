package com.lvl6.pictures.ui.admin;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvl6.pictures.dao.QuestionBaseDao;
import com.lvl6.pictures.po.MultipleChoiceQuestion;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.ui.admin.components.MultipleChoiceQuestionEditor;
import com.lvl6.pictures.ui.admin.components.MultipleChoiceQuestionViewer;
import com.lvl6.spring.AppContext;


public class QuestionEditorPage extends TemplatePage {

	private static final long serialVersionUID = -1728365297134290240L;
	
	private static final Logger log = LoggerFactory.getLogger(QuestionEditorPage.class);
	

	public QuestionEditorPage() {
		super();
		QuestionBase q = getQuestion();
		setupQuestionList();
		setupQuestionViewer(q);
		setupQuestionEditor(q);
	}
	
	
	protected QuestionBase getQuestion() {
		QuestionBaseDao qbDao = AppContext.getApplicationContext().getBean(QuestionBaseDao.class);
		String question = getPageParameters().get("q").toOptionalString();
		QuestionBase q;
		log.info("Loading question {}, {}", getPageParameters().get("q"), question);
		if(question != null && !question.equals("")) {
			 q = qbDao.findOne(question);
		}else {
			q = new MultipleChoiceQuestion();
		}
		return q;
	}
	
	protected void setupQuestionList() {
		QuestionBaseDao qbDao = AppContext.getApplicationContext().getBean(QuestionBaseDao.class);
		ListView<QuestionBase> listview = new ListView<QuestionBase>("questionsList", qbDao.findAll()) {
			private static final long serialVersionUID = 1L;
			protected void populateItem(ListItem<QuestionBase> item) {
				PageParameters params = new PageParameters();
				params.add("q", item.getModel().getObject().getId());
				Link<QuestionEditorPage> qLink = new BookmarkablePageLink<QuestionEditorPage>("questionLink", QuestionEditorPage.class, params);
				Label qt = new Label("questionText", item.getModel().getObject().getId());
		        item.add(qLink);
		        qLink.add(qt);
		    }
		};
		add(listview);
	}
	
	protected void setupQuestionViewer(QuestionBase qb) {
		MultipleChoiceQuestionViewer qv = new MultipleChoiceQuestionViewer("questionViewer");
		add(qv);
	}
	
	protected void setupQuestionEditor(QuestionBase qb) {
		MultipleChoiceQuestionEditor qe = new MultipleChoiceQuestionEditor("questionEditor", qb);
		add(qe);
	}
	
	
	

}
