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
package org.openelis.gwt.widget.rewrite;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;

import org.openelis.gwt.common.rewrite.data.TableDataRow;

import java.util.ArrayList;

public class AutoComplete<T> extends DropdownWidget implements HasValue<T> {
    
    public AutoCompleteListener listener = new AutoCompleteListener(this);
    public boolean queryMode;
    

    AutoCompleteCallInt autoCall;
    public String cat;
    
    public AutoComplete() {
    
    }
    
    public void setup() {
        super.init();
        lookUp.icon.addMouseListener(listener);
        lookUp.icon.addClickListener(listener);
        lookUp.textbox.addKeyboardListener(listener);
        lookUp.setIconStyle("");
        
        this.isDropdown = true;
    }
    
    
    public void getMatches(final String text) {
        if(!queryMode) {
            //if(screen != null && ((AppScreen)screen).window != null)
            //    ((AppScreen)screen).window.setStatus("", "spinnerIcon");
            try {
                autoCall.callForMatches(this, text);

            } catch (Exception e) {
                Window.alert(e.getMessage());
            }
        }
    }
    
    public void showAutoMatches(ArrayList<TableDataRow> data){
        activeRow = -1;
        activeCell = -1;
        model.load(data);
        showTable(0);
        //if(screen != null && ((AppScreen)screen).window != null)
        //    ((AppScreen)screen).window.setStatus("", "");
    }
    
    public void setAutoCall(AutoCompleteCallInt autoCall) {
        this.autoCall = autoCall;
    }
    
    public void setWidth(String width) {
        lookUp.textbox.setWidth(width);
    }
    
    public void setModel(ArrayList<TableDataRow> model){
        this.model.load((ArrayList<TableDataRow>)model.clone());
    }
    
    public T getValue() {
        if(model.getSelectedIndex() > -1)
            return (T)model.getRow(model.getSelectedIndex()).key;
        else
            return null;
    }

    public void setValue(T value) {
        setValue(value,false);
    }

    public void setValue(T value, boolean fireEvents) {
        T old = getValue();
        setSelection(value);
        if(fireEvents)
            ValueChangeEvent.fireIfNotEqual(this, old, value);
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler,ValueChangeEvent.getType());
    }
}