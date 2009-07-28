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

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowResizeListener;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenWindow;
import org.openelis.gwt.screen.rewrite.Screen;

import java.util.HashMap;

/**
 * WindowBrowser will display Screen widgets in draggable Windows
 * in a certain portion of the screen.  It uses the ScreenWindow
 * widget to wrap the contents of a screen.  It also control the 
 * z-index of the windows displayed.
 * 
 * @author tschmidt
 *
 */
public class WindowBrowser extends Composite implements HasKeyPressHandlers, KeyPressHandler, DragHandler {
    
    public AbsolutePanel browser = new AbsolutePanel();
    public HashMap<String,ScreenWindow> windows = new HashMap<String,ScreenWindow>();
    public int index;
    public int limit ;
    public PickupDragController dragController = new PickupDragController(browser,true);
    public AbsolutePositionDropController dropController = new AbsolutePositionDropController(browser) {
    	@Override
    	public void onDrop(DragContext context) {
    		// TODO Auto-generated method stub
    		super.onDrop(context);
    	    ((ScreenWindow)context.draggable).positionGlass();
    	}
    };
    public ScreenWindow focusedWindow;
    
    public static native void setIndex(Element elem, int index) /*-{
        elem.style.zIndex = index;
    }-*/;
    
    public static native int getIndex(Element elem) /*-{
        return Number(elem.style.zIndex);
    }-*/;
    
    public WindowBrowser() {
    }
    
    public WindowBrowser(boolean size, int limit) {
        init(size,limit);
    }
    
    public void init(boolean size, int limit) {
        this.limit = limit;
        initWidget(browser);
        dragController.setBehaviorDragProxy(true);
        dragController.registerDropController(dropController);
        //setIndex(getElement(),index);
        DOM.setStyleAttribute(browser.getElement(),
                              "overflow",
                              "auto");
        if(size){
            Window.addWindowResizeListener(new WindowResizeListener() {

                public void onWindowResized(int width, int height) {
                    setBrowserHeight();
                }

            });
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    setBrowserHeight();
                    //index = getIndex(browser.getElement());
                }
            });
        }
    }
    
    public void addScreen(final Screen screen, final String text, final String category, final String loadingText) {
        if(windows.size() == limit){
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        if (windows.containsKey(text)) {
            return;
        }
        RootPanel.get().addStyleName("ScreenLoad");
        final WindowBrowser brws = this;
        index++;
        ScreenWindow window = new ScreenWindow(brws, text, category, loadingText,false);
        window.setContent(screen);
        browser.add(window,(windows.size()*25),(windows.size()*25));
        windows.put(text,window);
//        if(screen instanceof AppScreen){
  //          DOM.addEventPreview((AppScreen)screen);
   //     }
    }
    
    public void addScreen(final ScreenBase screen, final String text, final String category, final String loadingText) {
        if(windows.size() == limit){
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        if (windows.containsKey(text)) {
            return;
        }
        RootPanel.get().addStyleName("ScreenLoad");
        final WindowBrowser brws = this;
        index++;
        ScreenWindow window = new ScreenWindow(brws, text, category, loadingText,false);
        window.setContent(screen);
        browser.add(window,(windows.size()*25),(windows.size()*25));
        windows.put(text,window);
        if(screen instanceof AppScreen){
            DOM.addEventPreview((AppScreen)screen);
        }
    }
    
    public void addScreen(AppScreen screen) {
        addScreen(screen,null);
    }
    
    public void addScreen(Screen screen) {
    	addScreen(screen,null);
    }
    
    public void addScreen(AppScreen screen, String key) {
        if(key == null)
           key = GWT.getTypeName(screen);
        if(windows.size() == limit){
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        if (windows.containsKey(key)) {
            selectScreen(key);
            return;
        }
        RootPanel.get().addStyleName("ScreenLoad");
        index++;
        ScreenWindow window = new ScreenWindow(this, key);
        window.setContent(screen);
        browser.add(window,(windows.size()*25),(windows.size()*25));
        windows.put(key,window);
        setFocusedWindow();
    }
    
    public void addScreen(Screen screen, String key) {
        if(key == null)
           key = GWT.getTypeName(screen);
        if(windows.size() == limit){
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        if (windows.containsKey(key)) {
            selectScreen(key);
            return;
        }
        RootPanel.get().addStyleName("ScreenLoad");
        index++;
        ScreenWindow window = new ScreenWindow(this, key);
        window.setContent(screen);
        browser.add(window,(windows.size()*25),(windows.size()*25));
        windows.put(key,window);
        setFocusedWindow();
    }
    
    public boolean selectScreen(String text) {
        if (windows.containsKey(text)) {
            if(index != windows.get(text).zIndex){
                ScreenWindow wid = windows.get(text);
                index++;
                //setIndex(((Widget)windows.get(text)).getElement(),index);
                windows.get(text).zIndex = index;
                int top = browser.getWidgetTop(wid);
                int left = browser.getWidgetLeft(wid);
                wid.setKeep(true);
                browser.add(wid, left, top);
                wid.setKeep(false);
                setFocusedWindow();
            }
            return true;
        }
        return false;
    }
    
    public void setBrowserHeight() {
        if (browser.isVisible()) {
            browser.setHeight((Window.getClientHeight() - browser.getAbsoluteTop()) + "px");
            browser.setWidth((Window.getClientWidth() - browser.getAbsoluteLeft())+ "px");
        }
    }
    
    public void setFocusedWindow() {
        for(ScreenWindow wind : windows.values()) {
            if(wind.zIndex != index){
                if(wind.getStyleName().indexOf("unfocused") < 0)
                    wind.addStyleName("unfocused");
            }else{
                wind.removeStyleName("unfocused");
                focusedWindow = wind;
            }
        }
    }

	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return addDomHandler(handler,KeyPressEvent.getType());
	}

	public void onKeyPress(KeyPressEvent event) {
		KeyPressEvent.fireNativeEvent(event.getNativeEvent(), focusedWindow);
		
	}

	public void onDragEnd(DragEndEvent event) {
		ScreenWindow wind = (ScreenWindow)event.getContext().draggable;
		wind.positionGlass();
		
	}

	public void onDragStart(DragStartEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		// TODO Auto-generated method stub
		
	}

	public void onPreviewDragStart(DragStartEvent event)
			throws VetoDragException {
		// TODO Auto-generated method stub
		
	}


 
}
