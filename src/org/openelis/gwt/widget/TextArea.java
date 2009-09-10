package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;
import org.openelis.gwt.screen.UIUtil;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;


public class TextArea extends com.google.gwt.user.client.ui.TextArea implements HasField<String> {

	private Field<String> field;
	private boolean enabled;
	
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
	
	public void addTabHandler(TabHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
	}

	public void setQueryMode(boolean query) {
		field.setQueryMode(query);
		
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

	public void enable(boolean enabled) {
		this.enabled = enabled;
		setReadOnly(!enabled);
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public String getFieldValue() {
		return field.getValue();
	}

	public void setFieldValue(String value) {
		field.setValue(value);
		setText(value);
	}
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return field.addValueChangeHandler(handler);
	}

}
