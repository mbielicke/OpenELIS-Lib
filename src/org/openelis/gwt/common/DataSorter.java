package org.openelis.gwt.common;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;

public class DataSorter implements DataSorterInt {
    
    DataModel data;
    SortDirection direction;
    
    public void sort(DataModel data, int col, SortDirection direction) {
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

    public int partition(int s, int t, int col) {
        DataObject x = data.get(s).get(col);
        int i = s - 1;
        int j = t + 1;
        while (true) {
            if (direction == SortDirection.DOWN) {
                while (data.get(--j).get(col).compareTo(x) > 0)
                    ;
                while (data.get(++i).get(col).compareTo(x) < 0)
                    ;
            } else {
                while (data.get(--j).get(col).compareTo(x) < 0)
                    ;
                while (data.get(++i).get(col).compareTo(x) > 0)
                    ;
            }
            if (i < j) {
                data.set(j, data.set(i, data.get(j)));
            } else {
                return j;
            }
        }
    }

}
