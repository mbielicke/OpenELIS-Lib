package org.openelis.gwt.common.data;

import java.io.Serializable;

public class BooleanObject implements DataObject, Serializable {

    private static final long serialVersionUID = 1L;

    public Boolean value;
    
    public BooleanObject() {
        
    }
    
    public BooleanObject(Object value){
        setValue(value);
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
    
    public Object getInstance() {
        BooleanObject clone = new BooleanObject();
        clone.setValue(value);
        return clone;
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof BooleanObject))
            return false;
        return ((BooleanObject)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
