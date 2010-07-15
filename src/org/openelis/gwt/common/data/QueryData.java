package org.openelis.gwt.common.data;

import org.openelis.gwt.common.RPC;


public class QueryData implements RPC {
	
	private static final long serialVersionUID = 1L;
	public enum Type {STRING,INTEGER,DOUBLE,DATE}
	public Type type;
	public String query;
	public String key;
	
	public String toString() {
	    return key+"("+type.toString() + ") = " + query;
	}

}
