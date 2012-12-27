package org.openelis.gwt.screen;

import java.util.HashMap;

import com.google.gwt.user.client.ui.Widget;

public interface ScreenDefInt {
	
	public void setWidgets(HashMap<String,Widget> widgets);
	
	public HashMap<String,Widget> getWidgets();
	
	public Widget getWidget(String key);
	
	public void setWidget(Widget widget, String key);
	
	public ScreenPanel getPanel();
	
	public String getName();
	
	public void setName(String name);

}
