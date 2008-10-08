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

;
/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class CheckField extends AbstractField implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String TAG_NAME = "rpc-check";

    public CheckField() {
        object = new StringObject();
    }
    
    public CheckField(Node node){
        this();
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                             .getNamedItem("key")
                             .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                              .getNamedItem("required")
                                              .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            setValue(node.getFirstChild().getNodeValue());
        }
    }
    
    public void validate() {
        if (required) {
            if (object.getValue()  == null) {
                addError("Field is required");
                valid =  false;
                return;
            }
        }
        if (!isInRange()) {
            valid = false;
            return;
        }
        valid = true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public String toString() {
       return (String)object.getValue();
    }

    public boolean isChecked() {
        return (object.getValue() != null && "Y".equals((String)object.getValue()));
    }

    public CheckField getInstance() {
        CheckField obj = new CheckField();
        obj.setRequired(required);
        obj.setValue(getValue());
        obj.setKey(key);
        return obj;
    }

    public CheckField getInstance(Node node) {
        return new CheckField(node);
    }
    
    public Object getValue() {
        String returnValue = (String)object.getValue();
        if("".equals(returnValue))
            return null;
        else
            return returnValue;
    }
}
