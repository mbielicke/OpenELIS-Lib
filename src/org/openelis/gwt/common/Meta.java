package org.openelis.gwt.common;

public interface Meta {

	public String[] getColumnList();
	
	public String getEntity();
	
	public boolean hasColumn(String columnName);
}
