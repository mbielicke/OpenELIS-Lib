package org.openelis.gwt.widget;

import org.openelis.gwt.screen.Screen;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

public class WindowChromeImpl extends Window {
	
	public WindowChromeImpl() {
		System.out.println("Using Window Chrome");
	}

	public void setKeyHandling() {
		
        /**
         * This handler is added to forward the key press event if received by the window 
         * down to the screen.
         */
        addDomHandler(new KeyDownHandler() {
         	 public void onKeyDown(KeyDownEvent event) {
         		System.out.println("Window Key down");
        		 KeyDownEvent.fireNativeEvent(event.getNativeEvent(), ((Screen)content).getDefinition().getPanel());   
        	 }
        },KeyDownEvent.getType());
	}
}
