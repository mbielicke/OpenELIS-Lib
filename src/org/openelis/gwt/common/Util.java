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

import org.openelis.gwt.widget.ScreenWidgetInt;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;

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
    
    public static int stripUnits(String value) throws NumberFormatException {
        int i = 0;
    	char ch;
    	
    	for(int j = 0 ; j < value.length(); j++) {
    	    ch = value.substring(j,j).charAt(0);
    		if(ch >= 0 && ch <= 9)
    		    i = i * 10 + ch;
    		else
    		    break;
    	}
    	return i;
    }
    
    public static String addUnits(int value) {
        return value+"px";
    }
    
    private static class UIFocusHandler implements FocusHandler, BlurHandler {
        public void onFocus(FocusEvent event) {
        	if(event.getSource() instanceof ScreenWidgetInt) {
        		if ( ((ScreenWidgetInt)event.getSource()).isEnabled()) {
        			((ScreenWidgetInt)event.getSource()).addFocusStyle("Focus");
        		}
        	}
            /*
            if ( ((HasField)event.getSource()).isEnabled()) {
                ((Widget)event.getSource()).addStyleName("Focus");
            }
            */
        }

        public void onBlur(BlurEvent event) {
        	//Remove this outer instance check after the redesign is completed
        	if(event.getSource() instanceof ScreenWidgetInt) {
        		if ( ((ScreenWidgetInt)event.getSource()).isEnabled()) {
        			((ScreenWidgetInt)event.getSource()).removeFocusStyle("Focus");
        		}
        	}
        }
    }
    
    /**
     * Compares the two parameters to see if they are different
     * 
     * @return true if object is the same; otherwise false
     */
    public static boolean isDifferent(Object a, Object b) {
        return (a == null && b != null) || (a != null && !a.equals(b));
    }
}
