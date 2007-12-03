package org.openelis.gwt.common;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


public class QueryOptionField extends OptionField {

    private String type;

    public QueryOptionField() {
        super();
        setMulti(true);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Object getInstance(Node field) {
        QueryOptionField option = new QueryOptionField();
        if(field.getAttributes().getNamedItem("key") != null)
            option.setKey(field.getAttributes().getNamedItem("key").getNodeValue());
        option.setType(field.getAttributes()
                            .getNamedItem("type")
                            .getNodeValue());
        option.setMulti(new Boolean(field.getAttributes()
                                         .getNamedItem("multi")
                                         .getNodeValue()).booleanValue());
        NodeList items = ((Element)field).getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            option.addOption(item.getAttributes()
                                 .getNamedItem("value")
                                 .getNodeValue(), (item.getFirstChild() == null ? " " : item.getFirstChild()
                                                      .getNodeValue()));
        }
        return option;
    }
    
    public Object getInstance() {
        QueryOptionField obj = new QueryOptionField();
        obj.setRequired(required);
        obj.setMulti(multi);
        for (int i = 0; i < options.size(); i++) {
            OptionItem item = (OptionItem)options.get(i);
            obj.addOption(item.akey, item.display);
        }
        obj.setValue(value);
        return obj;
    }
}
