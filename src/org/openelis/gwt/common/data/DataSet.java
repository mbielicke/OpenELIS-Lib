package org.openelis.gwt.common.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataSet implements Serializable {
    
    private static final long serialVersionUID = 1L;
    /**
     * @gwt.typeArgs <org.openelis.gwt.common.data.DataObject>
     */
    private ArrayList objects = new ArrayList();
    
    private DataObject key;
    
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
    
    public void setKey(DataObject key){
        this.key = key;
    }
    
    public DataObject getKey() {
        return key;
    }
    
    public Object getInstance() {
        DataSet clone = new DataSet();
        for(int i=0; i < size(); i++){
            clone.addObject((DataObject)getObject(i).getInstance());
        }
        clone.key = key;
        return clone;
    }

}
