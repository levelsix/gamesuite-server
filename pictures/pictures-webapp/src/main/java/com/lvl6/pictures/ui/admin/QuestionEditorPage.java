package com.lvl6.pictures.ui.admin;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.lvl6.pictures.dao.QuestionBaseDao;
import com.lvl6.pictures.po.QuestionBase;
import com.lvl6.pictures.ui.admin.components.MultipleChoiceQuestionEditor;
import com.lvl6.pictures.ui.admin.components.MultipleChoiceQuestionViewer;
import com.lvl6.spring.AppContext;


public class QuestionEditorPage extends TemplatePage {

	private static final long serialVersionUID = -1728365297134290240L;
	
	protected QuestionBaseDao qbDao;

	public QuestionEditorPage() {
		super();
		qbDao = AppContext.getApplicationContext().getBean(QuestionBaseDao.class);
		setupQuestionList();
		setupQuestionViewer();
		setupQuestionEditor();
	}
	
	protected void setupQuestionList() {
		ListView<QuestionBase> listview = new ListView<QuestionBase>("questionsList", qbDao.findAll()) {
			private static final long serialVersionUID = 1L;
			protected void populateItem(ListItem<QuestionBase> item) {
		        item.add(new Label("label", item.getModel()));
		    }
		};
		add(listview);
	}
	
	protected void setupQuestionViewer() {
		MultipleChoiceQuestionViewer qv = new MultipleChoiceQuestionViewer("questionViewer");
		add(qv);
	}
	
	protected void setupQuestionEditor() {
		MultipleChoiceQuestionEditor qe = new MultipleChoiceQuestionEditor("questionEditor");
		add(qe);
	}
	
	
	

}
