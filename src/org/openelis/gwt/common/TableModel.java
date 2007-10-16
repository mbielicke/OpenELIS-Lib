package org.openelis.gwt.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class TableModel implements Serializable {
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.TableRow>
     */
    private ArrayList rows = new ArrayList();
    public boolean paged;
    public int totalPages;
    public int rowsPerPage;
    private boolean sortDown;
    public int pageIndex;
    public int totalRows;
    public int shown;
    public boolean showIndex;
    public boolean autoAdd;

    private AbstractField[] fields;
    
    /**
     * @gwt.typeArgs <java.lang.String, org.openelis.gwt.common.AbstractField>
     */
    public HashMap hidden = new HashMap();

    public void addRow(TableRow row) {
        if(row == null)
            row = createRow();
        rows.add(row);
    }

    public void deleteRow(int row) {
        rows.remove(row);
    }

    public TableRow getRow(int row) {
        return (TableRow)rows.get(row);
    }

    public int numRows() {
        return rows.size();
    }

    public AbstractField getFieldAt(int row, int col) {
        return (AbstractField)((TableRow)rows.get(row)).getColumn(col);
    }

    public void insertRow(int index, TableRow row) {
        if(row == null)
            row = createRow();
        rows.add(index, row);
    }

    public Filter[] getFilterValues(int col) {
        ArrayList filterVals = new ArrayList();
        for (int i = 0; i < rows.size(); i++) {
            Object val = getFieldAt(i, col).getValue();
            if (val != null && !filterVals.contains(val))
                filterVals.add(val);
        }
        Collections.sort(filterVals);
        Filter[] filters = new Filter[filterVals.size() + 1];
        Filter filter = new Filter();
        filter.filtered = true;
        filter.value = "All";
        filters[0] = filter;
        for (int i = 1; i < filters.length; i++) {
            filter = new Filter();
            filter.filtered = false;
            filter.value = (String)filterVals.get(i - 1);
            filters[i] = filter;
        }
        return filters;
    }

    public void sort(int col, boolean down) {
        sortDown = down;
        quicksort(0, rows.size() - 1, col);
    }

    public boolean compare(AbstractField field1, AbstractField field2) {
        return field1.toString().toLowerCase().compareTo(field2.toString()
                                                               .toLowerCase()) < 0;
    }

    public void applyFilters(ArrayList filters) {
        ArrayList filterSets = new ArrayList();
        ArrayList splitOn = new ArrayList();
        for (int i = 0; i < filters.size(); i++) {
            if (filters.get(i) == null) {
                filterSets.add(null);
                splitOn.add(null);
                continue;
            }
            Filter[] filter = (Filter[])filters.get(i);
            if (filter[1].splitOn != null)
                splitOn.add(filter[1].splitOn);
            else
                splitOn.add(null);
            HashSet filterSet = new HashSet();
            filterSet.add("");
            filterSet.add(null);
            for (int j = 0; j < filter.length; j++) {
                if (filter[j].filtered) {
                    filterSet.add(filter[j].value);
                }
            }
            filterSets.add(filterSet);
        }
        for (int i = 0; i < rows.size(); i++) {
            TableRow row = (TableRow)rows.get(i);
            row.setShow(true);
            for (int j = 0; j < filterSets.size(); j++) {
                if (filterSets.get(j) == null)
                    continue;
                HashSet filterSet = (HashSet)filterSets.get(j);
                if (filterSet.contains("All"))
                    continue;
                String val = null;
                if (row.getColumn(j).getValue() != null)
                    val = row.getColumn(j).getValue().toString();
                if (splitOn.get(j) != null && val != null)
                    val = val.split((String)splitOn.get(j))[0].trim();
                if (!filterSet.contains(val))
                    row.setShow(false);
            }
        }
    }

    public void quicksort(int p, int r, int col) {
        if (p < r) {
            int q = partition(p, r, col);
            quicksort(p, q, col);
            quicksort(q + 1, r, col);
        }
    }

    public int partition(int s, int t, int col) {
        String x = ((TableRow)rows.get(s)).getColumn(col).getValue().toString();
        int i = s - 1;
        int j = t + 1;
        while (true) {
            if (sortDown) {
                while (((TableRow)rows.get(--j)).getColumn(col)
                                                .getValue()
                                                .toString()
                                                .compareTo(x) > 0)
                    ;
                while (((TableRow)rows.get(++i)).getColumn(col)
                                                .getValue()
                                                .toString()
                                                .compareTo(x) < 0)
                    ;
            } else {
                while (((TableRow)rows.get(--j)).getColumn(col)
                                                .getValue()
                                                .toString()
                                                .compareTo(x) < 0)
                    ;
                while (((TableRow)rows.get(++i)).getColumn(col)
                                                .getValue()
                                                .toString()
                                                .compareTo(x) > 0)
                    ;
            }
            if (i < j) {
                rows.set(j, rows.set(i, rows.get(j)));
            } else {
                return j;
            }
        }
    }
    
    public void setFields(AbstractField[] fields){
        this.fields = fields;
    }
    
    public AbstractField[] getFields(){
        return fields;
    }
    
    public TableRow createRow() {
        TableRow row = new TableRow();
        for (int i = 0; i < fields.length; i++) {
            row.addColumn((AbstractField)fields[i].getInstance());
        }
        return row;
    }
    
    public void reset() {
        rows = new ArrayList();
        hidden = new HashMap();
        totalRows = 0;
        shown = 0;
    }
}
