/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.common.data;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.xml.client.Node;

/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class NumberField extends AbstractField {

    private static final long serialVersionUID = 1L;
    private Double max;
    private Double min;
    private String pattern;
    
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
        if (node.getAttributes().getNamedItem("reset") != null)
            setAllowReset(new Boolean(node.getAttributes()
                                                .getNamedItem("reset")
                                                .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            setValue(node.getFirstChild().getNodeValue());
        }
        if (node.getAttributes().getNamedItem("pattern") != null){
            setFormat(node.getAttributes().getNamedItem("pattern").getNodeValue());
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
        return format();
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

    public Object clone() {
        NumberField obj = new NumberField();
        obj.setMax(max);
        obj.setMin(min);
        obj.setRequired(required);
        obj.setValue(getValue());
        obj.setType(((NumberObject)object).type);
        obj.setKey(key);
        obj.setAllowReset(allowReset);
        return obj;
    }

    public NumberField getInstance(Node node) {
        return new NumberField(node);
    }
    
    public String format() {
        if(((NumberObject)object).value == null)
            return "";
        if(pattern != null)
            return NumberFormat.getFormat(pattern).format(((NumberObject)object).value);
        return String.valueOf(((NumberObject)object).value);
    }
    
    public void setFormat(String pattern) {
        this.pattern = pattern;
    }
        
    public void setValue(Object val) {
        if(!(val instanceof String) || (pattern == null || val == null) || "".equals(val)){
            super.setValue(val);
            return;
        }
        try {
            super.setValue(Double.parseDouble((String)val));
        }catch(Exception e){
            super.setValue(new Double(NumberFormat.getFormat(pattern).parse((String)val)));
        }
    }
    
}
