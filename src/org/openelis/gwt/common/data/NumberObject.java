package org.openelis.gwt.common.data;

import java.io.Serializable;

public class NumberObject implements DataObject, Serializable {

    public static final int   INTEGER          = 1, 
                              DOUBLE           = 2;

    private static final long serialVersionUID = 1L;
    protected Double          value;
    protected int             type;
    protected boolean         invalid;

    public NumberObject() {
    }

    public NumberObject(int type) {
        this.type = type;
    }
    
    public NumberObject(int type, Object value){
        setType(type);
        setValue(value);
    }

    public Object getValue() {
        if (type == INTEGER)
            if (value == null)
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
                    value = Double.valueOf((String)object);
                else if (object instanceof Double)
                    value = (Double)object;
                else if (object instanceof Integer)
                    value = new Double(((Integer)object).doubleValue());
            } else {
                value = null;
            }
        } catch (Exception e) {
            invalid = true;
        }
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
	
	public Object getInstance() {
        NumberObject clone = new NumberObject();
        clone.type = type;
        clone.value = new Double(value.doubleValue());
        return clone;
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof NumberObject))
            return false;
        return ((NumberObject)obj).value.equals(value);
    }

    public int hashCode() {
        // TODO Auto-generated method stub
        return value.hashCode();
    }
}
