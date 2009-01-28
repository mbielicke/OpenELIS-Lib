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
import java.util.Date;

/**
 * DateField is an implementation of AbstractField that represents data
 * used for Date fields
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DateField extends AbstractField<DateObject> {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Number of days in the future from current date that this field is 
     * allowed to represent. 
     */
    private Integer max;
    /**
     * Number of days in the past from current date that this field is 
     * allowed to represent.
     */
    private Integer min;
    /**
     * Tag name used in the xml definition of the RPC
     */
    public static final String TAG_NAME = "rpc-date";
    
    /**
     * String that represents the Date format to be used when displaying this field
     */
    private String pattern;

    /**
     * Default constructor
     *
     */
    public DateField(){
        super(new DateObject());
    }
    
    /**
     * Constructor that takes values for the begin and end precsion and the value of the 
     * date in a DatetimeRPC object
     * @param begin
     * @param end
     * @param val
     */
    public DateField(byte begin, byte end, DatetimeRPC val){
        super(new DateObject());
        setBegin(begin);
        setEnd(end);
        setValue(val);
    }
    
    /**
     * Constructor that takes values for the begin and end precision and the value of the 
     * date in a java.util.Date object
     * @param begin
     * @param end
     * @param val
     */
    public DateField(byte begin, byte end, Date val){
        super(new DateObject());
        setBegin(begin);
        setEnd(end);
        setValue(DatetimeRPC.getInstance(getBegin(),
                                         getEnd(),
                                         val));
    }
    
    /**
     * Constructor that accepts an XML defintion of this field to be used in a FormRPC
     * @param node
     */
    public DateField(Node node) {
        this();
        setAttributes(node);
    }
    
    public void setAttributes(Node node) {
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
    
    /**
     * This method is called by the FormRPC and will check to see if the value set is valid.
     */
    public void validate() {
        if (required) {
            if (object.value == null) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        if (object.value != null && !isInRange()) {
            valid = false;
        }
        valid = true;
    }

   /**
    * This method will return the value of the required flag
    */
    public boolean isRequired() {
        // TODO Auto-generated method stub
        return required;
    }

   /**
    * This mehtod will determine if the date set is in the specified Date range for this field
    */
    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (min != null && object.value.before(DatetimeRPC.getInstance()
                                                   .add(-min.intValue()))) {
            addError("Date is too far in the past");
            return false;
        }
        if (max != null && object.value.after(DatetimeRPC.getInstance()
                                                  .add(max.intValue()))) {
            addError("Date is too far in the future");
            return false;
        }
        return true;
    }

    /**
     * Returns the value of this field as a String using the specified date pattern
     */
    public String toString() {
        return format();
    }

    /**
     * Sets the begin precision of this date field
     * @param begin
     */
    public void setBegin(byte begin) {
        object.begin = begin;
    }

    /**
     * Sets the end precision of this date field
     * @param end
     */
    public void setEnd(byte end) {
        object.end = end;
    }
    
    /**
     * Sets the minimum allowed date for this field represented by 
     * days from current date
     */
    public void setMin(Object min) {
        this.min = (Integer)min;
    }

    /**
     * Sets the maximum allowed date for this field represented by 
     * days from current date. 
     */
    public void setMax(Object max) {
        this.max = (Integer)max;
    }

    /**
     * Returns the begin precision value for this field.
     * @return
     */
    public byte getBegin() {
        return object.begin;
    }

    /**
     * Returns the end precision value for this field.
     * @return
     */
    public byte getEnd() {
        return object.end;
    }

    /**
     * Creates a new DataField object and set it values from this
     * object 
     */
    public Object clone() {
        DateField obj = new DateField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setValue(getValue());
        obj.setKey(key);
        return obj;
    }

    /**
     * Creates a new DateField object based on the XML node definition passed.
     * @param node
     * @return
     */
    public DateField getInstance(Node node) {
        return new DateField(node);
    }
    
    /**
     * Formats the string representation of this fields value using the set pattern
     */
    public String format() {
        if(object.value == null)
            return "";
        if(pattern != null)
            return DateTimeFormat.getFormat(pattern).format(object.value.getDate());
        return String.valueOf(object.value);
    }
    
    /**
     * Sets the string that represents the pattern to be used when formatting this fields value
     * for display
     */
    public void setFormat(String pattern) {
        this.pattern = pattern;
    }
        
    /**
     * Sets the value of the field from the passed string representation of this fields value.
     * If a pattern is set then the date must be passed in the pattern format to be valid.
     */
    public void setValue(Object val) {
        if(pattern == null || val == null || "".equals(val)){
            super.setValue(val);
            return;
        }
        super.setValue(DateTimeFormat.getFormat(pattern).parse((String)val));
    }
    
    public DatetimeRPC getValue() {
        return (DatetimeRPC)super.getValue();
    }
}
