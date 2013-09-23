package org.openelis.gwt.widget.table;

import java.util.Comparator;

import org.openelis.gwt.widget.table.event.SortEvent.SortDirection;

public class ColumnComparator implements Comparator<TableDataRow> {

	private int col; 
	private SortDirection dir;
	
	public ColumnComparator(int col, SortDirection dir) {
		this.col = col;
		this.dir = dir;
		
	}
	
	@SuppressWarnings("unchecked")
	public int compare(TableDataRow row1, TableDataRow row2) {
		if(dir == SortDirection.ASCENDING) {
			if(row1.cells.get(col).getValue() != null){
				if(row2.cells.get(col).getValue() == null)
					return -1;
				return ((Comparable)row1.cells.get(col).getValue()).compareTo(row2.cells.get(col).getValue());
			}else if(row2.getCells().get(col) == null)
			    return 0;
			else
				return 1;
		}else {
			if(row2.cells.get(col).getValue() != null) {
				if(row1.cells.get(col).getValue() == null)
					return 1;
				return ((Comparable)row2.cells.get(col).getValue()).compareTo(row1.cells.get(col).getValue());
			}else if(row1.cells.get(col).getValue() == null)
			    return 0;
			else
				return -1;
		}
	}

}
