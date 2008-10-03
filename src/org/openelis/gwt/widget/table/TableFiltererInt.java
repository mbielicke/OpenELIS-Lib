package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.DataModel;

import java.util.ArrayList;

public interface TableFiltererInt {
    
    public Filter[] getFilterValues(DataModel data,int col);
    
    public void applyFilters(DataModel data, ArrayList filters);

}
