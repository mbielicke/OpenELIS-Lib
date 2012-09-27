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
package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

/**
 * This interface is implemented by classes that will provide editing functionality to Table cells
 * @author tschmidt
 *
 * @param <T>
 */
public interface CellEditor {    
    
    /**
     * Returns the widget used as the editor for this cell
     * @return
     */
    @SuppressWarnings("rawtypes")
	public void startEditing(Object value, Container container, GwtEvent event);
    
    /**
     * Returns the widget used for querying this cell
     * @param qd
     * @return
     */
    @SuppressWarnings("rawtypes")
	public void startEditingQuery(QueryData qd, Container container, GwtEvent event); 
    
    /**
     * Pulls the edited value from the editor and returns it.  If in Query mode and QueryData object will be returned 
     * @return
     */
    public Object finishEditing();
    
    /**
     * Returns whether the passed key should be ignored by the KeyHandler when editing;
     * @param keyCode
     * @return
     */
    public boolean ignoreKey(int keyCode);
        
    public Widget getWidget();
    
    public void setColumn(ColumnInt col);

}
