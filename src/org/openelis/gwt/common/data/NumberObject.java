package org.openelis.gwt.common.data;

import java.io.Serializable;

public class NumberObject implements DataObject, Serializable {

    protected Double value;
    protected String type;
    
    public Object getValue() {
        if(type.equals("integer"))
            return new Integer(value.intValue());
        return value;
    }

    public void setValue(Object object) {
        try {
            if (object != null) {
                if (object instanceof String && !((String)object).equals(""))
                    this.value = Double.valueOf((String)object);
                if (object instanceof Double)
                    this.value = (Double)object;
                if (object instanceof Integer)
                    this.value = new Double(((Integer)object).doubleValue());
            } else {
                this.value = null;
            }
        } catch (Exception e) {
        }
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
