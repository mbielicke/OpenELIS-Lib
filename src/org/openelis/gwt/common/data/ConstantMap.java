package org.openelis.gwt.common.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class ConstantMap implements DataObject, Serializable{
    
    private static final long serialVersionUID = 1L;
    /**
     * @gwt.typeArgs <java.lang.String, java.lang.String>
     */
    private HashMap map = new HashMap();

    public Object getInstance() {
        ConstantMap constMap = new ConstantMap();
        Iterator keyIt = map.keySet().iterator();
        while(keyIt.hasNext()){
            String key = (String)keyIt.next();
            constMap.map.put(key, (String)map.get(key));
        }
        return constMap;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return map;
    }

    public void setValue(Object object) {
        // TODO Auto-generated method stub
        map = (HashMap)object;
    }
    
    public String getConstant(String key){
        return (String)map.get(key);
    }

}
