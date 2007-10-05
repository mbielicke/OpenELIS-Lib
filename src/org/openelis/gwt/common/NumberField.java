/*
 * Created on Apr 24, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common;

import com.google.gwt.xml.client.Node;

import java.io.Serializable;

;
/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NumberField extends AbstractField implements Serializable {
    private String type;
    private Double value;
    private Double max;
    private Double min;
    private boolean invalid;

    public boolean isValid() {
        if (invalid)
            return false;
        if (required) {
            if (value == null) {
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
        // TODO Auto-generated method stub
        if (value == null)
            return true;
        if (max != null && value.doubleValue() > max.doubleValue()) {
            addError("Field exceeded maximum length");
            return false;
        }
        if (min != null && value.doubleValue() < min.doubleValue()) {
            addError("Field is below minimum length");
            return false;
        }
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        try {
            if (val != null) {
                if (val instanceof String && !((String)val).equals(""))
                    this.value = Double.valueOf((String)val);
                if (val instanceof Double)
                    this.value = (Double)val;
                if (val instanceof Integer)
                    this.value = new Double(((Integer)val).doubleValue());
            } else {
                this.value = null;
            }
            invalid = false;
        } catch (Exception e) {
            invalid = true;
            addError("Invalid number format");
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IField#getValue()
     */
    public Object getValue() {
        // TODO Auto-generated method stub
        if (value == null)
            return null;
        if (type.equals("integer")) {
            return new Integer(value.intValue());
        }
        return value;
    }

    public String toString() {
        if (value == null)
            return "";
        if (type.equals("integer"))
            return "" + value.intValue();
        return value.toString();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMin(Object min) {
        this.min = (Double)min;
    }

    public void setMax(Object max) {
        this.max = (Double)max;
    }

    public Object getInstance() {
        NumberField obj = new NumberField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setType(type);
        obj.setRequired(required);
        obj.setValue(value);
        return obj;
    }

    public Object getInstance(Node field) {
        NumberField number = new NumberField();
        if (field.getAttributes().getNamedItem("key") != null)
            number.setKey(field.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (field.getAttributes().getNamedItem("type") != null)
            number.setType(field.getAttributes()
                                .getNamedItem("type")
                                .getNodeValue());
        if (field.getAttributes().getNamedItem("required") != null)
            number.setRequired(new Boolean(field.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (field.getAttributes().getNamedItem("max") != null)
            number.setMax(new Double(field.getAttributes()
                                          .getNamedItem("max")
                                          .getNodeValue()));
        if (field.getAttributes().getNamedItem("min") != null)
            number.setMin(new Double(field.getAttributes()
                                          .getNamedItem("min")
                                          .getNodeValue()));
        return number;
    }
}
