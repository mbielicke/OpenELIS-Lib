package org.openelis.gwt.widget.tree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.TreeDataItem;

public class TreeRow extends Widget implements SourcesMouseEvents {
    
    public MouseListenerCollection mouseListeners = new MouseListenerCollection();
    
    public int index;
    public int modelIndex;
    public TreeDataItem item;
    
    public int dragIndex;
    public int dragModelIndex;
    public TreeDataItem dragItem;
    
    public TreeRow() {
        
    }
    
    public TreeRow(Element elem) {
        setElement(elem);
        sinkEvents(Event.MOUSEEVENTS);
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
    
    public void setDragValues() {
        dragIndex = index;
        dragModelIndex = modelIndex;
        dragItem = (TreeDataItem)item.clone();
    }
    
}
