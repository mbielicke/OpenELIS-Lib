package org.openelis.gwt.common.deprecated;

import org.openelis.gwt.common.RPC;
import org.openelis.gwt.common.data.deprecated.AbstractField;
import org.openelis.gwt.common.data.deprecated.TableDataModel;
import org.openelis.gwt.common.data.deprecated.TableDataRow;

import java.util.ArrayList;

@Deprecated
public class Query<QRow extends TableDataRow> implements RPC {
    
	private static final long serialVersionUID = 1L;
	
	public ArrayList<AbstractField> fields;
    public TableDataModel<QRow> results; 
    
    public int page = 0;
    
    public Query(){
    	
    }
    

}
