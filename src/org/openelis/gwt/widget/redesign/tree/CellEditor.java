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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * This interface is implemented by classes that will provide editing functionality to Table cells
 * @author tschmidt
 *
 * @param <T>
 */
public interface CellEditor<T> {
   
    /**
     * Sets the passed value in the cell's editor and places the editor into the flexTable for editing
     * @param table
     * @param flexTable
     * @param row
     * @param col
     * @param value
     * @param event
     */
    @SuppressWarnings("unchecked")
    public void startEditing(Tree tree, FlexTable flexTable, int row, int col, T value, GwtEvent event);
    
    /**
     * Puts the cells editor into Query mode and sets the passed QueryData as the value and places the editor
     * into the flexTable for query entry
     * @param table
     * @param flexTable
     * @param row
     * @param col
     * @param query
     * @param event
     */
    @SuppressWarnings("unchecked")
    public void startQueryEditing(Tree tree, FlexTable flexTable, int row, int col, QueryData query, GwtEvent event);
    
    /**
     * Pulls the edited value from the editor and returns it.  If in Query mode and QueryData object will be returned 
     * @param table
     * @param flexTable
     * @param row
     * @param col
     * @return
     */
    public Object finishEditing(Tree tree, FlexTable flexTable, int row, int col);
        
    /**
     * Returns the widget used as the editor for this cell
     * @return
     */
    public Widget getWidget();
}
