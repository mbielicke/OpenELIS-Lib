package org.openelis.util;

public interface Meta {

	public String[] getColumnList();

	public String getTable();
	
	public String getEntity();
	
	//this boolean tells the querybuilder whether to put the table inside the from statement or not.
	//Jboss will handle this for us if it is mapped in the entity and isnt a collection.
	public boolean includeInFrom();
	
	public boolean hasColumn(String columnName);
}
