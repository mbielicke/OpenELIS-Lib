package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Node;

public class QueryCheckField extends CheckField {

    private static final long serialVersionUID = 1L;

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
