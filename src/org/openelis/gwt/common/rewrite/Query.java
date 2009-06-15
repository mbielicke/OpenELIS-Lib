package org.openelis.gwt.common.rewrite;

import java.util.ArrayList;

import org.openelis.gwt.common.RPC;
import org.openelis.gwt.common.data.QueryField;
import org.openelis.gwt.common.rewrite.data.TableDataRow;

public class Query<T> implements RPC {
    
	private static final long serialVersionUID = 1L;
	
	public ArrayList<QueryField> fields;
    public ArrayList<T> results; 
    public transient ArrayList<TableDataRow> model;
    
    public int page = 0;
    
    public Query(){
    	
    }
    

}
