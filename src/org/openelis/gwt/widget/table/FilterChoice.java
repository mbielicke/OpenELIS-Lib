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

/**
 *  This class is used to present a list of filter choices to the user.
 *  The FilterChoice is displayed as a list of options in a TableHeader menu. 
 */
public class FilterChoice {
    
    /**
     * Value to filter for in the Table model.
     */
    protected Object value;
    
    /**
     * Diplay String to use in the Filter Menu 
     */
    protected String display;
    
    /**
     * Flag indicating if this choice has been selected
     */
    protected boolean selected;
   
    /**
     * Method used to return the Value to filter by for this choice
     * @return
     */
    public Object getValue() {
        return value;
    }
    
    /**
     * Method used to set the Value to filter by for this choice
     * @param value
     */
    public void setValue(Object value) {
        this.value = value;
    }
    
    /**
     * Method used to return the display to be used by this choice
     * @return
     */
    public String getDisplay() {
        return display;
    }
    
    /**
     * Method used to set the display to be used by this choice
     * @param display
     */
    public void setDisplay(String display) {
        this.display = display;
    }
    
    /**
     * Method used to determine if this choice is currently selected
     * @return
     */
    public boolean isSelected() {
        return selected;
    }
    
    /**
     * Method used to set if this choice is currently selected
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
