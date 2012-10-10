package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.resources.TabPanelCSS;
import org.openelis.gwt.screen.ViewPanel;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabPanel extends com.google.gwt.user.client.ui.TabPanel  { 
	private ArrayList<String> keyTabList = new  ArrayList<String>();
	private ArrayList<Grid> tabWidgets = new ArrayList<Grid>();
	private String width;	
	private String height;
	private TabBar bar;
	private TabBarScroller barScroller;
	
	protected TabPanelCSS css;
	
	public TabPanel(ViewPanel viewPanel) {
		super();
		
		VerticalPanel panel = ((VerticalPanel)getWidget());
		bar = (TabBar)panel.getWidget(0);
		Widget deck = panel.getWidget(1);
		barScroller = new TabBarScroller(bar);
		panel.clear();
		panel.add(barScroller);
		panel.add(deck);
		
		viewPanel.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				setWidth((Integer.parseInt(width.replace("px",""))+event.getWidth())+"px");
				setHeight((Integer.parseInt(height.replace("px",""))+event.getHeight())+"px");
				for(int i = 0; i < getWidgetCount(); i++) {
					getWidget(i).setHeight(height);
					getWidget(i).setWidth(width);
				}
			}
		});
		
		setCSS(OpenELISResources.INSTANCE.tabpanel());
	}

	public void add(Widget wid, String text, String tab) {
		keyTabList.add(tab);
		add(wid,text);
	}
	
	@Override
	public void add(Widget wid, final String tabText) {
		Grid tabWidget;
		AbsolutePanel icon;
		
		tabWidget = new Grid(1,2);
		icon = new AbsolutePanel();
		tabWidget.setText(0, 0, tabText);
		tabWidget.setWidget(0,1,icon);
		tabWidget.getCellFormatter().setWordWrap(0, 0, false);
		
		
		tabWidgets.add(tabWidget);
		
		final ScrollPanel scroll = new ScrollPanel();
		scroll.setWidget(wid);
        super.add(scroll, tabWidget);
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
		barScroller.setWidth(width);
		super.setWidth(width);
	}
	
	@Override
	public void setHeight(String height) {
		this.height = height;
		super.setHeight(height);
	}
	
	public void setTabVisible(int index, boolean visible) {
		((Widget)getTabBar().getTab(index)).setVisible(visible);
		if(isAttached()) {
			Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				public void execute() {
					barScroller.checkScroll();
				}
			});
		}
	}
	
	public void checkScroll() {
		barScroller.checkScroll();
	}
	
	public void setTabInError(int index) {
		tabWidgets.get(index).getWidget(0, 1).setStyleName(css.TabError());
	}
	
	public void setTabHasData(int index) {
		tabWidgets.get(index).getWidget(0,1).setStyleName(css.TabData());
	}
	
	public void removeTabInError(int index) {
		tabWidgets.get(index).getWidget(0,1).removeStyleName(css.TabError());
	}
	
	public void remvoeTabHasData(int index) {
		tabWidgets.get(index).getWidget(0,1).removeStyleName(css.TabData());
	}
	
	public void setCSS(TabPanelCSS css) {
		css.ensureInjected();
		this.css = css;
		setStyleName(css.ScreenTab());
		
	}

}
