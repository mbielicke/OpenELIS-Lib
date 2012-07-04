package org.openelis.gwt.widget;

import java.util.ArrayList;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabLayout extends com.google.gwt.user.client.ui.TabLayoutPanel { 
	private ArrayList<String> keyTabList = new  ArrayList<String>();
	private String width;	
	private String height;
	private TabBar bar;
	private TabBarScroller barScroller;
	
	
	public TabLayout(double height) {
		super(height,Unit.PX);
		/*
		VerticalPanel panel = ((VerticalPanel)getWidget());
		bar = (TabBar)panel.getWidget(0);
		Widget deck = panel.getWidget(1);
		barScroller = new TabBarScroller(bar);
		panel.clear();
		panel.add(barScroller);
		panel.add(deck);
		*/
		
	}

	public void add(Widget wid, String text, String tab) {
		keyTabList.add(tab);
		add(wid,text);
	}
	
	@Override
	public void add(Widget wid, final String tabText) {
		final ScrollPanel scroll = new ScrollPanel();
		scroll.setWidget(wid);
        super.add(scroll, tabText);
		scroll.setWidth(width);
        scroll.setHeight(height);
	}
	
	public String getNextTabWidget() {
		return keyTabList.get(getSelectedIndex()).split(",")[0];
	}
	
	public String getPrevTabWidget() {
		return keyTabList.get(getSelectedIndex()).split(",")[1];
	}
	
	@Override
	public void setWidth(String width) {
		this.width = width;
		//barScroller.setWidth(width);
		super.setWidth(width);
	}
	
	@Override
	public void setHeight(String height) {
		this.height = height;
		super.setHeight(height);
	}
	
	public void setTabVisible(int index, boolean visible) {
		getTabWidget(index).setVisible(visible);
		//((Widget)getTabBar().getTab(index)).setVisible(visible);
		if(isAttached()) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				public void execute() {
					//barScroller.checkScroll();
				}
			});
		}
	}
	
	public void checkScroll() {
		//barScroller.checkScroll();
	}

}
