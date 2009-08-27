package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.HasField;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public class RadioButton extends com.google.gwt.user.client.ui.RadioButton implements HasField{
	
	public boolean enabled;

	public RadioButton(String name) {
		super(name);
	}

	private Field field; 
	
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
	
	public void addTabHandler(UIUtil.TabHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		
	}

	public void checkValue() {
		field.checkValue(this);
		
	}

	public void getQuery(ArrayList list, String key) {
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
}
