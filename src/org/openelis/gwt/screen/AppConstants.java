/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import org.openelis.gwt.common.data.ConstantMap;

import java.util.Iterator;

public class AppConstants {
    
    private ConstantMap map;
    
    public AppConstants() {
        
    }
    
    public String get(String key) {
        return map.get(key);
    }
    
    public void setMap(ConstantMap map) {
        this.map = map;
    }
    
    public void addMapp(ConstantMap addMap) {
        if(map == null)
            setMap(addMap);
        else{
            Iterator keyIt = addMap.keySet().iterator();
            while(keyIt.hasNext()){
                String key = (String)keyIt.next();
                map.put(key, addMap.get(key));
            }
        }
    }
    

}
