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

import org.openelis.gwt.common.RPC;

/**
 * This class is used to pass Query strings for all data types that a query can be done
 * on between the GWT client and the back end server.
 */
public class QueryData implements RPC {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Enum declaring the data type for this query
	 */
	public enum Type {STRING,INTEGER,DOUBLE,DATE}
	
	protected Type type;
	protected String query;
	protected String key;
	
	public QueryData() {
		
	}
	
	public QueryData(Type type, String query) {
		this.type = type;
		this.query = query;
	}
	
	public QueryData(String key, Type type, String query) {
		this(type,query);
		this.key = key;
	}
	
	public void setType(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public void setQuery(String query) {
		this.query = query;
	}
	
	public String getQuery() {
		return query;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public String toString() {
	    return key+"("+type.toString() + ") = " + query;
	}
	
}
