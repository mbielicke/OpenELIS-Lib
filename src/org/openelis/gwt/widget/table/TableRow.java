package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DragListenerCollection;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.dnd.DropListenerCollection;
import com.google.gwt.user.client.dnd.SourcesDragEvents;
import com.google.gwt.user.client.dnd.SourcesDropEvents;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.DataSet;

public class TableRow extends Widget implements SourcesMouseEvents, SourcesDragEvents, SourcesDropEvents {

    public MouseListenerCollection mouseListeners = new MouseListenerCollection();
    public DropListenerCollection dropListeners = new DropListenerCollection();
    public DragListenerCollection dragListeners = new DragListenerCollection();
    
    public int index;
    public int modelIndex;
    
    public DataSet row;
    
    public TableRow(Element elem, boolean setEvent) {
        setElement(elem);
        sinkEvents(Event.MOUSEEVENTS);
        if(setEvent)
            onAttach();
    }
    

    public void addMouseListener(MouseListener listener) {
        mouseListeners.add(listener);
    }

    public void removeMouseListener(MouseListener listener) {
        mouseListeners.remove(listener);   
    }
    
    @Override
    public void onBrowserEvent(Event event) {
        switch(DOM.eventGetType(event)){
            case Event.ONMOUSEDOWN :
            case Event.ONMOUSEMOVE :
            case Event.ONMOUSEOUT :
            case Event.ONMOUSEOVER :
            case Event.ONMOUSEUP :
                DOM.eventPreventDefault(event);
                mouseListeners.fireMouseEvent(this, event);
        }
    }

    public void addDragListener(DragListener listener) {
        if(listener != null)
            dragListeners.add(listener,this);
        
    }

    public void removeDragListener(DragListener listener) {
        dragListeners.remove(listener);
        
    }

    public void addDropListener(DropListener listener) {
        if(listener != null)
            dropListeners.add(listener,this);
        
    }

    public void removeDropListener(DropListener listener) {
        dropListeners.remove(listener);
        
    }
    
    public TableRow getProxy() {
        removeStyleName("Highlighted");
        Element div = DOM.createDiv();
        Element table = DOM.createTable();
        div.appendChild(table);
        Element tr = (Element)getElement().cloneNode(true);
        table.appendChild(tr);
        TableRow clone = new TableRow(div,false);
        clone.index = index;
        clone.modelIndex = modelIndex;
        clone.row   = row;
        return clone;
    }
    
}
