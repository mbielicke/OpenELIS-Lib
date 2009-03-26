/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.MenuLabel;

import java.util.ArrayList;

public class TableCellInputWidget extends SimplePanel implements TableCellWidget, MouseListener, SourcesMouseEvents, FocusListener {

    protected AbstractField field;
    protected VerticalPanel errorPanel = new VerticalPanel();
    protected PopupPanel pop;
    private MouseListenerCollection listeners = new MouseListenerCollection(); 
    protected ScreenBase screen;
    public int rowIndex;
    
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
        validate();
    }
    
    public void validate() {
        field.clearErrors();
        field.validate();
        if(!field.isValid()){
            drawErrors();
        }else{
            clearErrors();
        }
    }
    
    public void drawErrors() {
        addStyleName("CellError");
        ArrayList<String> errors = field.getErrors();
        errorPanel.clear();
        for (String error : errors) {
            MenuLabel errorLabel = new MenuLabel(error,"Images/bullet_red.png");
            errorLabel.setStyleName("errorPopupLabel");
            //errorPanel.add(new MenuLabel(error,"Images/bullet_red.png"));
            errorPanel.add(errorLabel);
        }
        if(errors.size() == 0){
            removeMouseListener(this);
        }else{
            removeMouseListener(this);
            addMouseListener(this);
        }

    }
    
    public void clearErrors() {
        errorPanel.clear();
        removeStyleName("CellError");
        removeMouseListener(this);
    }

    public void setDisplay() {
        if(!field.isValid())
            drawErrors();
        else
            clearErrors();

    }

    public void setEditor() {
        // TODO Auto-generated method stub

    }

    public void setField(FieldType field) {
       if(field instanceof AbstractField)
           this.field = (AbstractField)field;

    }
    
    public AbstractField getField() {
        return field;
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        if(!field.isValid()){
            if(pop == null){
                pop = new PopupPanel();
                //pop.setStyleName("ErrorPopup");
            }
            DecoratorPanel dp = new DecoratorPanel();
            
            //ScreenWindow win = new ScreenWindow(pop,"","","",false);
            dp.setStyleName("ErrorWindow");
            dp.add(errorPanel);
            dp.setVisible(true);
            pop.setWidget(dp);
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
    
    public void setCellWidth(int width) {
        
    }

    public void onFocus(final Widget sender) {
        if(sender instanceof TextBoxBase){
            DeferredCommand.addCommand(new Command(){
                public void execute() {
                   //((TextBoxBase)sender).setSelectionRange(0,0);
                   ((TextBoxBase)sender).setCursorPos(0);
                   ((TextBoxBase)sender).selectAll();
                }
            });
        }
        
    }

    public void onLostFocus(Widget sender) {
        if(sender instanceof TextBoxBase){
            ((TextBoxBase)sender).setSelectionRange(0, 0);
        }
    }
    
    public void setFocus(boolean focus) {
    }
    
    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int row) {
        rowIndex = row;
    }
    
    public void setForm(State state) {
        
    }

}
