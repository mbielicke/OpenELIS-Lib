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
import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
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
public class WindowBrowser extends Composite implements HasKeyPressHandlers, KeyPressHandler, DragHandler {
    
    public AbsolutePanel browser = new AbsolutePanel();
    public HashMap<String,ScreenWindow> windows = new HashMap<String,ScreenWindow>();
    public int index;
    public int limit ;
    public PickupDragController dragController = new PickupDragController(browser,true) {
    	@Override
    	public void previewDragStart() throws VetoDragException {
    		
    		int x = context.draggable.getAbsoluteLeft();
    		int y = context.draggable.getAbsoluteTop();
    		int width = context.draggable.getOffsetWidth();
    		int height = context.draggable.getOffsetHeight();

    		/**
    		 * if the drag exceeds either boundary return and cancel the move update.
    		 
    		if(x+width > browser.getAbsoluteLeft()+browser.getOffsetWidth())
    			throw new VetoDragException();
    		
    		if(y+height > browser.getAbsoluteTop()+browser.getOffsetHeight())
    			throw new VetoDragException();
    		*/
    		
    		super.previewDragStart();
    		
    	}
    	
    	public void dragMove() {
    		/*
    		int x = context.draggable.getAbsoluteLeft();
    		int y = context.draggable.getAbsoluteTop();
    		int width = context.draggable.getOffsetWidth();
    		int height = context.draggable.getOffsetHeight();

    		/**
    		 * if the drag exceeds either boundary return and cancel the move update.
    		 
    		if(x+width > browser.getAbsoluteLeft()+browser.getOffsetWidth())
    			return;
    		
    		if(y+height > browser.getAbsoluteTop()+browser.getOffsetHeight())
    			return;
    		*/
    		super.dragMove();
    		
    	};
    };
    public AbsolutePositionDropController dropController = new AbsolutePositionDropController(browser) {
    	@Override
    	public void onDrop(DragContext context) {
    		// TODO Auto-generated method stub
    		super.onDrop(context);
    		((ScreenWindow)context.draggable).positionGlass();
    	}
    };
    public FocusPanel focusedWindow;
    
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
       
    public void addScreen(Screen screen) {
        addScreen(screen, null);
    }

    public void addScreen(Screen screen, String key) {
        if (key == null)
            key = screen.getClass().getName();
        if (windows.size() == limit) {
            Window.alert("Please close at least one window before opening another.");
            return;
        }
        if (windows.containsKey(key)) {
            selectScreen(key);
            return;
        }
        RootPanel.get().addStyleName("ScreenLoad");
        index++ ;
        ScreenWindow window = new ScreenWindow(this, key);
        window.setContent(screen);
        screen.setWindow(window);
        browser.add(window, (windows.size() * 25), (windows.size() * 25));
        windows.put(key, window);
        setFocusedWindow();
    }

    public void addWindow(ScreenWindow window, String key) {
        index++ ;
        browser.add(window, (windows.size() * 25), (windows.size() * 25));
        windows.put(key, window);
        setFocusedWindow();
    }

    public void addWindow(ScreenWindow window, String key, int x, int y) {
        index++ ;
        browser.add(window, x, y);
        windows.put(key, window);
        setFocusedWindow();
    }

    public boolean selectScreen(String text) {
        if (windows.containsKey(text)) {
            ScreenWindow wid = windows.get(text);
            if (index != wid.zIndex) {
                index++ ;
                wid.zIndex = index;
                int top = browser.getWidgetTop(wid);
                int left = browser.getWidgetLeft(wid);
                browser.add(wid, left, top);
                setFocusedWindow();
            }
            return true;
        }
        return false;
    }

    public void resize() {
        if (browser.isVisible()) {
            browser.setHeight( (Window.getClientHeight() - browser.getAbsoluteTop()) + "px");
            browser.setWidth( (Window.getClientWidth() - browser.getAbsoluteLeft()) + "px");
        }
    }

    public void setFocusedWindow() {
        for (ScreenWindow wid : windows.values()) {
            if (wid.zIndex != index) {
                if (wid.getStyleName().indexOf("unfocused") < 0) {
                    wid.addStyleName("unfocused");
                }
            } else {
                wid.removeStyleName("unfocused");
                focusedWindow = wid;
            }
        }
    }
    
    public ScreenWindow getScreenByKey(String name) {
    	return windows.get(name);
    }

    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return addDomHandler(handler, KeyPressEvent.getType());
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

    public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
        // TODO Auto-generated method stub
    }
}
