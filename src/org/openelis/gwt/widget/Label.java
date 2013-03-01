package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.ui.common.data.QueryData;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class Label<T> extends com.google.gwt.user.client.ui.Label implements HasValue<String>, HasField<T> {
    
    String value;
    Field<T> field;
    
    public Label() {
    	super();
    }
    
    public Label(String text) {
    	super(text);
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		setValue(value,false);
	}
	

	public void setValue(String value, boolean fireEvents) {
		String old = this.value;
		this.value = value;
		setText(value);
		if(fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, old, value);
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}

	public void addException(Exception exception) {
		// TODO Auto-generated method stub
		
	}

	public void checkValue() {
		if(field != null)
			field.checkValue(this);
		
	}

	public void clearExceptions() {
		// TODO Auto-generated method stub
		
	}

	public void enable(boolean enabled) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Exception> getExceptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public Field<T> getField() {
		// TODO Auto-generated method stub
		return field;
	}

	public T getFieldValue() {
	    return field.getValue();    
	}

	public void getQuery(ArrayList<QueryData> list, String key) {
		// TODO Auto-generated method stub
		
	}

	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setField(Field<T> field) {
		this.field = field;
		addMouseOverHandler(field);
		addMouseOutHandler(field);
		
	}

	public void setFieldValue(T value) {
  		field.setValue(value);
   		setValue(field.format());
	}

	public void setQueryMode(boolean query) {
		
	}
	

	public void addExceptionStyle(String style) {
		addStyleName(style);
		
	}

	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<T> handler) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getWidgetValue() {
		return getText();
	}

	public void removeExceptionStyle(String style) {
		removeStyleName(style);
	}

}
