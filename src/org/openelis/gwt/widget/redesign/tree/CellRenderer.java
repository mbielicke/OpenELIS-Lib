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
package org.openelis.gwt.widget.redesign.tree;

import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * This interface is implemented by classes the provide rendering functionality for Table cells
 * @author tschmidt
 *
 * @param <T>
 */
public interface CellRenderer<T> {

    /**
     * Formats the value passed and places the text into the cell passed
     * @param table
     * @param flexTable
     * @param row
     * @param col
     * @param value
     */
    public void render(Tree tree, FlexTable flexTable,int row,int col, T value);
    
    /**
     * Formats the QueryData passed and places the text into the cell passed
     * @param table
     * @param flexTable
     * @param row
     * @param col
     * @param qd
     */
    public void renderQuery(Tree tree, FlexTable flexTable, int row, int col, QueryData qd);
    
}
