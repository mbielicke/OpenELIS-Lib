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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;


import java.io.Serializable;
import java.util.Vector;

public class AbstractField implements DataField, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected Vector<String> errors = new Vector<String>();
    protected boolean required;
    protected String key;
    protected String tip;
    protected DataObject object;
    protected boolean valid = true;

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

    public String[] getErrors() {
        String[] retErrors = new String[errors.size()];
        for (int i = 0; i < errors.size(); i++)
            retErrors[i] = errors.get(i);
        return retErrors;
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
        errors = new Vector<String>();
    }

    public Object getInstance(Node node) {
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

    public AbstractField getInstance() {
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
}
