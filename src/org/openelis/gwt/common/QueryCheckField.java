package org.openelis.gwt.common;

import com.google.gwt.xml.client.Node;

public class QueryCheckField extends org.openelis.gwt.common.data.CheckField {

    public Object getInstance(Node field) {
        QueryCheckField check = new QueryCheckField();
        check.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
        return check;
    }

    public Object getInstance() {
        QueryCheckField obj = new QueryCheckField();
        obj.setRequired(required);
        obj.setValue(getValue());
        return obj;
    }
}
