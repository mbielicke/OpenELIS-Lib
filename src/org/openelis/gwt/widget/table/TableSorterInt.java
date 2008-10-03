package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.data.DataModel;

public interface TableSorterInt {
    
    public enum SortDirection{UP,DOWN};
    
    public void sort(DataModel data, int col, SortDirection direction);

}
