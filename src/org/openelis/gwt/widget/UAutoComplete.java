package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.Util;
import org.openelis.gwt.event.GetMatchesEvent;
import org.openelis.gwt.event.GetMatchesHandler;
import org.openelis.gwt.event.HasGetMatchesHandlers;
import org.openelis.gwt.widget.table.TableWidget;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;

public class UAutoComplete<T> extends UDropdown<T> implements HasGetMatchesHandlers {
    
    
//************ Overridden methods in Dropdown ***************************    
    /**
     * Overridden method from Dropdown to draw the AutoComplete and set handlers 
     */
    @Override
    public void init() {
        final KeyboardHandler keyHandler = new KeyboardHandler();
        
        textbox = new com.google.gwt.user.client.ui.TextBox();
        textbox.setStyleName("TextboxUnselected");
        
        initWidget(textbox);
        
        addHandler(keyHandler,KeyDownEvent.getType());
        addHandler(keyHandler,KeyUpEvent.getType());
    }
    
    /**
     * Overridden method from Dropdown to enable and disable the 
     * AutoComplete
     */
    @Override
    public void setEnabled(boolean enabled) {
        textbox.setReadOnly(!enabled);
        if(enabled)
            sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        else
            unsinkEvents(Event.ONKEYUP | Event.ONKEYDOWN);
    }
    
    /**
     * Overridden method form Dropdown
     */
    @Override
    public void setPopupContext(TableWidget tableDef) {
        super.setPopupContext(tableDef);
        /*
         * This is the only reason for this method being here
         * so the table is not styled as a Dropdown.  Maybe stop implementing
         * this here in with table redesign
         */
        table.isDropdown = false;
    }
    
    @Override
    protected void handleKeyInput() {
        GetMatchesEvent.fire(this, getText());
    }
    
    
//*************** End User methods added for AutoComplete ***********************    
    /**
     * Added this setValue permutation for Autocomplete for when the user set the 
     * initial value of the widget in onDataChange.
     * @param key
     * @param display
     */
    public void setValue(T key, String display){
        ArrayList<Item<T>> model;
        Item<T>            item;
        
        model = new ArrayList<Item<T>>();
        item = new Item<T>(key,display);
        model.add(item);
        
        setModel(model);
        setValue(key);
        
    }
    
    public void showAutoMatches(ArrayList<Item<T>> model) {
        setModel(model);
        showPopup();
    }
    
//********** Implementation of HasGetMatchesHandler **************************
    
    public HandlerRegistration addGetMatchesHandler(GetMatchesHandler handler) {
        return addHandler(handler,GetMatchesEvent.getType());
    }

}
