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

import com.google.gwt.xml.client.Node;

import java.util.HashMap;
/**
 * @author tschmidt
 * 
 * CheckField is an implementation of AbstractField that represents
 * data used for Checkboxes.
 */
public class CheckField extends AbstractField<String> implements FieldType {

    private static final long serialVersionUID = 1L;
    
    public static final String TAG_NAME = "rpc-check";

    /** 
     * Default contstructor that sets up a StringObject for storing this
     * fields value
     *
     */
    public CheckField() {
        super();
    }
    
    /**
     * A constructor that excepts an intializing value the is a String with values
     * of either "Y" or "N"
     * @param val
     */
    public CheckField(String val) {
        super(val);
    }
    
    /**
     * A constructor that accpets a XML definition of this field from a screen
     * to set it's parameters
     * @param node
     */
    public CheckField(Node node){
        this();
        setAttributes(node);
    }
   
    /*
    public void setAttributes(Node node){        
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
    */
    
    public void setAttributes(HashMap<String,String> attribs) {
        if (attribs.containsKey("key"))
            setKey(attribs.get("key"));
        if (attribs.containsKey("required"))
            setRequired(new Boolean(attribs.get("required")));
        if (attribs.containsKey("value"))
            setValue(attribs.get("value"));
    }
    
    /**
     * This method will be used to validate the field for submission to the server.
     */
    public void validate() {
        if (required) {
            if (value == null) {
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

    /**
     * Hard coded to reuturn true always for checkboxes
     */
    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Returns the string value of this field as either "Y" or "N"
     */
    public String toString() {
       return value;
    }

    /**
     * Returns true if the value of the field is "Y" and false for null or "N"
     * @return
     */
    public boolean isChecked() {
        return (value != null && "Y".equals(value));
    }

    /**
     * Will create a new CheckField object and set all of the values and members the 
     * same the calling object.
     */
    public Object clone() {
        CheckField obj = new CheckField();
        obj.setRequired(required);
        obj.setValue(value);
        obj.setKey(key);
        return obj;
    }
    
    /*I don't think this is used anymore.
    public CheckField getInstance(Node node) {
        return new CheckField(node);
    }
    */
    
    /**
     * Returns the value of this Checkfield by returning the value of the wrapped
     * StringField
     */
    public String getValue() {
        if("".equals(value))
            return null;
        else
            return value;
    }
}
