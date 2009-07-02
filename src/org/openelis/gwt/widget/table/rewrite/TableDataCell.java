package org.openelis.gwt.widget.table.rewrite;

import java.util.ArrayList;

public class TableDataCell {

	public Object value;
	public ArrayList<String> errors;
	
	public TableDataCell() {
		value = null;
	}
	
	public TableDataCell(Object value) {
		this.value = value;
	}
	
	public void addError(String error) {
		if(errors == null)
			errors = new ArrayList<String>();
		errors.add(error);
	}
	
	public void clearErrors() {
		errors = null;
	}
	
	public ArrayList<String> getErrors() {
		return errors;
	}
}
