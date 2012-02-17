package org.openelis.gwt.widget;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;

public class BrowserChromeImpl extends Browser {
	
	public BrowserChromeImpl() {
	}
	
	public BrowserChromeImpl(boolean size, int windows) {
		super(size,windows);
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

}
