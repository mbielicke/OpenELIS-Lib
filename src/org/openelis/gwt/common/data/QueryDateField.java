package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.DatetimeRPC;


import java.util.Date;
import java.util.Iterator;

public class QueryDateField extends QueryField {


    private static final long serialVersionUID = 1L;
    private byte begin;
    private byte end;
    private Integer max;
    private Integer min;

    public void validate() {
        Iterator paramIt = parameter.iterator();
        while (paramIt.hasNext()) {
            String param = (String)paramIt.next();
            try {
                Date date = new Date(param.replaceAll("-", "/"));
            } catch (Exception e) {
                addError("Not a Valid Date");
                valid = false;
                return;
            }
        }
        if (value != null && !isInRange()) {
            valid = false;
        }
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
        Iterator paramIt = parameter.iterator();
        while (paramIt.hasNext()) {
            String[] params = ((String)paramIt.next()).split("..");
            for (int i = 0; i < params.length; i++) {
                Date date = new Date(params[i].replaceAll("-", "/"));
                DatetimeRPC dVal = DatetimeRPC.getInstance(begin, end, date);
                if (min != null && dVal.before(DatetimeRPC.getInstance()
                                                          .add(-min.intValue()))) {
                    addError("Date is too far in the past");
                    return false;
                }
                if (max != null && dVal.after(DatetimeRPC.getInstance()
                                                         .add(max.intValue()))) {
                    addError("Date is too far in the future");
                    return false;
                }
            }
        }
        return true;
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
        QueryDateField obj = new QueryDateField();
        obj.setBegin(begin);
        obj.setEnd(end);
        obj.setMax(max);
        obj.setMin(min);
        obj.setValue(value);
        return obj;
    }

    public Object getInstance(Node field) {
        QueryDateField date = new QueryDateField();
        date.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
        date.setBegin(Byte.parseByte(field.getAttributes()
                                          .getNamedItem("begin")
                                          .getNodeValue()));
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
        return date;
    }
}
