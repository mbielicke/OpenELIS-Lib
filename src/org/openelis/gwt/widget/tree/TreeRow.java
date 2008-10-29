package org.openelis.gwt.widget.tree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DragListenerCollection;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.dnd.DropListenerCollection;
import com.google.gwt.user.client.dnd.SourcesDragEvents;
import com.google.gwt.user.client.dnd.SourcesDropEvents;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.TreeDataItem;

public class TreeRow extends Widget implements SourcesMouseEvents, SourcesDragEvents, SourcesDropEvents {
    
    MouseListenerCollection mouseListeners = new MouseListenerCollection();
    DropListenerCollection dropListeners = new DropListenerCollection();
    DragListenerCollection dragListeners = new DragListenerCollection();
    
    public int index;
    public int modelIndex;
    
    public TreeDataItem item;
    
    public TreeRow(Element elem, boolean setEvent) {
        //super(elem.getString());
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
        dragListeners.add(listener,this);
        
    }

    public void removeDragListener(DragListener listener) {
        dragListeners.remove(listener);
        
    }

    public void addDropListener(DropListener listener) {
        dropListeners.add(listener,this);
        
    }

    public void removeDropListener(DropListener listener) {
        dropListeners.remove(listener);
        
    }
    
    public TreeRow getProxy() {
        removeStyleName("Highlighted");
        Element div = DOM.createDiv();
        Element table = DOM.createTable();
        div.appendChild(table);
        Element tr = (Element)getElement().cloneNode(true);
        //setStyleName(tr, "Highlighted",false);
        table.appendChild(tr);
        TreeRow clone = new TreeRow(div,false);
        //clone.dragListeners = dragListeners;
       // clone.mouseListeners = mouseListeners;
       // clone.dropListeners = dropListeners;
        clone.index = index;
        clone.modelIndex = modelIndex;
        clone.item  = item;
        return clone;
    }
    
    @Override
    protected void onAttach() {
        super.onAttach();
    }
    
}
