package org.openelis.gwt.common;

import org.openelis.gwt.common.Filter;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.StringObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class DataFilterer implements DataFiltererInt {
    
    public Filter[] getFilterValues(DataModel data,int col) {
        ArrayList<DataObject> filterVals = new ArrayList<DataObject>();
        for (int i = 0; i < data.size(); i++) {
            DataObject val = data.get(i).get(col);
            if (val != null && !filterVals.contains(val))
                filterVals.add(val);
        }
        Collections.sort(filterVals);
        Filter[] filters = new Filter[filterVals.size() + 1];
        Filter filter = new Filter();
        filter.filtered = true;
        filter.obj = new StringObject("All");
        filters[0] = filter;
        for (int i = 1; i < filters.length; i++) {
            filter = new Filter();
            filter.filtered = false;
            filter.obj = filterVals.get(i - 1);
            filters[i] = filter;
        }
        return filters;
    }
    
    public void applyFilters(DataModel data, ArrayList<Filter[]> filters) {
        ArrayList<HashSet> filterSets = new ArrayList<HashSet>();
        for (int i = 0; i < filters.size(); i++) {
            if (filters.get(i) == null) {
                filterSets.add(null);
                continue;
            }
            Filter[] filter = filters.get(i);
            HashSet filterSet = new HashSet();
            filterSet.add("");
            filterSet.add(null);
            for (int j = 0; j < filter.length; j++) {
                if (filter[j].filtered) {
                    filterSet.add(filter[j].obj);
                }
            }
            filterSets.add(filterSet);
        }
        for (int i = 0; i < data.size(); i++) {
            DataSet row = data.get(i);
            data.get(i).shown = true;
            for (int j = 0; j < filterSets.size(); j++) {
                if (filterSets.get(j) == null)
                    continue;
                HashSet filterSet = (HashSet)filterSets.get(j);
                if (filterSet.contains("All"))
                    continue;
                String val = null;
                if (row.get(j).getValue() != null)
                    val = row.get(j).getValue().toString();
                if (!filterSet.contains(val))
                  data.get(i).shown = false;  
            }
        }
    }
    
    public void applyFilter(DataModel data, Filter[] filters, int col) {
        if(filters == null){
            if(col == 0){
                for (int i = 0; i < data.size(); i++) 
                    data.get(i).shown = true;
            }
            return;
        }
        ArrayList<DataObject> filterSet = new ArrayList<DataObject>();
        //filterSet.add("");
        //filterSet.add(null);
        for (int j = 0; j < filters.length; j++) {
            if (filters[j].filtered) {
                filterSet.add(filters[j].obj);
            }
        }
        for (int i = 0; i < data.size(); i++) {
            DataSet row = data.get(i);
            if(col == 0)
                row.shown = true;
            if (filterSet.contains(new StringObject("All")))
                continue;
            if (row.get(col).getValue() != null && !filterSet.contains(row.get(col)))
              row.shown = false;  
        }
    }

}
