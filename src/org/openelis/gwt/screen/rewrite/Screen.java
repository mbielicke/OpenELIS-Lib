package org.openelis.gwt.screen.rewrite;

import org.openelis.gwt.screen.ScreenWindow;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;

public class Screen extends Composite {
	
	public final AbsolutePanel panel = new AbsolutePanel();
    public String name;
    public enum State {DEFAULT,DISPLAY,UPDATE,ADD,QUERY,BROWSE,DELETE};
    public enum Action {NEW_MODEL,REFRESH_PAGE,NEW_PAGE,LOAD,UNLOAD,SUBMIT_QUERY};
    public State state = State.DEFAULT;
    public ScreenDef def;
    public ScreenWindow window;
    
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
