package com.lvl6.pictures.ui.admin;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.request.mapper.parameter.PageParameters;

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
				PageParameters params = new PageParameters();
				Link<QuestionEditorPage> qLink = new BookmarkablePageLink<QuestionEditorPage>("questionLink", QuestionEditorPage.class, params);
				Label qt = new Label("questionText", item.getModel().getObject().getId());
		        item.add(qLink);
		        qLink.add(qt);
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
