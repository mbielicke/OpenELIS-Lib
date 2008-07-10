/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.common.data;

import java.io.Serializable;

public class BooleanObject implements DataObject, Serializable {

    private static final long serialVersionUID = 1L;

    public Boolean value;
    
    public BooleanObject() {
        
    }
    
    public BooleanObject(Object value){
        setValue(value);
    }
    
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

    public Object getValue() {
        return value;
    }

    public String toString() {
        if (value == null) {
            return "";
        }
        if (((Boolean)value).booleanValue())
            return "Y";
        else
            return "N";
    }
    
    public Object getInstance() {
        BooleanObject clone = new BooleanObject();
        clone.setValue(value);
        return clone;
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof BooleanObject))
            return false;
        return ((BooleanObject)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }
}
