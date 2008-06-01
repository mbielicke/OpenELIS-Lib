package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;


public class ModelField extends AbstractField {

    private static final long serialVersionUID = 1L;

    private DataModel value;
    public static final String TAG_NAME = "rpc-model";
    
    public ModelField() {
        
    }
    
    public ModelField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }

    public void validate() {
        // TODO Auto-generated method stub
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        if(val == null)
            value = null;
        else
            value = (DataModel)val;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return value;
    }

    public Object getInstance() {
        ModelField obj = new ModelField();
       
        obj.setRequired(required);
        obj.setValue(value.getInstance());
        obj.setRequired(required);
        obj.setTip(tip);
        obj.setKey(key);
        
        return obj;
    }

    public Object getInstance(Node node) {
       return new ModelField(node);
    }
}
