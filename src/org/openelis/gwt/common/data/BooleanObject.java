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
/**
 * BooleanObject is a class that implements the DataObject interface
 * and wraps objects of type Boolean so they can be used the RPC interfaces
 * that we have set to use with GWT and OpenELIS.
 * @author tschmidt
 *
 */
public class BooleanObject implements DataObject {

    private static final long serialVersionUID = 1L;

    public Boolean value;
    
    /**
     * Default constructor use to be necessaty with GWT, but I 
     * think it is required anymore
     *
     */
    public BooleanObject() {
        
    }
    
    /**
     * Constructor that takes a default value.  The parameter passed
     * can be a Boolean or String with values of "Y","N","true","false"
     * @param value
     */
    public BooleanObject(Object value){
        setValue(value);
    }
    
    /**
     * Method to set the value fro this object.  The parameter passed
     * can be a Boolean or String with values of "Y","N","true","false"
     */
    public void setValue(Object val) {
        // TODO Auto-generated method stub
        if (val != null) {
            if (val instanceof String) {
                if (val.equals("Y"))
                    value = new Boolean(true);
                else if (val.equals("N"))
                    value = new Boolean(false);
                else
                    value = new Boolean((String)val);
            } else
                value = (Boolean)val;
        } else {
            value = null;
        }
    }

    /**
     * Returns the current value of the object
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * Returns the value of the object in a String as either "Y" or "N"
     */
    public String toString() {
        if (value == null) {
            return "";
        }
        if (((Boolean)value).booleanValue())
            return "Y";
        else
            return "N";
    }
    
    /**
     * Creates a new object with the same value as this object 
     */
    public Object clone() {
        BooleanObject clone = new BooleanObject();
        clone.setValue(value);
        return clone;
    }
    
    /**
     * Implementation of .equals for this wrapper.
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof BooleanObject))
            return false;
        return ((BooleanObject)obj).value.equals(value);
    }
    
    /**
     * Returns the Boolean unique for use in Indexes
     */
    public int hashCode() {
        return value.hashCode();
    }
    
    /**
     * Implemented for the Comparator interface for soritng and filtering.
     */
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return value.compareTo((Boolean)o);
    }
}
