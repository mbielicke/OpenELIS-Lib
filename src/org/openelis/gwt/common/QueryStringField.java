package org.openelis.gwt.common;

import com.google.gwt.xml.client.Node;

public class QueryStringField extends QueryField {

    public Object getInstance(Node field) {
        QueryStringField string = new QueryStringField();
        if(field.getAttributes().getNamedItem("key") != null)
            string.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
        return string;
    }
}
