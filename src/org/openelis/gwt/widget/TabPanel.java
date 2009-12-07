package org.openelis.gwt.widget;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.Widget;

public class TabPanel extends com.google.gwt.user.client.ui.TabPanel {
	
	private ArrayList<String> keyTabList = new  ArrayList<String>();
	
	public void add(Widget wid, String text, String tab) {
		keyTabList.add(tab);
		add(wid,text);
	}
	
	public String getNextTabWidget() {
		return keyTabList.get(getTabBar().getSelectedTab()).split(",")[0];
	}
	
	public String getPrevTabWidget() {
		return keyTabList.get(getTabBar().getSelectedTab()).split(",")[1];
	}

}
