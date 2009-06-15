package org.openelis.gwt.common.rewrite.data;

import java.util.Arrays;
import java.util.List;


public class TableDataRow {
    
    private static final long serialVersionUID = 1L;
    
    public Object[] cells;
    public Object key;
    public Object data;
    

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
    
    public TableDataRow(int size) {
        cells = new Object[size];
    }
    
    public TableDataRow(Object key, Object display) {
    	this.key = key;
    	cells = new Object[1];
    	cells[0] = display;
    }
    
    
    public List getCells() {
        return Arrays.asList(cells);
    }

    public Object clone() {
        TableDataRow clone = new TableDataRow(cells.length);
        for(int i = 0; i < cells.length; i++)
            clone.cells[i] = cells[i];
        return clone;
    }
    
    public int size() {
        return cells.length;
    }

}
