package org.openelis.gwt.common;

import com.google.gwt.xml.client.Node;

import java.io.Serializable;
import java.util.Vector;

public class AbstractField implements IField, Serializable {
    /**
     * @gwt.typeArgs <java.lang.String>
     */
    protected Vector errors = new Vector();
    protected boolean required;
    protected String key;

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }

    public void addError(String err) {
        errors.add(err);
    }

    public String[] getErrors() {
        String[] retErrors = new String[errors.size()];
        for (int i = 0; i < errors.size(); i++)
            retErrors[i] = (String)errors.get(i);
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
        errors = new Vector();
    }

    public Object getInstance(Node node) {
        return null;
    }

    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getInstance() {
        // TODO Auto-generated method stub
        return null;
    }
}
