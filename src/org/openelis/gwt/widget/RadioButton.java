package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.ui.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class RadioButton extends com.google.gwt.user.client.ui.RadioButton implements HasField{
	
	public boolean enabled;

	public RadioButton(String name) {
		super(name);
	}

	private Field field; 
	
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

	public void setField(Field field) {
		this.field = field;
		addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}
	
	public void addTabHandler(TabHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		
	}

	public void checkValue() {
		field.checkValue(this);
		
	}

	public void getQuery(ArrayList list, String key) {
		if(!field.queryMode)
			return;
		if(field.queryString != null) {
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

	public void enable(boolean enabled) {
		this.enabled = enabled;
		super.setEnabled(enabled);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public Object getFieldValue() {
		return getFieldValue();
	}

	public void setFieldValue(Object value) {
		field.setValue(value);
	}
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler handler) {
		return field.addValueChangeHandler(handler);
	}

	public void addExceptionStyle(String style) {
		addStyleName(style);
		
	}

	public Object getWidgetValue() {
		return getValue();
	}

	public void removeExceptionStyle(String style) {
		removeStyleName(style);
	}
}
