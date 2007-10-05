/*
 * Created on Mar 27, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;

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
    protected Boolean value;

    public boolean isValid() {
        if (required) {
            if (value == null) {
                addError("Field is required");
                return false;
            }
        }
        if (!isInRange()) {
            return false;
        }
        return true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        if (val != null) {
            if (val instanceof String) {
                if (val.equals("Y"))
                    value = new Boolean(true);
                else if (val.equals("N"))
                    value = new Boolean(false);
                else
                    value = new Boolean((String)val);
            } else
                value = (Boolean)val;
        } else {
            value = null;
        }
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        if (value == null) {
            return "";
        }
        if (((Boolean)value).booleanValue())
            return "Y";
        else
            return "N";
    }

    public boolean isChecked() {
        if (value == null)
            return false;
        return value.booleanValue();
    }

    public Object getInstance() {
        CheckField obj = new CheckField();
        obj.setRequired(required);
        obj.setValue(value);
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
