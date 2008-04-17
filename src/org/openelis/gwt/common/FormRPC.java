/*
 * Created on Mar 24, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;


import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DateField;
import org.openelis.gwt.common.data.OptionField;
import org.openelis.gwt.common.data.StringField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class FormRPC implements IForm, Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * @gwt.typeArgs <java.lang.String, org.openelis.gwt.common.data.AbstractField>
     */
    private HashMap fields = new HashMap();
    public int operation;
    public int status;
    public ArrayList error = new ArrayList();
    public String action;
    public Integer userId;
    public String userName;
    public String fullName;
    public String key;

    public FormRPC() {
    }

    public void setFieldMap(HashMap fields) {
        this.fields = fields;
    }

    public HashMap getFieldMap() {
        return fields;
    }

    public Object getFieldValue(String key) {
        // TODO Auto-generated method stub
        try {
            AbstractField field = (AbstractField)fields.get(key);
            return field.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    public Vector getFieldValues(String key) {
        AbstractField field = (AbstractField)fields.get(key);
        return field.getValues();
    }

    public void setFieldValue(String key, Object value) {
        // TODO Auto-generated method stub
        AbstractField field = (AbstractField)fields.get(key);
        field.setValue(value);
    }

    public void addError(String err) {
    	error.add(err);
    }

    public void setFieldError(String key, String err) {
        AbstractField field = (AbstractField)fields.get(key);
        field.addError(err);
    }

    public AbstractField getField(String key) {
        if (!fields.containsKey(key)) {
            return null;
        }
        Object field = fields.get(key);
        if (field instanceof OptionField) {
            return (OptionField)field;
        }
        if (field instanceof DateField) {
            return (DateField)field;
        }
        if (field instanceof StringField) {
            return (StringField)field;
        }
        return (AbstractField)field;
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
        Iterator keyIt = fields.keySet().iterator();
        while (keyIt.hasNext()) {
            String fieldKey = (String)keyIt.next();
            //if (fieldKey.endsWith("Q"))
            //    continue;
            AbstractField field = (AbstractField)fields.get(fieldKey);
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
        error = new ArrayList();
        Iterator keyIt = fields.keySet().iterator();
        while (keyIt.hasNext()) {
            String fieldKey = (String)keyIt.next();
            AbstractField field = (AbstractField)fields.get(fieldKey);
            field.clearErrors();
        }
    }
}
