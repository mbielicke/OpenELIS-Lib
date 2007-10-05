package org.openelis.gwt.client.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DragListenerCollection;
import com.google.gwt.user.client.dnd.SourcesDragEvents;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

/**
 * Class has been deprecated should be deleted
 * @author tschmidt
 *
 */
public class DragProxy extends Composite implements
                                        SourcesMouseEvents,
                                        SourcesDragEvents {
    private MouseListenerCollection mouseListeners;
    private DragListenerCollection dragListeners;

    private Widget widget;
    public Object value;

    public DragProxy(Widget widget) {
        initWidget(widget);
        System.out.println("Widget set");
        this.widget = widget;
        sinkEvents(Event.MOUSEEVENTS);
    }

    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEUP:
            case Event.ONMOUSEMOVE:
            case Event.ONMOUSEOVER:
            case Event.ONMOUSEOUT:
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
        }
    }

    public void addMouseListener(MouseListener listener) {
        ((SourcesMouseEvents)widget).addMouseListener(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        ((SourcesMouseEvents)widget).removeMouseListener(listener);

    }

    public void addDragListener(DragListener listener) {
        if (dragListeners == null) {
            dragListeners = new DragListenerCollection();
        }
        dragListeners.add(listener, this);

    }

    public void removeDragListener(DragListener listener) {
        if (dragListeners != null) {
            dragListeners.remove(listener);
        }

    }
}
