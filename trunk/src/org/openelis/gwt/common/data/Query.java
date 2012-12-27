package org.openelis.gwt.common.data;

import java.util.ArrayList;

import org.openelis.gwt.common.RPC;

/**
 * Class used to transfer query fields and the page number to the query service.
 */
public class Query implements RPC {

    private static final long      serialVersionUID = 1L;

    protected int                  page, rowsPerPage;
    protected ArrayList<QueryData> fields;

    public Query() {
        page = 0;
        rowsPerPage = 20;
    }

    /**
     * Gets/Sets the query fields
     */
    public ArrayList<QueryData> getFields() {
        return fields;
    }

    public void setFields(QueryData field) {
        if (fields == null)
            fields = new ArrayList<QueryData>(1);

        fields.add(field);
    }

    public void setFields(ArrayList<QueryData> fields) {
        this.fields = fields;
    }

    /**
     * Gets/Sets the number of rows that we want to search for in a page
     */
    public int getRowsPerPage() {
        return rowsPerPage;
    }
    
    public void setRowsPerPage(int rows) {
        rowsPerPage = rows;
    }

    /**
     * Gets/Sets the current page number for result lists that are paged
     */
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
