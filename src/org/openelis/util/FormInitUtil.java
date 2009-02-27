package org.openelis.util;

import org.openelis.gwt.common.Form;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class FormInitUtil {
    
    public static void setForm(Form form, Node node){
        NodeList fieldList = node.getChildNodes();
        for (int j = 0; j < fieldList.getLength(); j++) {
            if (fieldList.item(j).getNodeType() == Node.ELEMENT_NODE) {
                String key = fieldList.item(j).getAttributes().getNamedItem("key").getNodeValue();
                if(form.fields.containsKey(key)){
                    form.fields.get(key).setAttributes(FormInitUtil.getAttributesMap(node));
                }
            }
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
    
}
