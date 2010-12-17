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

import java.util.ArrayList;

import org.openelis.gwt.common.RPC;

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
        if (fields == null)
            fields = new ArrayList<QueryData>(1);

        fields.add(field);
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
