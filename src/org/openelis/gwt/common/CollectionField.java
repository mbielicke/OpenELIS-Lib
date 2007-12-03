package org.openelis.gwt.common;


import java.io.Serializable;
import java.util.ArrayList;

public class CollectionField extends AbstractField implements Serializable {
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.DataSet>
     */
    private ArrayList coll = new ArrayList();

    public boolean isValid() {
        // TODO Auto-generated method stub
        return true;
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
