package com.lvl6.pictures.gui;

import java.net.URL;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.ListView;
import org.apache.pivot.wtk.Window;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lvl6.pictures.dao.MultipleChoiceQuestionDao;

public class PicturesAdminApp implements Application, Bindable {

	
	protected Window window = null;
	
	@BXML 
	ListView questionsList;
	
	ApplicationContext spring;
	
	MultipleChoiceQuestionDao mcqDao;
	
	
	public void resume() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public boolean shutdown(boolean arg0) throws Exception {
		if (window != null) {
            window.close();
        }
		return false;
	}

	public void startup(Display display, Map<String, String> props) throws Exception {
		spring = new ClassPathXmlApplicationContext("spring-application-context.xml");
		mcqDao = spring.getBean(MultipleChoiceQuestionDao.class);
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
        window = (Window)bxmlSerializer.readObject(PicturesAdminApp.class, "PicturesAdminApp.bxml");
        window.open(display);
		//questionsList.setListData((List<?>) mcqDao.findAll());
/*		window = new Window();

        Label label = new Label();
        label.setText("Hello World!");
        label.getStyles().put("font", new Font("Arial", Font.BOLD, 24));
        label.getStyles().put("color", Color.RED);
        label.getStyles().put("horizontalAlignment",   HorizontalAlignment.CENTER);
        label.getStyles().put("verticalAlignment",
            VerticalAlignment.CENTER);

        window.setContent(label);
        window.setTitle("Hello World!");
        window.setMaximized(true);

        window.open(display);*/
	}

	public void suspend() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
	    DesktopApplicationContext.main(PicturesAdminApp.class, args);
	}

	@Override
	public void initialize(Map<String, Object> arg0, URL arg1, Resources arg2) {
		// TODO Auto-generated method stub
		
	}

}
