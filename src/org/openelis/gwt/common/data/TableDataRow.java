package org.openelis.gwt.common.data;

import org.openelis.gwt.common.RPC;

import java.util.Arrays;
import java.util.List;


public class TableDataRow<Key> implements RPC,FieldType {
    
    private static final long serialVersionUID = 1L;
    
    public Key key;
    public FieldType[] cells;
    public FieldType data;
    

    /**
     * Flag letting Widgets know if this DataSet should be shown on screen or 
     * if it is currently hidden.  Used when filtering.
     */
    public boolean shown = true;
    
    /**
     * Flag letting Widgets know if this DataSet is enabled and is available for
     * selection by users.
     */
    public boolean enabled = true;
    
    /**
     * Default constructor
     *
     */
    
    public TableDataRow() {
        this(null);
    }
    
    public TableDataRow(Key key) {
        this.key = key;
        cells = new FieldType[] {};
    }
    
    public TableDataRow(int size) {
        cells = new FieldType[size];
    }
    
    public TableDataRow(Key key, FieldType field) {
        this(1);
        this.key = key;
        cells[0] = field;
    }
    
    public TableDataRow(Key key, FieldType[] fields){
        this.key = key;
        cells = fields;
    }
    
    public List<FieldType> getCells() {
        return Arrays.asList(cells);
    }

    public Object clone() {
        TableDataRow<Key> clone = new TableDataRow<Key>(cells.length);
        clone.key = key;
        for(int i = 0; i < cells.length; i++)
            clone.cells[i] = (FieldType)cells[i].clone();
        if(data!=null)
            clone.data = (FieldType)data.clone();
        return clone;
    }
    
    public int size() {
        return cells.length;
    }
    
    public void setData(FieldType data) {
        this.data = data;
    }
    
    public FieldType getData() {
        return data;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object obj) {
        // TODO Auto-generated method stub
        
    }

}
