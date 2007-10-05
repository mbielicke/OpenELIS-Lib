/*
 * Created on Apr 10, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;

import com.google.gwt.xml.client.Node;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DateField extends AbstractField implements Serializable {
    private byte begin;
    private byte end;
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.DatetimeRPC>
     */
    private DatetimeRPC value = null;
    private Integer max;
    private Integer min;

    public boolean isValid() {
        if (required) {
            if (value == null) {
                addError("Field is required");
                return false;
            }
        }
        if (value != null && !isInRange()) {
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IField#isRequired()
     */
    public boolean isRequired() {
        // TODO Auto-generated method stub
        return required;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IField#isInRange()
     */
    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (min != null && value.before(DatetimeRPC.getInstance()
                                                   .add(-min.intValue()))) {
            addError("Date is too far in the past");
            return false;
        }
        if (max != null && value.after(DatetimeRPC.getInstance()
                                                  .add(max.intValue()))) {
            addError("Date is too far in the future");
            return false;
        }
        return true;
    }

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

    public void setMin(Object min) {
        this.min = (Integer)min;
    }

    public void setMax(Object max) {
        this.max = (Integer)max;
    }

    public byte getBegin() {
        return this.begin;
    }

    public byte getEnd() {
        return this.end;
    }

    public Object getInstance() {
        DateField obj = new DateField();
        obj.setBegin(begin);
        obj.setEnd(end);
        obj.setMax(max);
        obj.setMin(min);
        obj.setValue(value);
        return obj;
    }

    public Object getInstance(Node field) {
        DateField date = new DateField();
        if (field.getAttributes().getNamedItem("key") != null)
            date.setKey(field.getAttributes()
                             .getNamedItem("key")
                             .getNodeValue());
        if (field.getAttributes().getNamedItem("required") != null)
            date.setRequired(new Boolean(field.getAttributes()
                                              .getNamedItem("required")
                                              .getNodeValue()).booleanValue());
        if (field.getAttributes().getNamedItem("begin") != null)
            date.setBegin(Byte.parseByte(field.getAttributes()
                                              .getNamedItem("begin")
                                              .getNodeValue()));
        if (field.getAttributes().getNamedItem("end") != null)
            date.setEnd(Byte.parseByte(field.getAttributes()
                                            .getNamedItem("end")
                                            .getNodeValue()));
        if (field.getAttributes().getNamedItem("max") != null)
            date.setMax(new Integer(field.getAttributes()
                                         .getNamedItem("max")
                                         .getNodeValue()));
        if (field.getAttributes().getNamedItem("min") != null)
            date.setMin(new Integer(field.getAttributes()
                                         .getNamedItem("min")
                                         .getNodeValue()));
        if (field.hasChildNodes()) {
            String def = field.getFirstChild().getNodeValue();
            Date dat = null;
            if (def.equals("current"))
                dat = new Date();
            else
                dat = new Date(def);
            date.setValue(DatetimeRPC.getInstance(date.getBegin(),
                                                  date.getEnd(),
                                                  dat));
        }
        return date;
    }
}
