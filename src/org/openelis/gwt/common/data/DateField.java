/*
 * Created on Apr 10, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.DatetimeRPC;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DateField extends AbstractField implements Serializable {

    protected DateObject date = (DateObject)object;
    private Integer max;
    private Integer min;

    public DateField(){
        object = new DateObject();
    }
    
    public boolean isValid() {
        if (required) {
            if (date.value == null) {
                addError("Field is required");
                return false;
            }
        }
        if (date.value != null && !isInRange()) {
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
        if (min != null && date.value.before(DatetimeRPC.getInstance()
                                                   .add(-min.intValue()))) {
            addError("Date is too far in the past");
            return false;
        }
        if (max != null && date.value.after(DatetimeRPC.getInstance()
                                                  .add(max.intValue()))) {
            addError("Date is too far in the future");
            return false;
        }
        return true;
    }

    public String toString() {
        if (date.value == null) {
            return "";
        }
        return date.value.toString();
    }

    public void setBegin(byte begin) {
        date.begin = begin;
    }

    public void setEnd(byte end) {
        date.end = end;
    }

    public void setMin(Object min) {
        this.min = (Integer)min;
    }

    public void setMax(Object max) {
        this.max = (Integer)max;
    }

    public byte getBegin() {
        return date.begin;
    }

    public byte getEnd() {
        return date.end;
    }

    public Object getInstance() {
        DateField obj = new DateField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setDataObject(date);
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
