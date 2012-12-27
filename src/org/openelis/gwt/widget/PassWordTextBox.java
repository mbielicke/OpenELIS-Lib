package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class PassWordTextBox extends PasswordTextBox implements HasField<String> {

	private Field<String> field; 
	private boolean enabled;
	
	public void addException(LocalizedException error) {
		field.addException(error);
		field.drawExceptions(this);
	}

	public void clearExceptions() {
		field.clearExceptions(this);
	}

	public Field<String> getField() {
		return field;
	}

	public void setField(Field<String> field) {
		this.field = field;
		addValueChangeHandler(field);
		addBlurHandler(field);
		addMouseOutHandler(field);
		addMouseOverHandler(field);
	}
	
	public String getFieldValue() {
		return field.getValue();
	}
	
	public void addTabHandler(TabHandler handler) {
		addDomHandler(handler,KeyPressEvent.getType());
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

	public ArrayList<LocalizedException> getExceptions() {
		return field.exceptions;
	}

	public void enable(boolean enabled) {
		this.enabled = enabled;
		setReadOnly(!enabled);
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setFieldValue(String value) {
		field.setValue(value);
		setText(value);	
	}
	
	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return field.addValueChangeHandler(handler);
	}

	public void addExceptionStyle(String style) {
		addStyleName(style);
	}

	public Object getWidgetValue() {
		return getText();
	}

	public void removeExceptionStyle(String style) {
		removeStyleName(style);
	}

}
