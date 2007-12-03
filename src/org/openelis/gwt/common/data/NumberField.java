/*
 * Created on Apr 24, 2006
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;

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
    private NumberObject number = (NumberObject)object;
    
    public NumberField() {
        object = new NumberObject();
    }
    
    public boolean isValid() {
        if (invalid)
            return false;
        if (required) {
            if (number.value == null) {
                addError("Field is required");
                return false;
            }
        }
        if (number.value != null && !isInRange()) {
            return false;
        }
        return true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        if (object.getValue() == null)
            return true;
        if (max != null && number.value.doubleValue() > max.doubleValue()) {
            addError("Field exceeded maximum length");
            return false;
        }
        if (min != null && number.value.doubleValue() < min.doubleValue()) {
            addError("Field is below minimum length");
            return false;
        }
        return true;
    }

    public String toString() {
        if (number.value == null)
            return "";
        if (number.type.equals("integer"))
            return "" + number.value.intValue();
        return number.value.toString();
    }

    public void setType(String type) {
        number.type = type;
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
        obj.setDataObject(object);
        return obj;
    }

    
    public Object getInstance(Node field) {
        NumberField number = new NumberField();
        if (field.getAttributes().getNamedItem("key") != null)
            number.setKey(field.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (field.getAttributes().getNamedItem("type") != null)
            number.number.setType(field.getAttributes()
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
