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
