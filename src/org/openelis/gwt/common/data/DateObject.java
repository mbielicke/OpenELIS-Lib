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

import org.openelis.gwt.common.DatetimeRPC;

import java.io.Serializable;

public class DateObject implements DataObject, Serializable {
    

    private static final long serialVersionUID = 1L;
    protected byte begin;
    protected byte end;

    protected DatetimeRPC value = null;
    
    public DateObject() {
        
    }
    
    public DateObject(byte begin, byte end, Object value) {
        setBegin(begin);
        setEnd(end);
        setValue(value);
    }
    
    public void setValue(Object val) {
        if (val == null || val == "") {
            value = null;
        } else if (val instanceof DatetimeRPC) {
            value = (DatetimeRPC)val;
        } else {
            value = DatetimeRPC.getInstance(begin, end, val);
        }
    }

    public Object getValue() {
        return value;
    }
    
    public String toString() {
        if (value == null) {
            return "";
        }
        return value.toString();
    }

    public void setBegin(byte begin) {
        this.begin = begin;
    }

    public void setEnd(byte end) {
        this.end = end;
    }

    public byte getBegin() {
        return this.begin;
    }

    public byte getEnd() {
        return this.end;
    }
    
    public Object getInstance() {
        DateObject clone = new DateObject();
        clone.begin = begin;
        clone.end = end;
        clone.setValue(value);
        return clone;
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof DateObject))
            return false;
        return ((DateObject)obj).value.equals(value);
    }
    
    public int hashCode() {
        return value.hashCode();
    }

}
