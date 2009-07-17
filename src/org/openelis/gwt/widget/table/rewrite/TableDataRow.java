package org.openelis.gwt.widget.table.rewrite;

import java.util.ArrayList;


public class TableDataRow {
    
    private static final long serialVersionUID = 1L;
    
    public ArrayList<TableDataCell> cells;
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
    
    public TableDataRow() {
    	cells = new ArrayList<TableDataCell>();
    }
    
    public TableDataRow(int size) {
        cells = new ArrayList<TableDataCell>(size);
        for(int i = 0; i < size; i++) {
        	cells.add(new TableDataCell());
        }
    }
    
    public TableDataRow(Object key, Object display) {
    	this.key = key;
    	cells = new ArrayList<TableDataCell>(1);
    	cells.add(new TableDataCell(display));
    }

    public Object clone() {
        TableDataRow clone = new TableDataRow(cells.size());
        for(int i = 0; i < cells.size(); i++)
            clone.cells.get(i).setValue(cells.get(i).getValue());
        return clone;
    }
    
    public int size() {
        return cells.size();
    }
    
    public ArrayList<Object> getCells() {
    	ArrayList<Object> list = new ArrayList<Object>();
    	for(TableDataCell tdc : cells) {
    		list.add(tdc.getValue());
    	}
    	return list;
    }

}
