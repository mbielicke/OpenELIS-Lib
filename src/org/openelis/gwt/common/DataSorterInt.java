package org.openelis.gwt.common;

import org.openelis.gwt.common.data.DataModel;

public interface DataSorterInt {
    
    public enum SortDirection{UP,DOWN};
    
    public void sort(DataModel data, int col, SortDirection direction);

}
