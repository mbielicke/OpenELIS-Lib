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

import org.openelis.gwt.common.DatetimeRPC;
/**
 * DateObject is an implementation of DataObject that wraps a
 * Date value to be passed between the server and the client.
 * @author tschmidt
 *
 */

public class DateObject implements DataObject {
    

    private static final long serialVersionUID = 1L;
    /**
     * Begin precision of the Date value
     */
    protected byte begin;
    /**
     * End precision of the Date value
     */
    protected byte end;

    /**
     * The date value that is wrapped by this object
     */
    protected DatetimeRPC value = null;
    
    /**
     * Default constructor
     *
     */
    public DateObject() {
        
    }
    
    /**
     * Constructor that accepts begin and end precisions along with
     * a default value for the object
     * @param begin
     * @param end
     * @param value
     */
    public DateObject(byte begin, byte end, Object value) {
        setBegin(begin);
        setEnd(end);
        setValue(value);
    }
    
    /**
     * Sets the value of the date wrapped by this object. The value passed can be of 
     * type DatetimeRPC or java.util.Date.
     */
    public void setValue(Object val) {
        if (val == null || val == "") {
            value = null;
        } else if (val instanceof DatetimeRPC) {
            value = (DatetimeRPC)val;
        } else {
            value = DatetimeRPC.getInstance(begin, end, val);
        }
    }

    /**
     * Returns the value of the this object as a DatetimeRPC
     */
    public DatetimeRPC getValue() {
        return value;
    }
    
    /**
     * Returns the value of this object a string 
     */
    public String toString() {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    /**
     * Sets the begin precision for this object.
     * @param begin
     */
    public void setBegin(byte begin) {
        this.begin = begin;
    }

    /**
     * Sets the end precision for this object.
     * @param end
     */
    public void setEnd(byte end) {
        this.end = end;
    }

    /**
     * Returns the begin precision of this object.
     * @return
     */
    public byte getBegin() {
        return this.begin;
    }

    /**
     * Returns the end precision of this object.
     * @return
     */
    public byte getEnd() {
        return this.end;
    }
    
    /**
     * Creates a new DateObject setting the values of the new
     * object to the same as this object.
     */
    public Object clone() {
        DateObject clone = new DateObject();
        clone.begin = begin;
        clone.end = end;
        clone.setValue(value);
        return clone;
    }
    
    /**
     * Overrride of the .equals() method
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof DateObject))
            return false;
        return ((DateObject)obj).value.equals(value);
    }
    
    /**
     * Override of hashCode() method returning the hashCode of the wrapped
     * value object
     */
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Implementation of the compareTo(object obj)
     */
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return -1;
    }

}
