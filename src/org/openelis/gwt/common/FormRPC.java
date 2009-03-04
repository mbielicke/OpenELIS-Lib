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
/*
 * Created on Mar 24, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;


import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FormRPC implements IForm, Serializable {

    private static final long serialVersionUID = 1L;

    private HashMap<String,AbstractField> fields = new HashMap<String,AbstractField>();
    public int operation;
    public Status status;
    public ArrayList<String> error = new ArrayList<String>();
    public String action;
    public Integer userId;
    public String userName;
    public String fullName;
    public String key;

    public FormRPC() {
    }

    public void setFieldMap(HashMap<String,AbstractField> fields) {
        this.fields = fields;
    }

    public HashMap<String,AbstractField> getFieldMap() {
        return fields;
    }

    public Object getFieldValue(String key) {
        // TODO Auto-generated method stub
        try {
            AbstractField field = fields.get(key);
            return field.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    public Vector getFieldValues(String key) {
        AbstractField field = fields.get(key);
        return field.getValues();
    }

    public void setFieldValue(String key, Object value) {
        // TODO Auto-generated method stub
        AbstractField field = fields.get(key);
        field.setValue(value);
    }

    public void addError(String err) {
    	error.add(err);
    }

    public void setFieldError(String key, String err) {
        AbstractField field = fields.get(key);
        field.addError(err);
    }

    public AbstractField getField(String key) {
        if (!fields.containsKey(key)) {
            return null;
        }
        return fields.get(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IForm#getError()
     */
    public ArrayList getErrors() {
        // TODO Auto-generated method stub
        return error;
    }

    public boolean validate() {
        // TODO Auto-generated method stub
        boolean valid = true;
        for (AbstractField field  : fields.values()) {
            field.clearErrors();
            field.validate();
            if (!field.isValid()) {
                status = Status.invalid;
            }
        }
        if (status == Status.invalid)
            return false;
        status = Status.valid;
        return valid;
    }

    public void reset() {
        status = Status.valid;
        error = new ArrayList<String>();
        for (AbstractField field : fields.values()) {
            field.clearErrors();
        }
    }
    
    public FormRPC clone(){
        FormRPC clone = new FormRPC();
        HashMap<String,AbstractField> cloneMap = (HashMap<String,AbstractField>)fields.clone();
        
        Object[] keys = (Object[]) ((Set)fields.keySet()).toArray();    
        for (int i = 0; i < keys.length; i++) {
            cloneMap.put((String)keys[i], fields.get((String)keys[i]).getInstance());
        }        
        
        clone.setFieldMap(cloneMap);
        clone.operation = operation;
        clone.status = status;
        clone.action = action;
        clone.userId = userId;
        clone.userName = userName;
        clone.fullName = fullName;
        clone.key = key;
        
        return clone;
    }
}
