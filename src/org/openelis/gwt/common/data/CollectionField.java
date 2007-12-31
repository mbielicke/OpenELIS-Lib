package org.openelis.gwt.common.data;


import java.io.Serializable;
import java.util.ArrayList;

public class CollectionField extends AbstractField implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.DataObject>
     */
    private ArrayList coll = new ArrayList();

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
        obj.setValue(coll);
        return obj;
    }
}
