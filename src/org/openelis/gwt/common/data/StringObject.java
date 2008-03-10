package org.openelis.gwt.common.data;

import java.io.Serializable;

public class StringObject implements DataObject, Serializable {

    private static final long serialVersionUID = 1L;
    protected String value;
    
    public Object getValue() {
        if(value == null)
            return "";
        return value;
    }

    public void setValue(Object val) {
        if(val != null)
            value = new String((String)val);
        else
        	value = "";
    }
    
    public Object getInstance() {
        StringObject clone = new StringObject();
        clone.setValue(value);
        return clone;
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof StringObject))
            return false;
        return ((StringObject)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }    

}
