package org.openelis.gwt.common;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.TableDataModel;
import org.openelis.gwt.common.data.TableDataRow;

import java.util.ArrayList;

public class Query<QRow extends TableDataRow> implements RPC {
    
    public ArrayList<AbstractField> fields;
    public TableDataModel<QRow> results; 
    
    public int page = 0;
    

}
