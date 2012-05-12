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
package org.openelis.gwt.widget;



/**
 * This interface is used for widgets that are used in out Screen layout framework
 * and have the ability to be enabled/disabled, recieve/lose focus, defined in the
 * tab order and can have shortcuts defined to them. 
 *
 */

public interface ScreenWidgetInt {
    
    /**
     * Enables/Disables the screen widget. Disabled screen widgets will not
     * allow input.
     */
    public void setEnabled(boolean enabled);

    /**
     * Returns the state of screen widget.
     * 
     * @return TRUE if the screen widget is enabled, FALSE otherwise.
     */
    public boolean isEnabled();
    
    
    public void finishEditing();
  

    
}
