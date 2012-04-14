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

import java.util.ArrayList;

/**
 * This interface is for classes that will implement a Column Filter in the Table.
 */
public interface Filter {
   
    /**
     * Method called to determine if the Row should be included in the Filtered Model
     * @param value
     * @return
     */
    public boolean include(Object value);
    
    /**
     * Method called by Header to get the list of FilterChoices when displaying a FilterMenu to 
     * user
     * @param model
     * @return
     */
    public ArrayList<FilterChoice> getChoices(ArrayList<? extends Row> model);
    
    /**
     * Method used to set the column that this Filter should be applied to
     * @param column
     */
    public void setColumn(int column);
    
    /**
     * Method used to return the Column that this Filter should be applied to
     * @return
     */
    public int getColumn();
    
    /**
     * Clear all set choices for the current filter.
     */
    public void unselectAll();
    
    /**
     * checks if any filter is set
     */
    public boolean isFilterSet();
    
}
