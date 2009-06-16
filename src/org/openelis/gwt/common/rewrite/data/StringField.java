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
package org.openelis.gwt.common.rewrite.data;

import org.openelis.gwt.common.ValidationException;
import org.openelis.gwt.screen.AppScreen;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StringField extends Field<String> {
    
    private static final long serialVersionUID = 1L;
    private Integer min;
    private Integer max;;

    
    public StringField() {
        super();
    }
    
    public void validate() throws ValidationException {
        if (required) {
            if (value == null || value.length() == 0) {
            	valid = false;
                throw new ValidationException(AppScreen.consts.get("fieldRequiredException"));    
             }
        }
        if (value != null) {
        	try {
        		isInRange();
        	}catch(ValidationException e) {
        		valid = false;
        		throw e;
        	}
        }
        valid = true;
    }

    public boolean isInRange() throws ValidationException {
        if (value == null)
            return true;
        if (max != null && value.length() > ((Integer)max).intValue()) {
            throw new ValidationException(AppScreen.consts.get("fieldMaxLengthException"));
        }
        if (min != null && value.length() < ((Integer)min).intValue() &&
            value.length() > 0) {
            throw new ValidationException(AppScreen.consts.get("fieldMinLengthException"));
        }
        return true;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Object clone() {
        StringField obj = new StringField();
        obj.setMax(max);
        obj.setMin(min);
        obj.required = required;
        obj.value = value;
        return obj;
    }

}
