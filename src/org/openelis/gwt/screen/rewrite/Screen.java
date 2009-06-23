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
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public class Screen extends Composite implements HasStateChangeHandlers<Screen.State>, HasDataChangeHandlers, HasActionHandlers<Screen.Action> {
	
	public final AbsolutePanel panel = new AbsolutePanel();
    public String name;
    public enum State {DEFAULT,DISPLAY,UPDATE,ADD,QUERY,BROWSE,DELETE};
    public enum Action {NEW_MODEL,REFRESH_PAGE,NEW_PAGE,LOAD,UNLOAD,SUBMIT_QUERY};
    public State state = State.DEFAULT;
    public ScreenDef def;
    public ScreenWindow window;
    public static HashMap<String,String> consts;
    
    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public Screen() {
        initWidget(panel);
    }
    
    public Screen(String url) {
    	initWidget(panel);
    	def = new ScreenDef();
    	def.loadURL = url;
    	UIUtil.createWidgets(def);
    	panel.add(def.panel);
    	afterDraw();
    }
    
    public Screen(ScreenDef def) {
    	initWidget(panel);
    	this.def = def;
    	panel.add(def.panel);
    	afterDraw();
    }
    
    public void afterDraw() {
    	
    }
    
    public void setDef(ScreenDef def) {
    	this.def = def;
    	panel.clear();
    	panel.add(def.panel);
    	afterDraw();
    }
    
    protected boolean validate() {
        return true;
    }
    
    protected void strikeThru(boolean enabled) {
        for(String key : def.widgets.keySet()) {
            if(enabled)
                def.widgets.get(key).addStyleName("strike");
            else
                def.widgets.get(key).removeStyleName("strike");
      }
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
    
    /**
     * This method is used to control the Tab order of input fields defined in
     * the form XML.  It is called when tab is pressed on widgets that have been 
     * defined to do so by overriding it's onBrowser(Event event) method during
     * declaration.  
     * 
     * @param event
     * @param wid
     
    
    public void doTab(Event event, ScreenWidget wid) {
        doTab(DOM.eventGetShiftKey(event),wid);
        DOM.eventCancelBubble(event, true);
        DOM.eventPreventDefault(event);
    }
    
    public void doTab(boolean shift, ScreenWidget wid) {
        
        ScreenWidget obj = null;
        if (shift)
            obj = widgets.get(tabBack.get(wid));
        else
            obj = widgets.get(tabOrder.get(wid));
        if (obj != null) {
            boolean tabbed = false;
            while (!tabbed) {
                if (obj.isVisible() && obj.isEnabled()) {
                    tabbed = true;
                    obj.setFocus(true);
                } else {
                    if (shift)
                        obj = widgets.get(tabBack.get(obj));
                    else
                        obj = widgets.get(tabOrder.get(obj));
                }
            }
        }
    }
    

    public void addTab(ScreenWidget on, String[] to) {
        tabOrder.put(on, to[0]);
        if (to.length > 1)
            tabBack.put(on, to[1]);
    }
    */
}
