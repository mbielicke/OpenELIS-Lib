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

import org.openelis.ui.messages.Messages;


/**
 * @author tschmidt
 * 
 * CheckField is an implementation of AbstractField that represents
 * data used for Checkboxes.
 */
public class CheckField extends Field<String> {
    
    /**
     * This method will be used to validate the field for submission to the server.
     */
    public void validate() {
        valid = true;
        if (required) {
            if (value == null) {
            	valid =  false;
                addException(new Exception(Messages.get().exc_fieldRequired()));
            }else
            	removeException(Messages.get().exc_fieldRequired());
        }
    }
    
    public void validateQuery() {
    	//queryString = value;
    }

    /**
     * Hard coded to reuturn true always for checkboxes
     */
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Returns the string value of this field as either "Y" or "N"
     */
    public String toString() {
       return value;
    }

    /**
     * Returns true if the value of the field is "Y" and false for null or "N"
     * @return
     */
    public boolean isChecked() {
        return (value != null && "Y".equals(value));
    }

    /**
     * Will create a new CheckField object and set all of the values and members the 
     * same the calling object.
     */
    public Object clone() {
        CheckField obj = new CheckField();
        obj.required = required;
        obj.value = value;
        return obj;
    }
    
    /**
     * Returns the value of this Checkfield by returning the value of the wrapped
     * StringField
     */
    public String getValue() {
        if("".equals(value))
            return null;
        else
            return value;
    }
    
    public void setStringValue(String value) {
    	this.value = value;
    }
}
