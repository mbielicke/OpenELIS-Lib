package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

public class QueryStringField extends QueryField {


    private static final long serialVersionUID = 1L;

    public Object getInstance(Node field) {
        QueryStringField string = new QueryStringField();
        if(field.getAttributes().getNamedItem("key") != null)
            string.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
        return string;
    }
    
    public Object getInstance() {
        return new QueryStringField();
       
    }
}
