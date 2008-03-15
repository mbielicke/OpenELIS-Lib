package org.openelis.gwt.common.data;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;


public class QueryOptionField extends OptionField {

    private static final long serialVersionUID = 1L;
    private String type;

    public QueryOptionField() {
        super();
        setMulti(true);
    }
    
    public QueryOptionField(Node node){
        this();
        if(node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes().getNamedItem("key").getNodeValue());
        setType(node.getAttributes()
                            .getNamedItem("type")
                            .getNodeValue());
        setMulti(new Boolean(node.getAttributes()
                                         .getNamedItem("multi")
                                         .getNodeValue()).booleanValue());
        NodeList items = ((Element)node).getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Node item = items.item(i);
            addOption(item.getAttributes()
                                 .getNamedItem("value")
                                 .getNodeValue(), (item.getFirstChild() == null ? " " : item.getFirstChild()
                                                      .getNodeValue()));
        }
        
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public Object getInstance(Node node) {
        return new QueryOptionField();
    }
    
    public Object getInstance() {
        QueryOptionField obj = new QueryOptionField();
        obj.setRequired(required);
        obj.setMulti(multi);
        obj.setType(type);
        for (int i = 0; i < options.size(); i++) {
            OptionItem item = (OptionItem)options.get(i);
            obj.addOption(item.akey, item.display);
        }
        obj.setValue(value);
        return obj;
    }
}
