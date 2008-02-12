package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.MenuLabel;

public class TableCellInputWidget extends SimplePanel implements TableCellWidget, MouseListener, SourcesMouseEvents {

    protected AbstractField field;
    protected VerticalPanel errorPanel = new VerticalPanel();
    protected PopupPanel pop;
    private MouseListenerCollection listeners = new MouseListenerCollection(); 
    
    public void clear() {
        // TODO Auto-generated method stub

    }

    public void enable(boolean enabled) {
        // TODO Auto-generated method stub

    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return null;
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveValue() {
        field.clearErrors();
        field.validate();
        if(!field.isValid()){
            addStyleName("CellError");
            String[] errors = field.getErrors();
            errorPanel.clear();
            for (int i = 0; i < errors.length; i++) {
                String error = errors[i];
                errorPanel.add(new MenuLabel(error,"Images/bullet_red.png"));
            }
            addMouseListener(this);
        }else{
            errorPanel.clear();
            removeStyleName("CellError");
            removeMouseListener(this);
        }
    }

    public void setDisplay() {
        // TODO Auto-generated method stub

    }

    public void setEditor() {
        // TODO Auto-generated method stub

    }

    public void setField(AbstractField field) {
        // TODO Auto-generated method stub

    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        if(!field.isValid()){
            if(pop == null){
                pop = new PopupPanel();
                pop.setStyleName("ErrorPopup");
            }
            pop.setWidget(errorPanel);
            pop.setPopupPosition(sender.getAbsoluteLeft()+sender.getOffsetWidth(), sender.getAbsoluteTop());
            pop.show();
        }
        
    }

    public void onMouseLeave(Widget sender) {
        if(!field.isValid()){
            if(pop != null){
                pop.hide();
            }
        }
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
       if(pop.isVisible()){
           pop.hide();
           removeMouseListener(this);
       }
        
    }

    public void addMouseListener(MouseListener listener) {
        listeners.add(listener);
        sinkEvents(Event.MOUSEEVENTS);
        
    }

    public void removeMouseListener(MouseListener listener) {
        listeners.remove(listener);
        unsinkEvents(Event.MOUSEEVENTS);
        
    }
    
    public void onBrowserEvent(Event event) {
        if(DOM.eventGetType(event) == Event.ONMOUSEOVER ||
           DOM.eventGetType(event) == Event.ONMOUSEOUT ||
           DOM.eventGetType(event) == Event.ONMOUSEUP){
            listeners.fireMouseEvent(this, event);
        }
        super.onBrowserEvent(event);
    }

}
