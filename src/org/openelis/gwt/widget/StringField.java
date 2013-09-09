/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget;

import org.openelis.ui.common.Datetime;
import org.openelis.ui.messages.Messages;

/**
 * @author tschmidt
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class StringField extends Field<String> {

    private static final long serialVersionUID = 1L;
    private Integer           min, max;

    public StringField() {
        super();
    }

    public void validate() {
        valid = true;
        if (required) {
            if (value == null || value.trim().length() == 0) {
                valid = false;
                addException(new Exception(Messages.get().exc_fieldRequired()));
            } else
                removeException(Messages.get().exc_fieldRequired());
        }
        if (value != null && !isInRange())
            valid = false;
    }

    public void ValidateQuery() {
        QueryFieldUtil qField;
        
        qField = new QueryFieldUtil();
        try {
            qField.parse(value);
            queryString = value;
            removeException(Messages.get().exc_invalidQuery());
        } catch (Exception e) {
            addException(new Exception(Messages.get().exc_invalidQuery()));
        }
    }

    public void setStringValue(String value) {
        this.value = value;
    }

    public boolean isInRange() {
        boolean rangeVal;
        
        rangeVal = true;
        if (value != null) {
            if (max != null && value.length() > max.intValue()) {
                rangeVal = false;
                addException(new Exception(Messages.get().exc_fieldMaxLength()));
            } else
                removeException(Messages.get().exc_fieldMaxLength());
            if (min != null && value.length() < min.intValue() && value.length() > 0) {
                rangeVal = false;
                addException(new Exception(Messages.get().exc_fieldMinLength()));
            } else
                removeException(Messages.get().exc_fieldMinLength());
        }
        return rangeVal;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Object clone() {
        StringField obj;
        
        obj = new StringField();
        obj.setMax(max);
        obj.setMin(min);
        obj.required = required;
        obj.value = value;

        return obj;
    }
}