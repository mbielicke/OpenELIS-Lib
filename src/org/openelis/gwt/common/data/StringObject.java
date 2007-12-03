package org.openelis.gwt.common.data;

import java.io.Serializable;

public class StringObject implements DataObject, Serializable {

    protected String value;
    
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = (String)value;
    }

}
