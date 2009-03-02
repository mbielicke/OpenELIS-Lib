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
package org.openelis.gwt.common;


import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.screen.ScreenBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


/**
 * @author tschmidt
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Form extends AbstractField<String> implements FieldType {

    private static final long serialVersionUID = 1L;
    
    public transient Node node;
    
    
    public HashMap<String,AbstractField> fields = new HashMap<String,AbstractField>();
    public enum Status {valid,invalid}
    public Status status;
    public ArrayList<String> error = new ArrayList<String>();
    public boolean load;

    public Form() {
    }
    
    public Form(Node node) {
        this.node = node;
        createFields(node);
    }
    
    public void createFields(Node node){
        this.node = node;
        NodeList fieldList = node.getChildNodes();
        for (int j = 0; j < fieldList.getLength(); j++) {
            if (fieldList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                String key = fieldList.item(j).getAttributes().getNamedItem("key").getNodeValue();
                if(fields.containsKey(key)){
                    fields.get(key).setAttributes(fieldList.item(j));
                }else{
                    AbstractField field = ScreenBase.createField(fieldList.item(j));
                    fields.put(field.key,field);
                }
            }
        }
        key = node.getAttributes().getNamedItem("key").getNodeValue();
        if(node.getAttributes().getNamedItem("load") != null){
            if(node.getAttributes().getNamedItem("load").getNodeValue().equals("true"))
                load = true;
        }
    }
    
    public void setAttributes(Node node) {
        this.node = node;
        createFields(node);
    }
    
    public void setFieldMap(HashMap<String,AbstractField> fields) {
        this.fields = fields;
    }

    public HashMap<String,AbstractField> getFieldMap() {
        return fields;
    }

    public Object getFieldValue(String key) {
        // TODO Auto-generated method stub
        try {
            if(key.indexOf(":") > -1){
                String rpc = key.substring(0,key.indexOf(":"));
                String field = key.substring(key.indexOf(":")+1,key.length());
                return ((Form)fields.get(rpc)).getFieldValue(field);
            }
            AbstractField field = getField(key);
            return field.getValue();
        } catch (Exception e) {
            return null;
        }
    }

    public void setFieldValue(String key, Object value) {
        // TODO Auto-generated method stub
        if(key.indexOf(":") > -1){
            String rpc = key.substring(0,key.indexOf(":"));
            String field = key.substring(key.indexOf(":")+1,key.length());
            ((Form)getField(rpc)).setFieldValue(field,value);
            return;
        }
        AbstractField field = getField(key);
        if(field != null)
            field.setValue(value);
    }

    public void addError(String err) {
    	error.add(err);
    }

    public void setFieldError(String key, String err) {
        if(key.indexOf(":") > -1){
            String rpc = key.substring(0,key.indexOf(":"));
            String field = key.substring(key.indexOf(":")+1,key.length());
            ((Form)getField(rpc)).setFieldError(field,err);
            return;
        }
        AbstractField field = getField(key);
        field.addError(err);
    }

    public AbstractField getField(String key) {
        if(key.indexOf(":") > -1){
            String rpc = key.substring(0,key.indexOf(":"));
            String field = key.substring(key.indexOf(":")+1,key.length());
            return ((Form)getField(rpc)).getField(field);
        }
        if (!fields.containsKey(key)) {
            return null;
        }
        return fields.get(key);
    }
    
    public void setField(String key, AbstractField field){
        if(key.indexOf(":") > -1){
            String rpc = key.substring(0,key.indexOf(":"));
            String fieldkey = key.substring(key.indexOf(":")+1,key.length());
            ((Form)getField(rpc)).setField(fieldkey,field);
            return;
        }
        fields.put(key, field);
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uiowa.uhl.inmsp.interfaces.IForm#getError()
     */
    public ArrayList<String> getErrors() {
        // TODO Auto-generated method stub
        return error;
    }

    public void validate() {
        // TODO Auto-generated method stub
        boolean valid = true;
        for (AbstractField field  : fields.values()) {
            field.clearErrors();
            field.validate();
            if (!field.isValid()) {
                status = Status.invalid;
            }
        }
        if (status == Status.invalid)
            return;
        status = Status.valid;
        return;
    }

    public void removeErrors() {
        status = Status.valid;
        error = new ArrayList<String>();
        for (AbstractField field : fields.values()) {
            field.clearErrors();
        }
    }
    
    public Object clone(){
        Form clone = new Form();
        
        Object[] keys = (Object[]) ((Set)fields.keySet()).toArray();    
        for (int i = 0; i < keys.length; i++) {
            if(fields.get((String)keys[i]) instanceof Form)
                clone.setField((String)keys[i], (Form)fields.get((String)keys[i]).clone());
            else
                clone.setField((String)keys[i], (AbstractField)fields.get((String)keys[i]).clone());
        }        
        
        clone.status = status;
        
        return clone;
    }
    
    public AbstractField createField(Node node, String key) {
        NodeList fields = node.getChildNodes();
        for(int i = 0; i < fields.getLength(); i++){
            if(fields.item(i).getNodeType() == Node.ELEMENT_NODE){
                if(fields.item(i).getAttributes().getNamedItem("key").getNodeValue().equals(key))
                    return (AbstractField)ScreenBase.createField(fields.item(i));
            }
        }
        return null;
    }
    
    public HashMap<String,Node> createNodeMap(Node node) {
        HashMap<String,Node> nmap = new HashMap<String,Node>();
        NodeList fields = node.getChildNodes();
        for(int i = 0; i < fields.getLength(); i++){
            if(fields.item(i).getNodeType() == Node.ELEMENT_NODE){
                String key = fields.item(i).getAttributes().getNamedItem("key").getNodeValue();
                nmap.put(key, fields.item(i));
            }
        }
        return nmap;
    }
}
