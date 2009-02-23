package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.DataSet;

public class TableRow extends Widget implements SourcesMouseEvents {

    public MouseListenerCollection mouseListeners = new MouseListenerCollection();
    
    public int index;
    public int modelIndex;
    public DataSet<Object> row;
    
    public int dragIndex;
    public int dragModelIndex;
    public DataSet<Object> dragRow;
    
    public TableRow() {
        
    }
    
    public TableRow(Element elem) {
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
            case Event.ONMOUSEUP :
            case Event.ONMOUSEOVER :
            case Event.ONMOUSEOUT :
                DOM.eventPreventDefault(event);
                mouseListeners.fireMouseEvent(this, event);
                break;
        }
    }
    
    public void setDragValues() {
        dragIndex = index;
        dragModelIndex = modelIndex;
        dragRow = (DataSet<Object>)row.clone();
    }
}
