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

public class StringObject implements DataObject, Serializable {

    private static final long serialVersionUID = 1L;
    protected String value;
    
    public StringObject() {
        
    }
    
    public StringObject(String val){
        setValue(val);
    }
    
    public Object getValue() {
        if(value == null)
            return "";
        return value;
    }

    public void setValue(Object val) {
    	value = (String)val;
    }
    
    public Object getInstance() {
        StringObject clone = new StringObject();
        clone.setValue(value);
        return clone;
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof StringObject))
            return false;
        return ((StringObject)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }    

}
