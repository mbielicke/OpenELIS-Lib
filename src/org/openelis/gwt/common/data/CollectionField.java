package org.openelis.gwt.common.data;


import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.xml.client.Node;

public class CollectionField extends AbstractField implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.DataObject>
     */
    private ArrayList coll = new ArrayList();
    private String type = "";

    public CollectionField() {
        
    }
    
    public CollectionField(Node node){
        if (node.getAttributes().getNamedItem("key") != null)
            setKey(node.getAttributes()
                               .getNamedItem("key")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("type") != null)
            setType(node.getAttributes()
                               .getNamedItem("type")
                               .getNodeValue());
        if (node.getAttributes().getNamedItem("required") != null)
            setRequired(new Boolean(node.getAttributes()
                                                .getNamedItem("required")
                                                .getNodeValue()).booleanValue());
    }
    
    public void validate() {
    	if (required) {
            if (coll.size() == 0) {
                addError("Field is required");
                valid = false;
                return;
            }
        }
        valid = true;
    }

    public boolean isInRange() {
        // TODO Auto-generated method stub
        return true;
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        coll = (ArrayList)val;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return coll;
    }

    public void addItem(Object item) {
        coll.add((String)item);
    }

    public Object getInstance() {
        CollectionField obj = new CollectionField();
        obj.setRequired(required);
        obj.setType(type);
        obj.setValue(coll);
        return obj;
    }
    
    public Object getInstance(Node node) {
        return new CollectionField(node);
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
