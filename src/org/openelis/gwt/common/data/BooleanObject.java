package org.openelis.gwt.common.data;

import java.io.Serializable;

public class BooleanObject implements DataObject, Serializable {

    public Boolean value;
    
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
}
