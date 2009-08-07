package org.openelis.gwt.widget;

import org.openelis.gwt.screen.UIUtil;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;

public class IconContainer extends FocusPanel implements SourcesMouseEvents, SourcesClickEvents, HasAllMouseHandlers, HasClickHandlers {
    
    MouseListenerCollection mouseListeners;
    ClickListenerCollection clickListeners;
    
    private boolean enabled;
    
    @Override
    public void onBrowserEvent(Event event) {
        if(enabled) {
            switch (DOM.eventGetType(event)) {
                case Event.ONMOUSEDOWN:
                case Event.ONMOUSEUP:
                case Event.ONMOUSEMOVE:
                case Event.ONMOUSEOVER:
                case Event.ONMOUSEOUT:
                    if (mouseListeners != null) {
                        mouseListeners.fireMouseEvent(this, event);
                    }
                    break;
                case Event.ONCLICK:
                    if(clickListeners != null) {
                        clickListeners.fireClick(this);
                    }
            }
        }
        super.onBrowserEvent(event);
    }
    
    public IconContainer() {
        sinkEvents(Event.MOUSEEVENTS);
        sinkEvents(Event.ONCLICK);
    }
    
    public void addTabHandler(UIUtil.TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public IconContainer(String style) {
        this();
        setStyleName(style);
    }

    public void addMouseListener(MouseListener listener) {
        if(mouseListeners == null)
            mouseListeners = new MouseListenerCollection();
        mouseListeners.add(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        if(mouseListeners != null)
            mouseListeners.remove(listener);
    }

    public void addClickListener(ClickListener listener) {
        if(clickListeners == null)
            clickListeners = new ClickListenerCollection();
        clickListeners.add(listener);
    }

    public void removeClickListener(ClickListener listener) {
        if(clickListeners != null)
            clickListeners.remove(listener);
    }
    
    public void enable(boolean enabled) {
        this.enabled = enabled;
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
    
}
