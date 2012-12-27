package org.openelis.gwt.widget.tree;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class TreeRow extends Widget implements HasAllMouseHandlers, HasDoubleClickHandlers{
    
    public int index;
    public int modelIndex;
    public TreeDataItem item;
    
    public int dragIndex;
    public int dragModelIndex;
    public int droppedIndex = -1;
    public TreeDataItem dragItem;
    protected TreeWidget controller;
    
    public TreeRow() {
        
    }
    
    public TreeRow(Element elem) {
        setElement(elem);
        sinkEvents(Event.MOUSEEVENTS);
        onAttach();
        addDoubleClickHandler(new DoubleClickHandler() {
			public void onDoubleClick(DoubleClickEvent event) {
				controller.toggle(item);
			}
        	
        });
    }
    
    public void setDragValues() {
        dragIndex = index;
        dragModelIndex = modelIndex;
        dragItem = item;
    }
    
    public Widget getDragProxy() {
    	Label wid = new Label(dragItem.cells.get(0).value.toString());
    	wid.setStyleName("ScreenLabel");
    	return wid;
    }
    
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return addDomHandler(handler, MouseDownEvent.getType());
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return addDomHandler(handler, MouseUpEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler,MouseOverEvent.getType());
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return addDomHandler(handler, MouseMoveEvent.getType());
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return addDomHandler(handler,MouseWheelEvent.getType());
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return addDomHandler(handler,DoubleClickEvent.getType());
	}
    
}
