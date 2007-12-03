/*
 * Created on Mar 22, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;

import java.io.Serializable;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StringField extends AbstractField implements Serializable {
    
    protected StringObject string = (StringObject)object;
    private Integer min;
    private Integer max;
    
    public StringField() {
        object = new StringObject();
    }

    public boolean isValid() {
        if (required) {
            if (string.value == null || string.value.length() == 0) {
                addError("Field is required");
                return false;
            }
        }
        if (string.value != null && !isInRange()) {
            return false;
        }
        return true;
    }

    public boolean isInRange() {
        if (string.value == null)
            return true;
        if (max != null && (string.value).length() > ((Integer)max).intValue()) {
            addError("Field exceeded maximum length");
            return false;
        }
        if (min != null && (string.value).length() < ((Integer)min).intValue()
            && (string.value).length() > 0) {
            addError("Field is below minimum length");
            return false;
        }
        return true;
    }


    public String toString() {
        if (string.value == null)
            return "";
        return string.value;
    }

    public void setMin(Object min) {
        this.min = (Integer)min;
    }

    public void setMax(Object max) {
        this.max = (Integer)max;
    }

    public Object getInstance() {
        StringField obj = new StringField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setRequired(required);
        obj.setDataObject(string);
        return obj;
    }

    public Object getInstance(Node field) {
        StringField string = new StringField();
        if (field.getAttributes().getNamedItem("key") != null)
            string.setKey(field.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (field.getAttributes().getNamedItem("max") != null)
            string.setMax(new Integer(field.getAttributes()
                                           .getNamedItem("max")
                                           .getNodeValue()));
        if (field.getAttributes().getNamedItem("min") != null)
            string.setMin(new Integer(field.getAttributes()
                                           .getNamedItem("min")
                                           .getNodeValue()));
        if (field.getAttributes().getNamedItem("required") != null)
            string.setRequired(new Boolean(field.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (field.hasChildNodes()) {
            string.setValue(field.getFirstChild().getNodeValue());
        }
        return string;
    }
}
