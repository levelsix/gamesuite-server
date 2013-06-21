package com.lvl6.pictures.ui.admin;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class PicturesAdmin extends WebApplication {

	

	
	
	@Override
	protected void init() {
		super.init();
		mountPage("/questions", QuestionEditorPage.class);
		mountPage("/admin", AdminPage.class);
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return MainPage.class;
	}
	
	

}
