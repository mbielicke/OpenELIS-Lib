package org.openelis.gwt.common;

import com.google.gwt.xml.client.Node;

public class QueryCheckField extends CheckField {

    public Object getInstance(Node field) {
        QueryCheckField check = new QueryCheckField();
        check.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
        return check;
    }

}
