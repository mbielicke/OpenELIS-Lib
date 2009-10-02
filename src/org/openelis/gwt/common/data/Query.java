package org.openelis.gwt.common.data;

import java.util.ArrayList;

import org.openelis.gwt.common.RPC;
import org.openelis.gwt.widget.table.TableDataRow;

/**
 * Class used to transfer query fields and the page number to the query service.
 */
public class Query implements RPC {

    private static final long      serialVersionUID = 1L;

    protected int                  page;
    protected ArrayList<QueryData> fields;

    public Query() {
        page = 0;
    }

    /**
     * Gets/Sets the query fields
     */
    public ArrayList<QueryData> getFields() {
        return fields;
    }

    public void setFields(QueryData field) {
        ArrayList<QueryData> fields = new ArrayList<QueryData>(1);

        fields.add(field);
        setFields(fields);
    }

    public void setFields(ArrayList<QueryData> fields) {
        this.fields = fields;
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
