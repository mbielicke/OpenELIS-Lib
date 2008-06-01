/*
 * Created on Mar 24, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;


import org.openelis.gwt.common.data.AbstractField;

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
    public int status;
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

    public HashMap getFieldMap() {
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
        if (operation == DISPLAY_NEW || operation == DISPLAY
            || operation == DISPLAY_UPDATE) {
            status = VALID_FORM;
            return true;
        }
        boolean valid = true;
        for (AbstractField field  : fields.values()) {
            field.clearErrors();
            field.validate();
            if (!field.isValid()) {
                status = INVALID_FORM;
            }
        }
        if (status == INVALID_FORM)
            return false;
        status = VALID_FORM;
        return valid;
    }

    public void reset() {
        status = IForm.VALID_FORM;
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
            cloneMap.put((String)keys[i], (AbstractField)fields.get((String)keys[i]).getInstance());
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
