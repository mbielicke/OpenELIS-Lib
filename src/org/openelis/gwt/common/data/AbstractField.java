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

import com.google.gwt.xml.client.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class AbstractField implements DataField, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected ArrayList<String> errors = new ArrayList<String>();
    protected boolean required;
    public String key;
    protected String tip;
    protected DataObject object;
    public boolean valid = true;
    protected boolean allowReset = true;

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public void addError(String err) {
        errors.add(err);
        valid = false;
    }

    public ArrayList getErrors() {
        return errors;
    }

    public Vector getValues() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setMin(Object min) {
    }

    public void setMax(Object max) {
    }

    public void setKey(Object key) {
        this.key = (String)key;
    }

    public Object getKey() {
        return key;
    }

    public void addOption(Object key, Object val) {
        // TODO Auto-generated method stub
    }
    
    public void clearErrors() {
        errors = new ArrayList<String>();
        valid = true;
    }

    public DataField getInstance(Node node) {
        return null;
    }

    public boolean isValid() {
        // TODO Auto-generated method stub
        return valid;
    }

    public void validate() {
        
    }
    
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setValue(Object val) {
       object.setValue(val);
    }

    public Object getValue() {
        return object.getValue();
    }

    public Object clone() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public String getTip() {
        return tip;
    }
    
    public void setTip(String tip) {
        this.tip = tip;
    }
    
    public void setDataObject(DataObject object){
        this.object = object;
    }
    
    public DataObject getDataObject() {
        return object;
    }
    
    public void setAllowReset(boolean reset) {
        allowReset = reset;
    }
    
    public boolean allowsReset() {
        return allowReset;
    }
    
    public void reset() {
        valid = true;
        errors.clear();
    }
    
    public int compareTo(Object o) {
        if(o instanceof AbstractField)
            return object.compareTo(((AbstractField)o).object);
        else if(o instanceof DataObject)
            return object.compareTo(o);
        else
            return -1;
    }
    
    public boolean equals(Object o) {
        if(o instanceof AbstractField)
            return object.equals(((AbstractField)o).object);
        else if (o instanceof DataObject)
            return object.equals(o);
        else
            return false;
    }
    
    public String format() {
        if(getValue() == null)
            return "";
        return String.valueOf(getValue());
    }
    
    public void setFormat(String pattern){
        
    }

}
