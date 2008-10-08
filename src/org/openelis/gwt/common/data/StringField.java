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
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;


import java.io.Serializable;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StringField extends AbstractField implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Integer min;
    private Integer max;
    public static final String TAG_NAME = "rpc-string";
    
    public StringField() {
        object = new StringObject();
    }
    
    public StringField(String value) {
        object = new StringObject(value);
    }

    public StringField(Node node){
        this();
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("max") != null)
            setMax(new Integer(node.getAttributes()
                                           .getNamedItem("max")
                                           .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            setMin(new Integer(node.getAttributes()
                                           .getNamedItem("min")
                                           .getNodeValue()));
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("reset") != null)
            setAllowReset(new Boolean(node.getAttributes()
                                                .getNamedItem("reset")
                                                .getNodeValue()).booleanValue());
        
        if (node.hasChildNodes()) {
            setValue(node.getFirstChild().getNodeValue());
        }
    }
    
    public void validate() {
        if (required) {
            if (((StringObject)object).value == null || ((StringObject)object).value.length() == 0) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        if (((StringObject)object).value != null && !isInRange()) {
            valid =  false;
            return;
        }
        valid = true;
    }

    public boolean isInRange() {
        if (((StringObject)object).value == null)
            return true;
        if (max != null && (((StringObject)object).value).length() > ((Integer)max).intValue()) {
            addError("Field exceeded maximum length");
            return false;
        }
        if (min != null && (((StringObject)object).value).length() < ((Integer)min).intValue() &&
            ((StringObject)object).value.length() > 0) {
            addError("Field is below minimum length");
            return false;
        }
        return true;
    }


    public String toString() {
        if (((StringObject)object).value == null)
            return "";
        return ((StringObject)object).value;
    }

    public void setMin(Object min) {
        this.min = (Integer)min;
    }

    public void setMax(Object max) {
        this.max = (Integer)max;
    }

    public StringField getInstance() {
        StringField obj = new StringField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setRequired(required);
        obj.setValue(getValue());
        obj.setKey(key);
        obj.setAllowReset(allowReset);
        return obj;
    }

    public StringField getInstance(Node node) {
        return new StringField(node);
    }
}
