package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;

import org.openelis.gwt.common.rewrite.QueryData;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.widget.HasField;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.user.client.ui.PasswordTextBox;

public class PassWordTextBox extends PasswordTextBox implements HasField<String> {

	private Field<String> field; 
	private boolean enabled;
	
	public void addError(String error) {
		field.addError(error);
		field.drawError(this);
	}

	public void clearErrors() {
		field.clearError(this);
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
	
	public void addTabHandler(UIUtil.TabHandler handler) {
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

}
