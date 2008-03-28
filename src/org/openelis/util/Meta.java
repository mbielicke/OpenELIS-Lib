package org.openelis.util;

public interface Meta {

	public String[] getColumnList();

	public String getTable();
	
	public String getEntity();
	
	//this boolean tells the querybuilder whether to put the table inside the from statement or not.
	//Jboss will handle this for us if it is mapped in the entity and isnt a collection.
	public boolean includeInFrom();
	
	//this boolean tells the query builder if it is a collection in the entity. If it is then we need
	//to use the IN() tag so we can access single columns inside a collection.  This is due to limitations of the
	//EJB query language.
	public boolean isCollection();
	
	public boolean hasColumn(String columnName);
}
