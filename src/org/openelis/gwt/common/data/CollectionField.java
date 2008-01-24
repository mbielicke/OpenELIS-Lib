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

    public void validate() {
        // TODO Auto-generated method stub
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
    
    public Object getInstance(Node field) {
    CollectionField collection = new CollectionField();
    if (field.getAttributes().getNamedItem("key") != null)
    	collection.setKey(field.getAttributes()
                           .getNamedItem("key")
                           .getNodeValue());
    if (field.getAttributes().getNamedItem("type") != null)
    	collection.setType(field.getAttributes()
                           .getNamedItem("type")
                           .getNodeValue());
    if (field.getAttributes().getNamedItem("required") != null)
    	collection.setRequired(new Boolean(field.getAttributes()
                                            .getNamedItem("required")
                                            .getNodeValue()).booleanValue());
    return collection;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
