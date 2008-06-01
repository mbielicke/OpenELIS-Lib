package org.openelis.gwt.screen;

import org.openelis.gwt.common.data.ConstantMap;

import java.util.Iterator;

public class AppConstants {
    
    private ConstantMap map;
    
    public AppConstants() {
        
    }
    
    public String get(String key) {
        return map.getConstant(key);
    }
    
    public void setMap(ConstantMap map) {
        this.map = map;
    }
    
    public void addMapp(ConstantMap addMap) {
        if(map == null)
            setMap(addMap);
        else{
            Iterator keyIt = addMap.getMap().keySet().iterator();
            while(keyIt.hasNext()){
                String key = (String)keyIt.next();
                map.getMap().put(key, addMap.getConstant(key));
            }
        }
    }
    

}
