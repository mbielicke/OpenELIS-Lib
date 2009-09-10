package org.openelis.gwt.widget.table.deprecated;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.deprecated.Field;
import org.openelis.gwt.common.data.deprecated.FieldType;
import org.openelis.gwt.screen.deprecated.ClassFactory;

@Deprecated
public class TableDate extends SimplePanel implements TableCellWidget {
    
    public int rowIndex;
    public static final String TAG_NAME = "table-date";
    private int width;
    protected MouseListenerCollection mouseListeners = new MouseListenerCollection();

    private AbsolutePanel editor = new AbsolutePanel() {
        @Override
        public void onBrowserEvent(Event event) {
            // TODO Auto-generated method stub
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
            }
            super.onBrowserEvent(event);
        }
    }; 
    
    public TableDate() {
        editor.setStyleName("DateCell");
        editor.sinkEvents(Event.MOUSEEVENTS);
        mouseListeners.add((MouseListener)ClassFactory.forName("HoverListener"));
    }
    
    public void clear() {

    }

    public void enable(boolean enabled) {

    }

    public Widget getInstance(Node node) {
        return new TableDate();
    }

    public TableCellWidget getNewInstance() {
        return new TableDate();
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void saveValue() {

    }

    public void setCellWidth(int width) {
        this.width = width;
        if(editor != null){
            editor.setWidth(width+"px");
        }
    }

    public void setDisplay() {
        setEditor();

    }

    public void setEditor() {
        

    }

    public void setField(FieldType field) {
        // TODO Auto-generated method stub

    }

    public void setFocus(boolean focus) {
        // TODO Auto-generated method stub

    }

    public void setRowIndex(int row) {
        // TODO Auto-generated method stub

    }

}
