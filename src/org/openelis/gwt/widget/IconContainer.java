package org.openelis.gwt.widget;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class IconContainer extends FocusPanel implements HasAllMouseHandlers, HasClickHandlers, MouseOverHandler, MouseOutHandler {
    

    private boolean enabled = true;
    
    public IconContainer() {
        addMouseOverHandler(this);
        addMouseOutHandler(this);
    }
    
    public IconContainer(String style) {
        this();
        setStyleName(style);
        addMouseOverHandler(this);
        addMouseOutHandler(this);
    }
    
    public void enable(boolean enabled) {
        this.enabled = enabled;
        if(!enabled)
        	unsinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.ONKEYPRESS);
        else
        	sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.ONKEYPRESS);
        	
    }

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler,MouseDownEvent.getType());
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler,MouseUpEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler,MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler,MouseOverEvent.getType());
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler,MouseMoveEvent.getType());
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler,MouseWheelEvent.getType());
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler,ClickEvent.getType());
	}

	public void onMouseOver(MouseOverEvent event) {
		if(enabled)
			((Widget)event.getSource()).addStyleName("Hover");
		
	}

	public void onMouseOut(MouseOutEvent event) {
		((Widget)event.getSource()).removeStyleName("Hover");
		
	}
    
}
