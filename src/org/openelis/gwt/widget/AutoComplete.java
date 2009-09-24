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
package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.event.BeforeGetMatchesEvent;
import org.openelis.gwt.event.BeforeGetMatchesHandler;
import org.openelis.gwt.event.GetMatchesEvent;
import org.openelis.gwt.event.GetMatchesHandler;
import org.openelis.gwt.event.HasBeforeGetMatchesHandlers;
import org.openelis.gwt.event.HasGetMatchesHandlers;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.screen.UIUtil;
import org.openelis.gwt.widget.table.TableDataRow;
import org.openelis.gwt.widget.table.TableRenderer;
import org.openelis.gwt.widget.table.TableView;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;

public class AutoComplete<T> extends DropdownWidget implements FocusHandler, BlurHandler, HasValue<T>, HasBeforeGetMatchesHandlers, HasGetMatchesHandlers{
    
    public AutoCompleteListener listener = new AutoCompleteListener(this);
    public boolean queryMode;
    public Field<T> field;
    
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
        setWidget(textbox);
        setStyleName("AutoDropDown");
        textbox.setStyleName("TextboxUnselected");
        textbox.addFocusHandler(this);
        textbox.addBlurHandler(this);
        popup.setStyleName("DropdownPopup");
        popup.setWidget(view);
        popup.addCloseHandler(this);
        textbox.addKeyUpHandler(listener);
        textbox.setReadOnly(!enabled);
        
        this.isDropdown = true;
        addHandler(keyboardHandler,KeyDownEvent.getType());
        addHandler(keyboardHandler,KeyUpEvent.getType());
    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void enable(boolean enabled) {
        this.enabled = enabled;
        textbox.setReadOnly(!enabled);
        if(enabled){
        	sinkEvents(Event.KEYEVENTS);
        }else
        	unsinkEvents(Event.KEYEVENTS);
        super.enable(enabled);
    }
    
    public boolean isEnabled() {
    	return enabled;
    }
    
    public void getMatches(final String text) {
        if(!queryMode) {
            try {
            	if(getHandlerCount(BeforeGetMatchesEvent.getType()) > 0) {
            		BeforeGetMatchesEvent event = BeforeGetMatchesEvent.fire(this, text);
            		if(event.isCancelled())
            			return;
            	}
            	if(getHandlerCount(GetMatchesEvent.getType()) > 0)
            		GetMatchesEvent.fire(this, text);
            	else
            		throw new Exception("No GetMatchesHandler registered to AutoComplete Widget");

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
    }
        
    public void setModel(ArrayList<TableDataRow> model){
        this.load(model);
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

    public void setSelection(T key, String display) {
    	ArrayList<TableDataRow> model = new ArrayList<TableDataRow>();
    	model.add(new TableDataRow(key,display));
    	setModel(model);
    	setSelection(key);
    }
    
    public void setSelection(TableDataRow row) {
    	ArrayList<TableDataRow> model = new ArrayList<TableDataRow>();
    	model.add(row);
    	setModel(model);
    	setSelection(row.key);
    }
    
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler,ValueChangeEvent.getType());
    }
    
    public HandlerRegistration addFieldValueChangeHandler(
    		ValueChangeHandler handler) {
    	return addValueChangeHandler(handler);
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
		addBlurHandler(field);
		textbox.addMouseOutHandler(field);
		textbox.addMouseOverHandler(field);
	}
	
    public void onFocus(FocusEvent event) {
    	if(isEnabled())
    		textbox.addStyleName("Focus");
    }

    public void onBlur(BlurEvent event) {
    	textbox.removeStyleName("Focus");
    	if("".equals(textbox.getValue())){
	    	if(getValue() != null){
    			setSelection(null,"");
    			ValueChangeEvent.fire(this, null);
    		}
    	}
    }
    
    @Override
    public void setFocus(boolean focus) {
    	textbox.setFocus(focus);
    }

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);	
		if(query)
			textbox.unsinkEvents(Event.ONKEYUP);
		else
			textbox.sinkEvents(Event.ONKEYUP);
			
	}
	
	@Override
	public void checkValue() {
		if(!field.queryMode)
			field.checkValue(this);
	}
	
	public void getQuery(ArrayList list, String key) {
		if(textbox.getText() != null && !textbox.getText().equals("")){
			QueryData qd = new QueryData();
			qd.key = key;
			qd.query = textbox.getText();
			qd.type = QueryData.Type.STRING;
			list.add(qd);
			QueryFieldUtil qField = new QueryFieldUtil();
			qField.parse(qd.query);
			int nothing = 0;
			
		}
	}
	
	@Override
	public ArrayList<String> getErrors() {
		return field.errors;
	}
    
	public Object getFieldValue() {
		return getValue();
	}
	
	public void setFieldValue(Object value) {
		setValue((T)value);
	}
	
	@Override
	public void complete() {
		super.complete();
		ValueChangeEvent.fire(this, getValue());
		textbox.setFocus(true);
		
	}
	
	public HandlerRegistration addBeforeGetMatchesHandler(BeforeGetMatchesHandler handler) {
		return addHandler(handler, BeforeGetMatchesEvent.getType());
	}
	
	public HandlerRegistration addGetMatchesHandler(GetMatchesHandler handler) {
		return addHandler(handler,GetMatchesEvent.getType());
	}
	
	
}
