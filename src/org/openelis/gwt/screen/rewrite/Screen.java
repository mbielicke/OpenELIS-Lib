package org.openelis.gwt.screen.rewrite;

import java.util.HashMap;

import org.openelis.gwt.event.ActionEvent;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.HasActionHandlers;
import org.openelis.gwt.event.HasDataChangeHandlers;
import org.openelis.gwt.event.HasStateChangeHandlers;
import org.openelis.gwt.event.StateChangeEvent;
import org.openelis.gwt.event.StateChangeHandler;
import org.openelis.gwt.screen.ScreenWindow;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class Screen extends Composite implements HasStateChangeHandlers<Screen.State>, HasDataChangeHandlers, HasActionHandlers<Screen.Action> {
	
	public final AbsolutePanel panel = new AbsolutePanel();
    public String name;
    public enum State {DEFAULT,DISPLAY,UPDATE,ADD,QUERY,BROWSE,DELETE};
    public enum Action {NEW_MODEL,REFRESH_PAGE,NEW_PAGE,LOAD,UNLOAD,SUBMIT_QUERY,SELECTION_FETCHED};
    public State state = State.DEFAULT;
    protected ScreenDef def;
    protected ScreenWindow window;
    public static HashMap<String,String> consts;
    
    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public Screen() {
        initWidget(panel);
    }
    
    public Screen(String url) throws Exception {
    	initWidget(panel);
    	def = new ScreenDef();
    	def.loadURL = url;
    	UIUtil.createWidgets(def);
    	panel.add(def.panel);
    }
    
    public Screen(ScreenDef def) {
    	initWidget(panel);
    	this.def = def;
    	panel.add(def.panel);
    }
    
    public void setWindow(ScreenWindow window) {
        this.window = window;
    }
    
    public ScreenDef getDefinition() {
        return def;
    }
    
    public ScreenWindow getWindow() {
        return window;
    }
    
        
    public void setDef(ScreenDef def) {
    	this.def = def;
    	panel.clear();
    	panel.add(def.panel);
    }
    
    public void addScreenHandler(Widget wid, ScreenEventHandler<?> screenHandler) {
    	addDataChangeHandler(screenHandler);
    	addStateChangeHandler(screenHandler);
    	if(wid instanceof HasValue)
    		((HasValue)wid).addValueChangeHandler(screenHandler);
    	if(wid instanceof HasClickHandlers) 
    		((HasClickHandlers)wid).addClickHandler(screenHandler);
    }

	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		return addHandler(handler, DataChangeEvent.getType());
	}

	public HandlerRegistration addStateChangeHandler(
			StateChangeHandler<org.openelis.gwt.screen.rewrite.Screen.State> handler) {
		return addHandler(handler, StateChangeEvent.getType());
	}

	public HandlerRegistration addActionHandler(
			ActionHandler<org.openelis.gwt.screen.rewrite.Screen.Action> handler) {
		return addHandler(handler,ActionEvent.getType());
	}
	
	public void removeFocus() {
		for(Widget wid : def.getWidgets().values()){
			if(wid instanceof Focusable) {
				((Focusable)wid).setFocus(false);
			}
		}
	}

}
