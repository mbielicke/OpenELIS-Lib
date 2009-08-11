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

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.HasField;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.event.KeyboardHandler;

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
    
    public CheckBox(CheckType type, String text){
        this(type);
        Label label = new Label(text);
        label.setStyleName("CheckText");
        hp.add(label);
    }
    
    public CheckBox(CheckType type, Widget widget){
        this(type);
        hp.add(widget);
    }
    
    public void addTabHandler(UIUtil.TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    public void setText(String text){
        Label label = new Label(text);
        label.setStyleName("CheckText");
        if(hp.getWidgetCount() > 1)
            hp.remove(1);
        hp.add(label);
    }
    
    public void setWidget(Widget widget){
        if(hp.getWidgetCount() > 1)
            hp.remove(1);
        hp.add(widget);
    }
    
    public void setType(CheckType type){
        this.type = type;
        if(type == CheckType.THREE_STATE)
            setState(UNKNOWN);
        else
            setState(UNCHECKED);
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
        if(state == CHECKED)
            setStyleName(CHECKED_STYLE);
        else if(state == UNCHECKED) 
            setStyleName(UNCHECKED_STYLE);
        else if(state == UNKNOWN && type == CheckType.THREE_STATE)
            setStyleName(UNKNOWN_STYLE);
        else{
            setStyleName(UNCHECKED_STYLE);
            this.state = UNCHECKED;
        }
        ValueChangeEvent.fireIfNotEqual(this, old, this.state);
    }

    public void onClick(ClickEvent event) {
    	if(!enabled)
    		return;
       if(type == CheckType.TWO_STATE){
           if(state == CHECKED)
               setState(UNCHECKED);
           else
               setState(CHECKED);
       }else{
           if(state == CHECKED) 
               setState(UNCHECKED);
           else if (state == UNCHECKED)
               setState(UNKNOWN);
           else
               setState(CHECKED);
       }
       addStyleName("Focus");
    }
    
    public void enable(boolean enabled){
       this.enabled = enabled;
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

	public void setField(Field<String> field) {
		this.field = field;
		addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}

	/*
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler, BlurEvent.getType());
	}
	
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return addDomHandler(handler, FocusEvent.getType());
	}

	*/


	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addDomHandler(handler,KeyDownEvent.getType());
	}

	public void onKeyDown(KeyDownEvent event) {
		if(event.getNativeKeyCode() == KeyboardHandler.KEY_ENTER) {
	    	if(!enabled)
	    		return;
	       if(type == CheckType.TWO_STATE){
	           if(state == CHECKED)
	               setState(UNCHECKED);
	           else
	               setState(CHECKED);
	       }else{
	           if(state == CHECKED) 
	               setState(UNCHECKED);
	           else if (state == UNCHECKED)
	               setState(UNKNOWN);
	           else
	               setState(CHECKED);
	       }
	       addStyleName("Focus");
		}
	}

	public void setQueryMode(boolean query) {
		// TODO Auto-generated method stub
		
	}

	public void checkValue() {
		field.checkValue(this);
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		if(field.queryString != null) {
			QueryData qd = new QueryData();
			qd.query = field.queryString;
			qd.key = key;
			qd.type = QueryData.Type.STRING;
			list.add(qd);
		}
		
	}

	public ArrayList<String> getErrors() {
		return field.errors;
	}

	public String getFieldValue() {
		return getFieldValue();
	}

	public void setFieldValue(String value) {
		field.setValue(value);
		setValue(value);
		
	}

}
