/*
 * Created on Mar 22, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;

import com.google.gwt.xml.client.Node;

import java.io.Serializable;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class StringField extends AbstractField implements Serializable {
    private String value;
    private Integer min;
    private Integer max;

    public boolean isValid() {
        if (required) {
            if (value == null || value.length() == 0) {
                addError("Field is required");
                return false;
            }
        }
        if (value != null && !isInRange()) {
            return false;
        }
        return true;
    }

    public boolean isInRange() {
        if (value == null)
            return true;
        if (max != null && ((String)value).length() > ((Integer)max).intValue()) {
            addError("Field exceeded maximum length");
            return false;
        }
        if (min != null && ((String)value).length() < ((Integer)min).intValue()
            && ((String)value).length() > 0) {
            addError("Field is below minimum length");
            return false;
        }
        return true;
    }

    public void setValue(Object val) {
        value = (String)val;
    }

    public Object getValue() {
        return value;
    }

    public String toString() {
        if (value == null)
            return "";
        return (String)value;
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
        obj.setValue(value);
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
