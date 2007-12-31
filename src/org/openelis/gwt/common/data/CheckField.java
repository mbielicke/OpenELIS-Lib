/*
 * Created on Mar 27, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
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

    public CheckField() {
        object = new BooleanObject();
    }
    
    public void validate() {
        if (required) {
            if (((BooleanObject)object).value  == null) {
                addError("Field is required");
                valid =  false;
                return;
            }
        }
        if (!isInRange()) {
            valid = false;
        }
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public String toString() {
        if (((BooleanObject)object).value == null) {
            return "";
        }
        if (((Boolean)((BooleanObject)object).value).booleanValue())
            return "Y";
        else
            return "N";
    }

    public boolean isChecked() {
        if (((BooleanObject)object).value == null)
            return false;
        return ((BooleanObject)object).value.booleanValue();
    }

    public Object getInstance() {
        CheckField obj = new CheckField();
        obj.setRequired(required);
        obj.setValue(getValue());
        return obj;
    }

    public Object getInstance(Node node) {
        CheckField check = new CheckField();
        if (node.getAttributes().getNamedItem("key") != null)
            check.setKey(node.getAttributes()
                             .getNamedItem("key")
                             .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            check.setRequired(new Boolean(node.getAttributes()
                                              .getNamedItem("required")
                                              .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            check.setValue(new Boolean(node.getFirstChild().getNodeValue()));
        }
        return check;
    }
}
