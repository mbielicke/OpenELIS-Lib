package org.openelis.gwt.widget.table;

import java.util.ArrayList;

public class TableSorter implements TableSorterInt {

	ArrayList<TableDataRow> data;
	SortDirection direction;
	
 	public void sort(ArrayList<TableDataRow> data, int col, SortDirection direction) {
        this.data = data;
        this.direction = direction;
        quicksort(0, data.size() - 1, col);
    }

    public void quicksort(int p, int r, int col) {
        if (p < r) {
            int q = partition(p, r, col);
            quicksort(p, q, col);
            quicksort(q + 1, r, col);
        }
    }

    @SuppressWarnings("unchecked")
	public int partition(int s, int t, int col) {
        Comparable x = (Comparable)data.get(s).cells.get(col).getValue();
        int i = s - 1;
        int j = t + 1;
        while (true) {
        	if(x != null){
        		if (direction == SortDirection.DOWN) {
        			while (data.get(--j).cells.get(col).getValue() == null || x.compareTo(data.get(j).cells.get(col).getValue()) < 0)
        				;
        			while (data.get(++i).cells.get(col).getValue() == null || x.compareTo(data.get(i).cells.get(col).getValue()) > 0)
        				;
        		} else {
        			while (data.get(--j).cells.get(col).getValue() == null || x.compareTo(data.get(j).cells.get(col).getValue()) > 0)
        				;
        			while (data.get(++i).cells.get(col).getValue() == null || x.compareTo(data.get(i).cells.get(col).getValue()) < 0)
        				;
        		}
        	}else{
        		int k = j;
        		while(data.get(--k).cells.get(col).getValue() == null);
        		i++;
        		if( i > k)
        			return i;
        		else {
        			 j = k;
        			 data.set(k, data.set(i, data.get(k)));
        			 continue;
        		}
        	}
            if (i < j) {
                data.set(j, data.set(i, data.get(j)));
            } else {
                return j;
            }
        }
    }

}
