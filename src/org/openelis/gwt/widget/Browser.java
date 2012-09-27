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

import org.openelis.gwt.constants.Constants;
import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.resources.WindowCSS;
import org.openelis.gwt.resources.WindowNoImageCSS;
import org.openelis.gwt.screen.Screen;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
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
public class Browser extends Composite {
    
    /*
     * The main panel used to contain and display windows
     */
    protected AbsolutePanel browser;
    protected FocusPanel    fp;
    
    /*
     * Hash of all currently displayed windows
     */
    protected HashMap<WindowInt,WindowValues> windows;
    protected HashMap<String,WindowInt> windowsByKey;
    
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
    protected WindowInt focusedWindow;
        
    protected WindowNoImageCSS css = OpenELISResources.INSTANCE.window(); 
    
    public Browser() {

    };
    
    /**
     * Constructor that takes an arguments if the browser should auto-size to the window,
     * and the number of allowed screens to show at once.
     * @param size
     * @param limit
     */
    public Browser(boolean size, int limit) {
    	init(size,limit);
    }
    
    public void init(boolean size, int limit) {
    	css.ensureInjected();
        browser = new AbsolutePanel();
        browser.setWidth("100%");
        browser.setHeight("100%");
        fp = new FocusPanel();
        windows = new HashMap<WindowInt, WindowValues>();
        windowsByKey = new HashMap<String,WindowInt>();
        
        dragController = new PickupDragController(browser,true);
        
        dropController = new AbsolutePositionDropController(browser) {
            @Override
            public void onDrop(DragContext context) {
                super.onDrop(context);
                ((WindowInt)context.draggable).positionGlass();
            }
        };
        
        this.limit = limit;
        
        fp.add(browser);
        initWidget(fp);
        
        dragController.setBehaviorDragProxy(true);
        dragController.registerDropController(dropController);
        DOM.setStyleAttribute(browser.getElement(),
                              "overflow",
                              "auto");
        
        if(size){
            com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {

				public void onResize(ResizeEvent event) {
					 resize();
					
				}

            });
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
				public void execute() {
					 resize();
				}
			});
        }
        
        
        setKeyHandling();
    }
    
    public void setKeyHandling() {
        /**
         * This handler is added to forward the key press event on to the focused window if received by the browser
         */
        addDomHandler(new KeyDownHandler() {
        	public void onKeyDown(KeyDownEvent event) {
   				KeyDownEvent.fireNativeEvent(event.getNativeEvent(), focusedWindow);        		
        	}
        },KeyDownEvent.getType());
        
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
            com.google.gwt.user.client.Window.alert(Constants.get().tooManyWindows());
            return;
        }
        
        /*
         * If Screen is already being shown bring the Screen to focus and exit.
         */
        if (windowsByKey.containsKey(key)) {
            selectScreen(key);
            return;
        }
        
        /*
         * Sets cursor to loading
         */
        RootPanel.get().addStyleName(css.ScreenLoad());
     
        /*
         * Create a ScreenWindow and add the passed Screen to it to be added to the
         * browser.
         */
        WindowInt window = (WindowInt)GWT.create(org.openelis.gwt.widget.Window.class);
        window.setContent(screen);
        screen.setWindow(window);
        addWindow(window,key);
    }
    
    /**
     * Adds a Window directly to the browser indexing it by the passed key
     * @param window
     * @param key
     */
    public void addWindow(WindowInt window, String key) {
        WindowValues wv;
        
    	index++;
    	browser.add(window.asWidget(),(windows.size()*25),(windows.size()*25));
    	wv = new WindowValues();
    	wv.key = key;
    	wv.zIndex = index;
    	windows.put(window,wv);
    	windowsByKey.put(key,window);
    	window.addCloseHandler(new CloseHandler<WindowInt>() {
    	    public void onClose(CloseEvent<WindowInt> event) {
    	       windowsByKey.remove(windows.remove(event.getSource()).key);
    	       setFocusedWindow();
    	    }
    	});
    	window.addFocusHandler(new FocusHandler() {
    	    public void onFocus(FocusEvent event) {
    	        selectScreen(windows.get(event.getSource()).key);
    	        
    	    }
    	});
    	window.makeDragable(dragController);
    	setFocusedWindow();
    	focusedWindow = window;
    	window.setFocus(true);
    }
    
    /**
     * Brings the window indexed by the passed key to be the focusedWindow
     * @param key
     * @return
     */
    public boolean selectScreen(String key) {
        WindowInt wid;
        WindowValues wv;
        
    	if (windowsByKey.containsKey(key)) {
    		wid = windowsByKey.get(key);
    		wv = windows.get(wid);
    		
    		if(index != wv.zIndex){
    			index++;
    			wv.zIndex = index;
    			int top = browser.getWidgetTop(wid.asWidget());
    			int left = browser.getWidgetLeft(wid.asWidget());
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
        if (isVisible()) {
            setHeight((com.google.gwt.user.client.Window.getClientHeight() - getAbsoluteTop()) + "px");
            setWidth((com.google.gwt.user.client.Window.getClientWidth() - getAbsoluteLeft())+ "px");
        }
    }
    
    /**
     * This method will make sure that the window with the largest index is brought to the 
     * top and focused.
     */
    public void setFocusedWindow() {
    	for(WindowInt wid : windowsByKey.values()) {
    		if(windows.get(wid).zIndex != index){
    			if(wid.asWidget().getStyleName().indexOf(css.unfocused()) < 0){	
    				wid.asWidget().addStyleName(css.unfocused());
    			}
    		}else{
    			wid.asWidget().removeStyleName(css.unfocused());
    			focusedWindow = wid;
    			wid.setFocus(true);
    		}
    	}
    }
    /**
     * This method will return true if their is a window currently in the browser
     * using the key passed.
     * @param key
     * @return
     */
    public WindowInt getScreenByKey(String key) {
    	return windowsByKey.get(key);
    }
    
    private class WindowValues {
        protected String key;
        protected int zIndex;
    }
	
}
