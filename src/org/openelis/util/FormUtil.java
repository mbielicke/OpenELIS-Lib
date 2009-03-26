package org.openelis.util;

import org.openelis.gwt.common.FieldErrorException;
import org.openelis.gwt.common.Form;
import org.openelis.gwt.common.FormErrorException;
import org.openelis.gwt.common.TableFieldErrorException;
import org.openelis.gwt.common.data.AbstractField;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class FormUtil {
    
    public static void setForm(Form<? extends Object> form, Node node){
        HashMap<String,Node> nodeMap = createNodeMap(node);
        for (AbstractField field : form.getFields()){
            field.setAttributes(getAttributesMap(nodeMap.get(field.key)));
        }
        form.key = node.getAttributes().getNamedItem("key").getNodeValue();
        if(node.getAttributes().getNamedItem("load") != null){
            if(node.getAttributes().getNamedItem("load").getNodeValue().equals("true"))
                form.load = true;
        }
    }
    
    public static HashMap<String,String> getAttributesMap(Node node) {
        HashMap<String,String> attribs = new HashMap<String,String>();
        for(int i = 0; i < node.getAttributes().getLength(); i++){
            attribs.put(node.getAttributes().item(i).getNodeName(), node.getAttributes().item(i).getNodeValue());
        }
        if (node.hasChildNodes()) {
            attribs.put("value",node.getFirstChild().getNodeValue());
        }
        return attribs;
    }
    
    public static HashMap<String,Node> createNodeMap(Node node) {
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
    
    public static HashMap<String,AbstractField> createFieldMap(Form<? extends Object> form) {
        HashMap<String,AbstractField> map = new HashMap<String,AbstractField>();
        for(AbstractField field : form.getFields()) {
            map.put(field.key,field);
        }
        return map;
    }
    /*
    public static setFormErrors(List exceptions, Form<? extends Object> form) {
        for (int i=0; i<exceptions.size();i++) {
            //if the error is inside the org contacts table
            if(exceptions.get(i) instanceof TableFieldErrorException){
                int rowindex = ((TableFieldErrorException)exceptions.get(i)).getRowIndex();
                adjustmentsTable.getField(rowindex,((TableFieldErrorException)exceptionList.get(i)).getFieldName())
                    .addError(openElisConstants.getString(((FieldErrorException)exceptionList.get(i)).getMessage()));

            //if the error is on the field
            }else if(exceptionList.get(i) instanceof FieldErrorException)
                form.getFieldMap().get(((FieldErrorException)exceptionList.get(i)).getFieldName()).addError(openElisConstants.getString(((FieldErrorException)exceptionList.get(i)).getMessage()));
            
            //if the error is on the entire form
            else if(exceptionList.get(i) instanceof FormErrorException)
                form.addError(openElisConstants.getString(((FormErrorException)exceptionList.get(i)).getMessage()));
            }        
        
        form.status = Form.Status.invalid;
    }
    */
    
}
