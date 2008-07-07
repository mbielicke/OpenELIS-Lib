/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
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
    
    public static final String TAG_NAME = "rpc-number";

    
    public NumberField() {
        object = new NumberObject();
    }
    
    public NumberField(NumberObject.Type type) {
        object = new NumberObject();
        ((NumberObject)object).type = type;
    }
    
    public NumberField(Integer val){
        this(NumberObject.Type.INTEGER);
        setValue(val);
    }
    
    public NumberField(Double val){
        this(NumberObject.Type.DOUBLE);
        setValue(val);
    }
    
    public NumberField(Node node){
        this();
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("type") != null){
            if("integer".equals(node.getAttributes().getNamedItem("type").getNodeValue()))
            	((NumberObject)object).setType(NumberObject.Type.INTEGER);
            else if("double".equals(node.getAttributes().getNamedItem("type").getNodeValue()))
            	((NumberObject)object).setType(NumberObject.Type.DOUBLE);
        }
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
        if (node.hasChildNodes()) {
            setValue(node.getFirstChild().getNodeValue());
        }
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
        if (((NumberObject)object).type == NumberObject.Type.INTEGER)
            return "" + ((NumberObject)object).value.intValue();
        return ((NumberObject)object).value.toString();
    }

    public void setType(NumberObject.Type type) {
        ((NumberObject)object).type = type;
    }

    public void setMin(Object min) {
        this.min = (Double)min;
    }

    public void setMax(Object max) {
        this.max = (Double)max;
    }

    public NumberField getInstance() {
        NumberField obj = new NumberField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setRequired(required);
        obj.setValue(getValue());
        obj.setType(((NumberObject)object).type);
        return obj;
    }

    
    public NumberField getInstance(Node node) {
        return new NumberField(node);
    }
    
}
