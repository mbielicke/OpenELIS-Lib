package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

public class QueryStringField extends QueryField {


    private static final long serialVersionUID = 1L;
    
    public static final String TAG_NAME = "rpc-queryString";

    public QueryStringField() {
        
    }
    
    public QueryStringField(Node node){
        if(node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes().getNamedItem("key").getNodeValue());
    }
    
    public Object getInstance(Node node) {
        return new QueryStringField(node);
    }
    
    public Object getInstance() {
        return new QueryStringField();
       
    }
}
