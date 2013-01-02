/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.common.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class used to transfer query fields and the page number to the query service.
 */
public class Query implements Serializable {

    private static final long      serialVersionUID = 1L;

    protected int                  page,rowsPerPage;;
    protected ArrayList<QueryData> fields;

    public Query() {
        page = 0;
        rowsPerPage = 20;
    }

    /**
     * Gets the query fields.
     * 
     * @return list of QueryData to create query
     */
    public ArrayList<QueryData> getFields() {
        return fields;
    }

    /**
     * Adds a new QueryData parameter to the Query.
     * 
     * @param field query parameter to ad  to query
     */
    public void setFields(QueryData field) {
        if (fields == null)
            fields = new ArrayList<QueryData>(1);

        fields.add(field);
    }

    /**
     * Sets the list of query parameters used to build a query.
     * 
     * @param fields list of query parameters to make the query
     */
    public void setFields(ArrayList<QueryData> fields) {
        this.fields = fields;
    }
    
    /**
     * Gets the number of rows that we want to search for in a page
     * 
     * @return number of rows to be returned for each execution of the query
     */
    public int getRowsPerPage() {
        return rowsPerPage;
    }
    
    /**
     * Sets the number of rows to return when executing the query.
     * 
     * @param rows - number {@literal >} 1 seting max rows to return per query
     * @throws IndexOutOfBoundsException if rows {@literal <} 1 
     */
    public void setRowsPerPage(int rows) {
    	if(rows < 1)
    		throw new IndexOutOfBoundsException("Rows per page must >= 1");
    	
        rowsPerPage = rows;
    }

    /**
     * Gets the current page number for result lists that are paged
     * 
     * @return the current cursor for the page to fetch
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the the current page number to be fetched
     * 
     * @param page positive number indicating page index
     */
    public void setPage(int page) {
    	if(page < 0)
    		throw new IndexOutOfBoundsException("Page must be positive");
    	
        this.page = page;
    }
}
