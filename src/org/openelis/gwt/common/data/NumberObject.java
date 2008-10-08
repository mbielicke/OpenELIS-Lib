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

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return value.compareTo(((NumberObject)o).value);
    }
}
