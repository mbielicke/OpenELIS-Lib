package org.openelis.gwt.widget;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabPanel extends com.google.gwt.user.client.ui.TabPanel {
	private ArrayList<String> keyTabList = new  ArrayList<String>();
	private String width;	
	private String height;
	
	public void add(Widget wid, String text, String tab) {
		keyTabList.add(tab);
		add(wid,text);
	}
	
	@Override
	public void add(Widget wid, String tabText) {
		ScrollPanel scroll = new ScrollPanel();
		scroll.setWidget(wid);
		super.add(scroll, tabText);
		scroll.setWidth(width);
		scroll.setHeight(height);
	
	}
	
	public String getNextTabWidget() {
		return keyTabList.get(getTabBar().getSelectedTab()).split(",")[0];
	}
	
	public String getPrevTabWidget() {
		return keyTabList.get(getTabBar().getSelectedTab()).split(",")[1];
	}
	
	@Override
	public void setWidth(String width) {
		this.width = width;
		//super.setWidth(width);
	}
	
	@Override
	public void setHeight(String height) {
		this.height = height;
		//super.setHeight(height);
	}
	
	public void setTabVisible(int index, boolean visible) {
		((Widget)getTabBar().getTab(index)).setVisible(visible);
	}

}
