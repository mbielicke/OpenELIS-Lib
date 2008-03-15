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

    
    public NumberField() {
        object = new NumberObject();
    }
    
    public NumberField(Node node){
        this();
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("type") != null)
            ((NumberObject)object).setType(node.getAttributes()
                                .getNamedItem("type")
                                .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
        if (node.getAttributes().getNamedItem("max") != null)
            setMax(new Double(node.getAttributes()
                                          .getNamedItem("max")
                                          .getNodeValue()));
        if (node.getAttributes().getNamedItem("min") != null)
            setMin(new Double(node.getAttributes()
                                          .getNamedItem("min")
                                          .getNodeValue()));
    }
    
    public void validate() {
        if (((NumberObject)object).invalid){
            valid = false;
            addError("Field must be numeric");
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
            addError("Field exceeded maximum value");
            return false;
        }
        if (min != null && ((NumberObject)object).value.doubleValue() < min.doubleValue()) {
            addError("Field is below minimum value");
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

    
    public Object getInstance(Node node) {
        return new NumberField(node);
    }
    
}
