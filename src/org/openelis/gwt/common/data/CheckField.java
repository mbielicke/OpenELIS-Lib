/*
 * Created on Mar 27, 2006
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
public class CheckField extends AbstractField implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static final String TAG_NAME = "rpc-check";

    public CheckField() {
        object = new StringObject();
    }
    
    public CheckField(Node node){
        this();
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                             .getNamedItem("key")
                             .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                              .getNamedItem("required")
                                              .getNodeValue()).booleanValue());
        if (node.hasChildNodes()) {
            setValue(node.getFirstChild().getNodeValue());
        }
    }
    
    public void validate() {
        if (required) {
            if (object.getValue()  == null) {
                addError("Field is required");
                valid =  false;
                return;
            }
        }
        if (!isInRange()) {
            valid = false;
            return;
        }
        valid = true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public String toString() {
       return (String)object.getValue();
    }

    public boolean isChecked() {
        return (object.getValue() != null && "Y".equals((String)object.getValue()));
    }

    public Object getInstance() {
        CheckField obj = new CheckField();
        obj.setRequired(required);
        obj.setValue(getValue());
        return obj;
    }

    public Object getInstance(Node node) {
        return new CheckField(node);
    }
    
    public Object getValue() {
    	String returnValue = (String)object.getValue();
    	if("".equals(returnValue))
    		return null;
    	else
    		return returnValue;
    }
}
