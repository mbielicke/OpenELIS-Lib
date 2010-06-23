package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;

public class TableDataCell  {

	public Object value;
	public ArrayList<LocalizedException> exceptions;
	
	public TableDataCell() {
		value = null;
	}
	
	public TableDataCell(Object value) {
		this.value = value;
	}
	
	public void addException(LocalizedException exception) {
		if(exceptions == null)
			exceptions = new ArrayList<LocalizedException>();
		exceptions.add(exception);
	}
	
	public void clearExceptions() {
		exceptions = null;
	}
	
	public ArrayList<LocalizedException> getExceptions() {
		return exceptions;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
		
	}



}
