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
package org.openelis.gwt.common.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

public class DataMap extends HashMap<String,DataObject> implements DataObject, Serializable{
    
    private static final long serialVersionUID = 1L;

    public Object getInstance() {
        DataMap dataMap = new DataMap();
        Iterator keyIt = keySet().iterator();
        while(keyIt.hasNext()){
            String key = (String)keyIt.next();
            dataMap.put(key, (DataObject)((DataObject)get(key)).getInstance());
        }
        return dataMap;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return this;
    }

    public void setValue(Object object) {
        // TODO Auto-generated method stub
         
    }

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

}
