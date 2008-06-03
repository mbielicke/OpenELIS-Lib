package org.openelis.gwt.common.data;

import java.io.Serializable;

public class NumberObject implements DataObject, Serializable {

    public enum Type {INTEGER,DOUBLE}

    private static final long serialVersionUID = 1L;
    protected Double          value;
    protected Type            type;
    protected boolean         invalid;

    public NumberObject() {
    }

    public NumberObject(Type type) {
        this.type = type;
    }
    
    public NumberObject(Type type, Object value){
        setType(type);
        setValue(value);
    }
    
    public NumberObject(Integer value){
        this(Type.INTEGER,value);
    }
    
    public NumberObject(Double value){
        this(Type.DOUBLE,value);
    }
    
    public NumberObject(int value){
        this(Type.INTEGER,new Integer(value));
    }
    
    public NumberObject(double value){
        this(Type.DOUBLE,new Double(value));
    }

    public Object getValue() {
        if (type == Type.INTEGER)
            if (value == null)
                return null;
            else
                return new Integer(value.intValue());
        return value;
    }

    public void setValue(Object object) {
        invalid = false;
        try {
            if (object != null && !"".equals(object)) {
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

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
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
