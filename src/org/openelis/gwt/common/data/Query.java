package org.openelis.gwt.common.data;

import java.util.ArrayList;

import org.openelis.gwt.common.RPC;
import org.openelis.gwt.widget.table.TableDataRow;

/**
 * Class used to transfer query fields and query results from screen to 
 * entity bean.
 */
public class Query<T extends RPC> implements RPC {
    
	private static final long serialVersionUID = 1L;
	
	protected ArrayList<QueryData> fields;
    protected ArrayList<T> results; 
    protected transient ArrayList<TableDataRow> model;
    
    public int page = 0;
    
    public Query(){
    }

    /**
     * Gets/Sets the query fields
     */
    public ArrayList<QueryData> getFields() {
        return fields;
    }

    public void setFields(QueryData field) {
        ArrayList<QueryData> fields = new ArrayList<QueryData>();
        
        fields.add(field);
        setFields(fields);
    }

    public void setFields(ArrayList<QueryData> fields) {
        this.fields = fields;
    }

    /**
     * Gets/Sets the result list
     */
    public ArrayList<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }
    
    /**
     * Gets/Sets the model list
     */
    public ArrayList<TableDataRow> getModel() {
        return model;
    }

    public void setModel(ArrayList<TableDataRow> model) {
        this.model = model;
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
