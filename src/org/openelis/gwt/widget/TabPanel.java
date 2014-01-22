package org.openelis.gwt.widget;

import java.util.ArrayList;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabPanel extends com.google.gwt.user.client.ui.TabPanel { 
	private ArrayList<String> keyTabList = new  ArrayList<String>();
	private String width;	
	private String height;
	private TabBar bar;
	private TabBarScroller barScroller;
	
	
	public TabPanel() {
		super();
		VerticalPanel panel = ((VerticalPanel)getWidget());
		bar = (TabBar)panel.getWidget(0);
		Widget deck = panel.getWidget(1);
		barScroller = new TabBarScroller(bar);
		panel.clear();
		panel.add(barScroller);
		panel.add(deck);
		
	}
	
	
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
		//if(isAttached()) {
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					barScroller.checkScroll();
				}
			});
		//}
		
	
	}
	
	@Override
	public boolean remove(int index) {
	    boolean ret;
	    ret = super.remove(index);
	    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            
            @Override
            public void execute() {
                checkScroll();
            }
        });
	   
	    return ret;
	}
	
	@Override
	public boolean remove(Widget widget) {
	    boolean ret;
	    ret = super.remove(widget);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            
            @Override
            public void execute() {
                checkScroll();
            }
        });
	    return ret;
	}
	
	@Override
	public void clear() {
	    super.clear();
	    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
	            
	         @Override
	         public void execute() {
	             checkScroll();
	         }
	    });
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
		//super.setWidth(width);
	}
	
	@Override
	public void setHeight(String height) {
		this.height = height;
		//super.setHeight(height);
	}
	
	public void setTabVisible(int index, boolean visible) {
		((Widget)getTabBar().getTab(index)).setVisible(visible);
		//if(isAttached()) {
			DeferredCommand.addCommand(new Command() {
				public void execute() {
					barScroller.checkScroll();
				}
			});
		//}
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		DeferredCommand.addCommand(new Command() {
			public void execute() {
				barScroller.checkScroll();
			}
		});
	}
	
	public void checkScroll() {
		barScroller.checkScroll();
	}
	

	

}
