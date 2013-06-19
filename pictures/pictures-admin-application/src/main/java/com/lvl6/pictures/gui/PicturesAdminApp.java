package com.lvl6.pictures.gui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;
import java.awt.Color;
import java.awt.Font;

import org.apache.pivot.wtk.HorizontalAlignment;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.VerticalAlignment;

public class PicturesAdminApp implements Application {

	
	protected Window window = null;
	
	
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
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
        window = (Window)bxmlSerializer.readObject(PicturesAdminApp.class, "PicturesAdminApp.bxml");
        window.open(display);
		
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

}
