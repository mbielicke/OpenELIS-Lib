/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.common.data;

import java.util.ArrayList;
import java.util.HashSet;

import org.openelis.gwt.widget.table.TableDataRow;

public class DataFilterer implements DataFiltererInt {
    
    public Filter[] getFilterValues(ArrayList<TableDataRow> data,int col) {
        ArrayList filterVals = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            Object val = data.get(i).cells.get(col).getValue();
            if (val != null && !filterVals.contains(val))
                filterVals.add(val);
        }
        //Collections.sort(filterVals);
        Filter[] filters = new Filter[filterVals.size() + 1];
        Filter filter = new Filter();
        filter.filtered = true;
        filter.obj = "All";
        filters[0] = filter;
        for (int i = 1; i < filters.length; i++) {
            filter = new Filter();
            filter.filtered = false;
            filter.obj = filterVals.get(i - 1);
            filters[i] = filter;
        }
        return filters;
    }
    
    public void applyFilters(ArrayList<TableDataRow> data, ArrayList<Filter[]> filters) {
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
            TableDataRow row = data.get(i);
            row.shown = true;
            for (int j = 0; j < filterSets.size(); j++) {
                if (filterSets.get(j) == null)
                    continue;
                HashSet filterSet = (HashSet)filterSets.get(j);
                if (filterSet.contains("All"))
                    continue;
                String val = null;
                if (row.cells.get(j).getValue() != null)
                    val = row.cells.get(j).getValue().toString();
                if (!filterSet.contains(val))
                  data.get(i).shown = false;  
            }
        }
    }
    
    public void applyFilter(ArrayList<TableDataRow> data, Filter[] filters, int col) {
        if(filters == null){
            if(col == 0){
                for (int i = 0; i < data.size(); i++) 
                    data.get(i).shown = true;
            }
            return;
        }
        ArrayList filterSet = new ArrayList();
        //filterSet.add("");
        //filterSet.add(null);
        for (int j = 0; j < filters.length; j++) {
            if (filters[j].filtered) {
                filterSet.add(filters[j].obj);
            }
        }
        for (int i = 0; i < data.size(); i++) {
            TableDataRow row = data.get(i);
            if(col == 0)
                row.shown = true;
            if (filterSet.contains("All"))
                continue;
            if (row.cells.get(col).getValue() != null && !filterSet.contains(row.cells.get(col).getValue()))
              row.shown = false;  
        }
    }
    
    public void applyQueryFilter(ArrayList<TableDataRow> data, String query, int col) {
        if(query == null){
            if(col == 0){
                for (int i = 0; i < data.size(); i++) 
                    data.get(i).shown = true;
            }
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            TableDataRow row = data.get(i);
            if(col == 0)
                row.shown = true;
            if (row.cells.get(col).getValue() != null && !((String)row.cells.get(col).getValue()).toLowerCase().startsWith(query.toLowerCase()))
              row.shown = false;  
        }
    }

}
