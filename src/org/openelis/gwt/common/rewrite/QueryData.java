package org.openelis.gwt.common.rewrite;

import java.io.Serializable;

public class QueryData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public enum Type {STRING,INTEGER,DOUBLE,DATE}
	public Type type;
	public String query;
	public String key;
	

}
