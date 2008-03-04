package org.openelis.gwt.common.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class DataMap implements DataObject, Serializable{
    
    private static final long serialVersionUID = 1L;
    /**
     * @gwt.typeArgs <java.lang.String, org.openelis.gwt.common.data.DataObject>
     */
    private HashMap map = new HashMap();

    public Object getInstance() {
        DataMap dataMap = new DataMap();
        Iterator keyIt = map.keySet().iterator();
        while(keyIt.hasNext()){
            String key = (String)keyIt.next();
            dataMap.map.put(key, ((DataObject)map.get(key)).getInstance());
        }
        return dataMap;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return map;
    }

    public void setValue(Object object) {
        // TODO Auto-generated method stub
        map = (HashMap)object;
    }

}
