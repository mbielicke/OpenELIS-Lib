package org.openelis.gwt.common.data;

import java.io.Serializable;

public class StringObject implements DataObject, Serializable {

    protected String value;
    
    public Object getValue() {
        if(value == null)
            return "";
        return value;
    }

    public void setValue(Object val) {
        if(val != null)
            value = (String)val;
    }

}
