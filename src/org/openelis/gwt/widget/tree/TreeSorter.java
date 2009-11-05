package org.openelis.gwt.widget.tree;

import java.util.ArrayList;
import java.util.Iterator;

import org.openelis.gwt.widget.table.TableSorter.SortDirection;

public class TreeSorter {

	ArrayList<TreeDataItem> data;
	SortDirection direction;
	
 	public void sort(ArrayList<TreeDataItem> data, ArrayList<String> leafs, int col, SortDirection direction) {
        this.direction = direction;
        for(String leaf : leafs){
        	if(leaf.equals("model")){
        		this.data = data;
        		quicksort(0,data.size() -1, col);
        	}else {
        		Iterator<TreeDataItem> it = data.iterator();
                while(it.hasNext())
                    checkChildItems(it.next(),leaf, col);
        	}
        	
        }
        	
    }
 	
    private void checkChildItems(TreeDataItem item, String leaf, int col){
    	if(item.leafType.equals(leaf)){
    		this.data = item.getItems();
    		quicksort(0, data.size()-1, col);
    	}
        Iterator<TreeDataItem> it = item.getItems().iterator();   
        while(it.hasNext())
            checkChildItems(it.next(),leaf,col);
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
