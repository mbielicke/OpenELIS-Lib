package org.openelis.gwt.widget.table;

import java.util.ArrayList;


public class TableDataCell  {

	public Object value;
	public ArrayList<Exception> exceptions;
	public String style;
	
	public TableDataCell() {
		value = null;
	}
	
	public TableDataCell(Object value) {
		this.value = value;
	}
	
	public void addException(Exception exception) {
		if(exceptions == null)
			exceptions = new ArrayList<Exception>();
		exceptions.add(exception);
	}
	
	public void clearExceptions() {
		exceptions = null;
	}
	
	public ArrayList<Exception> getExceptions() {
		return exceptions;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		
	}



}
