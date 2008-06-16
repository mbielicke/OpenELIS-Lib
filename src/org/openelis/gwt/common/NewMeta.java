package org.openelis.gwt.common;

public interface NewMeta {

	public String[] getColumnList();
	
	public String getEntity();
	
	public boolean hasColumn(String columnName);
}
