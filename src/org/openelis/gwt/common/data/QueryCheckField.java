package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

public class QueryCheckField extends CheckField {

    private static final long serialVersionUID = 1L;
    
    public static final String TAG_NAME = "rpc-queryCheck";
    
    public QueryCheckField() {
        
    }
    
    public QueryCheckField(Node node){
        setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }

    public Object getInstance(Node node) {
        return new QueryCheckField(node);
    }

    public Object getInstance() {
        QueryCheckField obj = new QueryCheckField();
        obj.setRequired(required);
        obj.setValue(getValue());
        return obj;
    }
}
