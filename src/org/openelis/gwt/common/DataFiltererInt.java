package org.openelis.gwt.common;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.DataModel;

import java.util.ArrayList;

public interface DataFiltererInt {
    
    public Filter[] getFilterValues(DataModel data,int col);
    
    public void applyFilters(DataModel data, ArrayList<Filter[]> filters);
    
    public void applyFilter(DataModel data, Filter[] filter, int col);

}
