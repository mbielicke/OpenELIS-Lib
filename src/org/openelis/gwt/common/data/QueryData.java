/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.common.data;

import java.io.Serializable;

/**
 * This class is used to pass Query strings for all data types that a query can be done
 * on between the GWT client and the back end server.
 */
public class QueryData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enum declaring the data type for this query
	 */
	public enum Type {STRING,INTEGER,DOUBLE,DATE}
	public enum Logical {AND,OR}
	
	protected Type type;
	protected String query;
	protected String key;
	protected Logical logical;
	
	/**
	 * No arg constructor
	 */
	public QueryData() {
		setLogical(Logical.AND);
	}
	
	/**
	 * Constructor to initialize QD Object with type and query params set.
	 * 
	 * @param type - enum that indicates type of data for the column
	 * @param query - the query string entered by the user
	 */
	public QueryData(Type type, String query) {
		this();
		setType(type);
		setQuery(query);
	}
	
	/**
	 * Constructor to initial QD object with Key, Type and query string. 
	 *
	 * @param key - string representing the column key
	 * @param type - enum that indicates the type of data for the column
	 * @param query - the query string entered by the user
	 */
	public QueryData(String key, Type type, String query) {
		this(type,query);
		setKey(key);
	}
	
	/**
	 * Sets the type of data this query parameter is used for
	 * 
	 * @param type - enum that indicates type of data for the column
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * Gets the enum that represents the type of data for this query param.
	 * 
	 * @return - Type enum representing data type of column
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * Gets the enum that for the logical glue how this parameter should be added to the query.

	 * @return - Logical enum that returns AND by defualt
	 */
	public Logical getLogical() {
		return logical;
	}
	
	/**
	 * Sets the enum that is used for the logical glue for how this parameter is added to the query
	 * 
	 * @param logical - Logical enum {AND,OR}
	 */
	public void setLogical(Logical logical) {
		this.logical = logical;
	}
	
	/**
	 * Sets the query string entered by the user for this parameter.
	 * 
	 * @param query - string entered by user
	 */
	public void setQuery(String query) {
		if(query.startsWith("|")) {
			setLogical(Logical.OR);
			this.query = query.substring(1);
		}else
			this.query = query;
	}
	
	/**
	 * Gets the query string entered by the user for this parameter.
	 * 
	 * @return - string the user entered for this query parameter
	 */
	public String getQuery() {
		return query;
	}
	
	/**
	 * Sets the key used to identify a column in the query.
	 * 
	 * @param key - string identifier for column in a query
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Gets the key used to identify a column in the query.
	 * 
	 * @return - string identifying a column in a query.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Convenience method to inspect the contents of query parameter.
	 * 
	 * @return - string representing contents
	 */
	public String toString() {
	    return key+"("+type.toString() + ") = " + query;
	}
	
}
