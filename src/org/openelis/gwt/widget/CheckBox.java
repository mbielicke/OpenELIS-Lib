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

import org.openelis.ui.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;


public class CheckBox extends FocusPanel implements ClickHandler, HasValue<String>,  HasField<String>, HasMouseOutHandlers, HasMouseOverHandlers, HasKeyDownHandlers, KeyDownHandler {
        
    boolean enabled = true;
    
    private Field<String> field;
    
    public enum CheckType {TWO_STATE,THREE_STATE};
    
    public static final String UNCHECKED = "N",
                            CHECKED = "Y",
                            UNKNOWN = null;
    
    public static final String UNCHECKED_STYLE = "Unchecked",
                                CHECKED_STYLE = "Checked",
                                UNKNOWN_STYLE = "Unknown";
        
    private String state = UNCHECKED; 
    
    private CheckType type = CheckType.TWO_STATE;
    
    private HorizontalPanel hp = new HorizontalPanel();
    private final CheckBox check = this;
        
    public CheckBox() {
        setStyleName(UNCHECKED_STYLE);
        addDomHandler(this,ClickEvent.getType());
        addDomHandler(this,KeyDownEvent.getType());
    }
    
    public CheckBox(CheckType type) {
        this();
        this.type = type;
        if(type == CheckType.THREE_STATE)
            setState(UNKNOWN);
    }
    
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void setType(CheckType type){
        this.type = type;
        if(type == CheckType.THREE_STATE)
            setValue(UNKNOWN);
        else
            setValue(UNCHECKED);
    }
    
    public CheckType getType() {
        return type;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
    	String old = this.state;
        this.state = state;
        if(CHECKED.equals(state))
            setStyleName(CHECKED_STYLE);
        else if(UNCHECKED.equals(state)) 
            setStyleName(UNCHECKED_STYLE);
        else if(state == UNKNOWN && type == CheckType.THREE_STATE)
            setStyleName(UNKNOWN_STYLE);
        else{
            setStyleName(UNCHECKED_STYLE);
            this.state = UNCHECKED;
        }
        
        //ValueChangeEvent.fireIfNotEqual(this, old, this.state);
    }

    public void onClick(ClickEvent event) {
    	if(!enabled)
    		return;
       if(type == CheckType.TWO_STATE){
           if(state == CHECKED)
               setValue(UNCHECKED,true);
           else
               setValue(CHECKED,true);
       }else{
           if(state == CHECKED) 
               setValue(UNCHECKED,true);
           else if (state == UNCHECKED)
               setValue(UNKNOWN,true);
           else
               setValue(CHECKED,true);
       }
       addStyleName("Focus");
       //field.setValue(state);
    }
    
    public void enable(boolean enabled){
       this.enabled = enabled;
       if(enabled){
    	   removeStyleName("disabled");
       }else
    	   addStyleName("disabled");
    }
    
    public boolean isEnabled(){
        return enabled;
    }

	public String getValue() {
		return getState();
	}

	public void setValue(String value) {
		setValue(value,false);
	}

	public void setValue(String value, boolean fireEvents) {
		String old = getState();
		setState(value);
		if(fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, old, value);
		else
			field.setValue(value);
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler,MouseOutEvent.getType());
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler,MouseOverEvent.getType());
	}

	public void addException(Exception error) {
		field.addException(error);
		field.drawExceptions(this);
	}

	public void clearExceptions() {
		field.clearExceptions(this);
	}

	public Field getField() {
		return field;
	}

	public void setField(Field<String> field) {
		this.field = field;
		addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addDomHandler(handler,KeyDownEvent.getType());
	}

	public void onKeyDown(KeyDownEvent event) {
		if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	    	if(!enabled)
	    		return;
	       if(type == CheckType.TWO_STATE){
	           if(state == CHECKED)
	               setValue(UNCHECKED,true);
	           else
	               setValue(CHECKED,true);
	       }else{
	           if(state == CHECKED) 
	               setValue(UNCHECKED,true);
	           else if (state == UNCHECKED)
	               setValue(UNKNOWN,true);
	           else
	               setValue(CHECKED,true);
	       }
	       addStyleName("Focus");
		}
	}

	public void setQueryMode(boolean query) {
		if(query == field.queryMode)
			return;
		
		if(query)
			setType(CheckType.THREE_STATE);
		else
			setType(CheckType.TWO_STATE);
		field.setQueryMode(query);
	}

	public void checkValue() {
		field.checkValue(this);
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		if(!field.queryMode)
			return;
		if(field.queryString != null && !field.queryString.equals("")){
			QueryData qd = new QueryData();
			qd.setQuery(field.queryString);
			qd.setKey(key);
			qd.setType(QueryData.Type.STRING);
			list.add(qd);
		}
		
	}

	public ArrayList<Exception> getExceptions() {
		return field.exceptions;
	}

	public String getFieldValue() {
		return field.getValue();
	}

	public void setFieldValue(String value) {
		field.setValue(value);
		setValue(value);
	}
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return field.addValueChangeHandler(handler);
	}

	public void addExceptionStyle(String style) {
		addStyleName(style);
	}

	public Object getWidgetValue() {
		return getValue();
	}

	public void removeExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

}
