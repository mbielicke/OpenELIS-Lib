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

}
