package org.openelis.gwt.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;

public class IconContainer extends AbsolutePanel implements SourcesMouseEvents, SourcesClickEvents {
    
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
    
}
