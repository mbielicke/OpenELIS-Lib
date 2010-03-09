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
package org.openelis.gwt.common;

import org.openelis.gwt.widget.HasField;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.ui.Widget;

public class Util {

	public static UIFocusHandler focusHandler = new UIFocusHandler();
	
    /**
     * Simple method that returns a string representation of the obj.
     */
    public static String toString(Object obj) {
        if (obj == null)
            return "";
        return obj.toString();
    }
    
    public static int stripUnits(String value, String... units) throws NumberFormatException {
    	value = value.trim();
    	for(String unit : units) {
    		if(value.endsWith(unit))
    			return Integer.parseInt(value.substring(0,value.length()-unit.length()));
    	}
    	return Integer.parseInt(value);
    }
    
    private static class UIFocusHandler implements FocusHandler, BlurHandler {
        public void onFocus(FocusEvent event) {
            if ( ((HasField)event.getSource()).isEnabled()) {
                ((Widget)event.getSource()).addStyleName("Focus");
            }
        }

        public void onBlur(BlurEvent event) {
            if ( ((HasField)event.getSource()).isEnabled()) {
                ((Widget)event.getSource()).removeStyleName("Focus");
            }
        }
    }
}
