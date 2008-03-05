package org.openelis.gwt.common.data;

import java.io.Serializable;

public class NumberObject implements DataObject, Serializable {

    private static final long serialVersionUID = 1L;
    protected Double value;
    protected String type;
    protected boolean invalid;
    
    public Object getValue() {
        if(type.equals("integer"))
        	if(value == null)
        		return null;
        	else
        		return new Integer(value.intValue());
        return value;
    }

    public void setValue(Object object) {
        invalid = false;
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
            invalid = true;
        }
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Object getInstance() {
        NumberObject clone = new NumberObject();
        clone.type = type;
        clone.value = new Double(value.doubleValue());
        return clone;
    }
}
