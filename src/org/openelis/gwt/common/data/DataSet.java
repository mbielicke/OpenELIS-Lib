package org.openelis.gwt.common.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSet implements Serializable {
    
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.DataObject>
     */
    private ArrayList objects = new ArrayList();
    
    public void addObject(DataObject object) {
        objects.add(object);
    }

    public void setObject(int index, DataObject object) {
        objects.set(index, object);
    }

    public DataObject getObject(int index) {
        return (DataObject)objects.get(index);
    }

    public void removeObject(int index) {
        objects.remove(index);
    }

    public int size() {
        return objects.size();
    }

}
