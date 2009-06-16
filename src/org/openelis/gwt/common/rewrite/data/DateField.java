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
package org.openelis.gwt.common.rewrite.data;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.common.ValidationException;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.widget.HandlesEvents;

import java.util.Date;
import java.util.HashMap;

/**
 * DateField is an implementation of AbstractField that represents data
 * used for Date fields
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DateField extends Field<Date> {
    
    private byte begin;
    private byte end;
    
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
     * String that represents the Date format to be used when displaying this field
     */
    private String pattern;

    public DateField() {
    	
    }
    
    /**
     * Constructor that takes values for the begin and end precsion and the value of the 
     * date in a DatetimeRPC object
     * @param begin
     * @param end
     * @param val
     */
    public DateField(byte begin, byte end, Date val){
        setBegin(begin);
        setEnd(end);
        setValue(val);
    }
    
    /**
     * This method is called by the FormRPC and will check to see if the value set is valid.
     */
    public void validate() throws ValidationException {
        if (required) {
            if (value == null) {
            	valid = false;
                throw new ValidationException(AppScreen.consts.get("fieldRequiredException"));
            }
        }
        if (value != null && !isInRange()) {
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
    public boolean isInRange() throws ValidationException {
        // TODO Auto-generated method stub
    	Date today = new Date();
        if (min != null && value.before(DatetimeRPC.getInstance().add(-min.intValue()).getDate())) {
        	valid = false;
            throw new ValidationException(AppScreen.consts.get("fieldPastException"));
        }
        if (max != null && value.after(DatetimeRPC.getInstance()
                                                  .add(max.intValue()).getDate())) {
        	valid = false;
            throw new ValidationException(AppScreen.consts.get("fieldFutureException"));
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
        this.begin = begin;
    }

    /**
     * Sets the end precision of this date field
     * @param end
     */
    public void setEnd(byte end) {
       this.end = end;
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
        return begin;
    }

    /**
     * Returns the end precision value for this field.
     * @return
     */
    public byte getEnd() {
        return end;
    }

    /**
     * Creates a new DataField object and set it values from this
     * object 
     */
    public Object clone() {
        DateField obj = new DateField();
        obj.setMax(max);
        obj.setMin(min);
        obj.value = value;
        obj.setBegin(begin);
        obj.setEnd(end);
        return obj;
    }
    
    /**
     * Formats the string representation of this fields value using the set pattern
     */
    public String format() {
        if(value == null)
            return "";
        if(pattern != null)
            return DateTimeFormat.getFormat(pattern).format(value);
        return String.valueOf(value);
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
    public void setValue(String val) {
        if(pattern == null) {
            if (val == null || val == "") 
                value = null;
           else 
                setValue(DatetimeRPC.getInstance(begin, end, val).getDate());
        }else
            setValue(DatetimeRPC.getInstance(begin, end, DateTimeFormat.getFormat(pattern).parse((String)val)).getDate());
    }
    
}
