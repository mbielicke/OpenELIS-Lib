package org.openelis.gwt.common.data;

import org.openelis.gwt.common.DatetimeRPC;

import java.io.Serializable;

public class DateObject implements DataObject, Serializable {
    

    private static final long serialVersionUID = 1L;
    protected byte begin;
    protected byte end;
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.DatetimeRPC>
     */
    protected DatetimeRPC value = null;
    
    public void setValue(Object val) {
        if (val == null || val == "") {
            value = null;
        } else if (val instanceof DatetimeRPC) {
            value = (DatetimeRPC)val;
        } else {
            value = DatetimeRPC.getInstance(begin, end, val);
        }
    }

    public Object getValue() {
        return value;
    }
    
    public String toString() {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    public void setBegin(byte begin) {
        this.begin = begin;
    }

    public void setEnd(byte end) {
        this.end = end;
    }

    public byte getBegin() {
        return this.begin;
    }

    public byte getEnd() {
        return this.end;
    }
    
    public Object getInstance() {
        DateObject clone = new DateObject();
        clone.begin = begin;
        clone.end = end;
        clone.setValue(value);
        return clone;
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof DateObject))
            return false;
        return ((DateObject)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }

}
