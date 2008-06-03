package org.openelis.gwt.common.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class DataMap implements DataObject, Serializable{
    
    private static final long serialVersionUID = 1L;

    private HashMap<String,DataObject> map = new HashMap<String,DataObject>();

    public Object getInstance() {
        DataMap dataMap = new DataMap();
        Iterator keyIt = map.keySet().iterator();
        while(keyIt.hasNext()){
            String key = (String)keyIt.next();
            dataMap.map.put(key, (DataObject)((DataObject)map.get(key)).getInstance());
        }
        return dataMap;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return map;
    }

    public void setValue(Object object) {
        // TODO Auto-generated method stub
        map = (HashMap<String,DataObject>)object;
    }

}
