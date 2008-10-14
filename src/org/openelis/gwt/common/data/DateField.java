/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.common.data;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.xml.client.Node;

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
    
    private static final long serialVersionUID = 1L;
    private Integer max;
    private Integer min;
    public static final String TAG_NAME = "rpc-date";
    private String pattern;

    public DateField(){
        object = new DateObject();
    }
    
    public DateField(byte begin, byte end, DatetimeRPC val){
        object = new DateObject();
        setBegin(begin);
        setEnd(end);
        setValue(val);
    }
    
    public DateField(byte begin, byte end, Date val){
        object = new DateObject();
        setBegin(begin);
        setEnd(end);
        setValue(DatetimeRPC.getInstance(getBegin(),
                                         getEnd(),
                                         val));
    }
    
    public DateField(Node node) {
        this();
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                             .getNamedItem("key")
                             .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                              .getNamedItem("required")
                                              .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("begin") != null)
            setBegin(Byte.parseByte(node.getAttributes()
                                              .getNamedItem("begin")
                                              .getNodeValue()));
        if (node.getAttributes().getNamedItem("end") != null)
            setEnd(Byte.parseByte(node.getAttributes()
                                            .getNamedItem("end")
                                            .getNodeValue()));
        if (node.getAttributes().getNamedItem("max") != null)
            setMax(new Integer(node.getAttributes()
                                         .getNamedItem("max")
                                         .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            setMin(new Integer(node.getAttributes()
                                         .getNamedItem("min")
                                         .getNodeValue()));
        if (node.hasChildNodes()) {
            String def = node.getFirstChild().getNodeValue();
            Date dat = null;
            if (def.equals("current"))
                dat = new Date();
            else
                dat = new Date(def);
            setValue(DatetimeRPC.getInstance(getBegin(),
                                                  getEnd(),
                                                  dat));
        }
        if(node.getAttributes().getNamedItem("pattern") != null){
            setFormat(node.getAttributes().getNamedItem("pattern").getNodeValue());
        }
        
    }
    public void validate() {
        if (required) {
            if (((DateObject)object).value == null) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        if (((DateObject)object).value != null && !isInRange()) {
            valid = false;
        }
        valid = true;
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
        if (min != null && ((DateObject)object).value.before(DatetimeRPC.getInstance()
                                                   .add(-min.intValue()))) {
            addError("Date is too far in the past");
            return false;
        }
        if (max != null && ((DateObject)object).value.after(DatetimeRPC.getInstance()
                                                  .add(max.intValue()))) {
            addError("Date is too far in the future");
            return false;
        }
        return true;
    }

    public String toString() {
        return format();
    }

    public void setBegin(byte begin) {
        ((DateObject)object).begin = begin;
    }

    public void setEnd(byte end) {
        ((DateObject)object).end = end;
    }

    public void setMin(Object min) {
        this.min = (Integer)min;
    }

    public void setMax(Object max) {
        this.max = (Integer)max;
    }

    public byte getBegin() {
        return ((DateObject)object).begin;
    }

    public byte getEnd() {
        return ((DateObject)object).end;
    }

    public DateField getInstance() {
        DateField obj = new DateField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setValue(getValue());
        obj.setKey(key);
        return obj;
    }

    public DateField getInstance(Node node) {
        return new DateField(node);
    }
    
    public String format() {
        if(((DateObject)object).value == null)
            return "";
        if(pattern != null)
            return DateTimeFormat.getFormat(pattern).format(((DateObject)object).value.getDate());
        return String.valueOf(((DateObject)object).value);
    }
    
    public void setFormat(String pattern) {
        this.pattern = pattern;
    }
        
    public void setValue(Object val) {
        if(pattern == null || val == null || "".equals(val)){
            super.setValue(val);
            return;
        }
        super.setValue(DateTimeFormat.getFormat(pattern).parse((String)val));
  
    }
}
