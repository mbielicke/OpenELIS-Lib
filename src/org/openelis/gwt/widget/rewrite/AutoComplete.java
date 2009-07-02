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

import java.util.ArrayList;

import org.openelis.gwt.common.data.QueryField;
import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.HasField;
import org.openelis.gwt.widget.table.rewrite.TableDataRow;
import org.openelis.gwt.widget.table.rewrite.TableMouseHandler;
import org.openelis.gwt.widget.table.rewrite.TableRenderer;
import org.openelis.gwt.widget.table.rewrite.TableView;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;

public class AutoComplete<T> extends DropdownWidget implements HasValue<T>, HasField {
    
    public AutoCompleteListener listener = new AutoCompleteListener(this);
    public boolean queryMode;
    private Field field;
    

    AutoCompleteCallInt autoCall;
    public String cat;
    
    public AutoComplete() {
    
    }
    
    public void setup() {
    	if(maxRows == 0)
    		maxRows = 10;
        renderer = new TableRenderer(this);
        view = new TableView(this,showScroll);
        view.setWidth(width);
        view.setHeight((maxRows*cellHeight+(maxRows*cellSpacing)+(maxRows*2)+cellSpacing));
        keyboardHandler = this;
        mouseHandler = new TableMouseHandler(this);
        setWidget(textbox);
        setStyleName("AutoDropDown");
        textbox.setStyleName("TextboxUnselected");
        textbox.addFocusHandler(this);
        textbox.addBlurHandler(this);
        popup.setStyleName("DropdownPopup");
        popup.setWidget(view);
        popup.addPopupListener(this);
        textbox.addKeyboardListener(listener);
        textbox.setReadOnly(!enabled);
        
        this.isDropdown = true;
        addDomHandler(keyboardHandler,KeyDownEvent.getType());
        addDomHandler(keyboardHandler,KeyUpEvent.getType());
    }
    
    public void addTabHandler(UIUtil.TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void enabled(boolean enabled) {
        this.enabled = enabled;
        textbox.setReadOnly(!enabled);
        super.enabled(enabled);
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
        load(data);
        showTable(0);
        //if(screen != null && ((AppScreen)screen).window != null)
        //    ((AppScreen)screen).window.setStatus("", "");
    }
    
    public void setAutoCall(AutoCompleteCallInt autoCall) {
        this.autoCall = autoCall;
    }
    
    /*
    public void setWidth(String width) {
        setWidth(width);
    }
    */
    
    public void setModel(ArrayList<TableDataRow> model){
        this.load((ArrayList<TableDataRow>)model.clone());
    }
    
    public T getValue() {
        if(getSelectedIndex() > -1)
            return (T)getRow(getSelectedIndex()).key;
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

	public void addError(String error) {
		field.addError(error);
		field.drawError(this);
	}

	public void clearErrors() {
		field.clearError(this);
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
		addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}
	
    public void onFocus(FocusEvent event) {
        if (!textbox.isReadOnly()) {
                // we need to set the selected style name to the textbox
                textbox.addStyleName("TextboxSelected");
                textbox.removeStyleName("TextboxUnselected");
                textbox.setFocus(true);
                textbox.addStyleName("Focus");
        }
    }

    public void onBlur(BlurEvent event) {
        if (!textbox.isReadOnly() && !field.queryMode) {
                // we need to set the unselected style name to the textbox
                textbox.addStyleName("TextboxUnselected");
                textbox.removeStyleName("TextboxSelected");
                textbox.removeStyleName("Focus");
                complete();
        }
    }
    
    @Override
    public void setFocus(boolean focus) {
    	textbox.setFocus(focus);
    }

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);	
		if(query)
			textbox.removeKeyboardListener(listener);
		else
			textbox.addKeyboardListener(listener);
			
	}
	
	@Override
	public void checkValue() {
		if(!field.queryMode)
			field.checkValue(this);
	}
	
	public void getQuery(ArrayList<QueryData> list, String key) {
		if(textbox.getText() != null && !textbox.getText().equals("")){
			QueryData qd = new QueryData();
			qd.key = key;
			qd.query = textbox.getText();
			qd.type = QueryData.Type.STRING;
			list.add(qd);
			QueryField qField = new QueryField();
			qField.parse(qd.query);
			int nothing = 0;
			
		}
	}
	
	@Override
	public ArrayList<String> getErrors() {
		return field.errors;
	}
    
	
}
