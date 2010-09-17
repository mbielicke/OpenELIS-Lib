/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget;

import java.util.HashMap;

import org.openelis.gwt.screen.Screen;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * WindowBrowser will display Screen widgets in draggable Windows
 * in a certain portion of the screen.  It uses the ScreenWindow
 * widget to wrap the contents of a screen.  It also control the 
 * z-index of the windows displayed.
 * 
 * @author tschmidt
 *
 */
public class WindowBrowser extends Composite {// implements HasKeyPressHandlers, KeyPressHandler {
    
    /*
     * The main panel used to contain and display windows
     */
    protected AbsolutePanel browser;
    
    /*
     * Hash of all currently displayed windows
     */
    protected HashMap<String,ScreenWindow> windows;
    
    /*
     * Integers for current zIndex and limit of windows shown
     */
    protected int index,limit ;
    
    /*
     * Drag and drop controllers
     */
    protected PickupDragController dragController;
    protected AbsolutePositionDropController dropController; 
    
    /*
     * Reference to the currently focused window
     */
    protected FocusPanel focusedWindow;
    
    
    /**
     * Constructor that takes an arguments if the browser should auto-size to the window,
     * and the number of allowed screens to show at once.
     * @param size
     * @param limit
     */
    public WindowBrowser(boolean size, int limit) {
        browser = new AbsolutePanel();
        windows = new HashMap<String,ScreenWindow>();
        
        dragController = new PickupDragController(browser,true);
        
        dropController = new AbsolutePositionDropController(browser) {
            @Override
            public void onDrop(DragContext context) {
                // TODO Auto-generated method stub
                super.onDrop(context);
                ((ScreenWindow)context.draggable).positionGlass();
            }
        };
        
        this.limit = limit;
        initWidget(browser);
        
        dragController.setBehaviorDragProxy(true);
        dragController.registerDropController(dropController);
        DOM.setStyleAttribute(browser.getElement(),
                              "overflow",
                              "auto");
        if(size){
            Window.addResizeHandler(new ResizeHandler() {

				public void onResize(ResizeEvent event) {
					 resize();
					
				}

            });
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    resize();
                }
            });
        }
    } 
    
    /**
     * Adds the Screen to the window browser
     * @param screen
     */
    public void addScreen(Screen screen) {
    	addScreen(screen,null);
    }
    
    /**
     * Adds a screen to the borwser and indexing it by the key passed
     * @param screen
     * @param key
     */
    public void addScreen(Screen screen, String key) {
        /*
         * If key passed as NULL us the fully qualifed class name of the Screen.
         */
        if(key == null)
           key = screen.getClass().getName();
        
        if(windows.size() == limit){
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        
        /*
         * If Screen is already being shown bring the Screen to focus and exit.
         */
        if (windows.containsKey(key)) {
            selectScreen(key);
            return;
        }
        
        /*
         * Sets cursor to loading
         */
        RootPanel.get().addStyleName("ScreenLoad");
     
        /*
         * Create a ScreenWindow and add the passed Screen to it to be added to the
         * browser.
         */
        ScreenWindow window = new ScreenWindow(this, key);
        window.setContent(screen);
        screen.setWindow(window);
        addWindow(window,key);
    }
    
    /**
     * Adds a ScreenWindow directly to the browser indexing it by the passed key
     * @param window
     * @param key
     */
    public void addWindow(ScreenWindow window, String key) {
    	index++;
    	browser.add(window,(windows.size()*25),(windows.size()*25));
    	windows.put(key, window);
    	setFocusedWindow();
    }
    
    /**
     * Brings the window indexed by the passed key to be the focusedWindow
     * @param key
     * @return
     */
    public boolean selectScreen(String key) {
    	if (windows.containsKey(key)) {
    		ScreenWindow wid = windows.get(key);
    		if(index != wid.zIndex){
    			index++;
    			wid.zIndex = index;
    			int top = browser.getWidgetTop(wid);
    			int left = browser.getWidgetLeft(wid);
    			if(wid.content instanceof ReportFrame){
    				//browser.setWidgetPosition(wid, left, top);
    				DOM.setStyleAttribute(wid.getElement(), "zIndex", String.valueOf(index));
    				DOM.setStyleAttribute(wid.content.getElement(), "zIndex", String.valueOf(index));
    				
    			}else
    				browser.add(wid, left, top);
    			setFocusedWindow();
    		}
    		return true;
    	}
    	return false;
    }
    
    /**
     * This method will size the browser to the the size of the window containing it. 
     */
    public void resize() {
        if (browser.isVisible()) {
            browser.setHeight((Window.getClientHeight() - browser.getAbsoluteTop()) + "px");
            browser.setWidth((Window.getClientWidth() - browser.getAbsoluteLeft())+ "px");
        }
    }
    
    /**
     * This method will make sure that the window with the largest index is brought to the 
     * top and focused.
     */
    public void setFocusedWindow() {
    	for(ScreenWindow wid : windows.values()) {
    		if(wid.zIndex != index){
    			if(wid.getStyleName().indexOf("unfocused") < 0){	
    				wid.addStyleName("unfocused");
    			}
    		}else{
    			wid.removeStyleName("unfocused");
    			focusedWindow = wid;
    		}
    	}
    }

    /*
	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return addDomHandler(handler,KeyPressEvent.getType());
	}

	public void onKeyPress(KeyPressEvent event) {
		KeyPressEvent.fireNativeEvent(event.getNativeEvent(), focusedWindow);
		
	}
	*/
}
