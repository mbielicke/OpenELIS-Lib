/*
 * Created on Apr 24, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common.data;

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

    private static final long serialVersionUID = 1L;
    private Double max;
    private Double min;
    private boolean invalid;
    
    public NumberField() {
        object = new NumberObject();
    }
    
    public void validate() {
        if (invalid){
            valid = false;
            return;
        }
        if (required) {
            if (((NumberObject)object).value == null) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        if (((NumberObject)object).value != null && !isInRange()) {
            valid = false;
            return;
        }
        valid = true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (object.getValue() == null)
            return true;
        if (max != null && ((NumberObject)object).value.doubleValue() > max.doubleValue()) {
            addError("Field exceeded maximum length");
            return false;
        }
        if (min != null && ((NumberObject)object).value.doubleValue() < min.doubleValue()) {
            addError("Field is below minimum length");
            return false;
        }
        return true;
    }

    public String toString() {
        if (((NumberObject)object).value == null)
            return "";
        if (((NumberObject)object).type.equals("integer"))
            return "" + ((NumberObject)object).value.intValue();
        return ((NumberObject)object).value.toString();
    }

    public void setType(String type) {
        ((NumberObject)object).type = type;
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
        obj.setRequired(required);
        obj.setValue(getValue());
        obj.setType(((NumberObject)object).type);
        return obj;
    }

    
    public Object getInstance(Node field) {
        NumberField number = new NumberField();
        if (field.getAttributes().getNamedItem("key") != null)
            number.setKey(field.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (field.getAttributes().getNamedItem("type") != null)
            ((NumberObject)number.object).setType(field.getAttributes()
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
