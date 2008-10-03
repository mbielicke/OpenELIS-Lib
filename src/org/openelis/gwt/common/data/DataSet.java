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
import java.util.ArrayList;

public class DataSet extends ArrayList<DataObject> implements Serializable, DataObject{
    
    private static final long serialVersionUID = 1L;
    
    protected DataObject key;
    
    protected DataObject data;
    
    public boolean shown = true;
    
    public boolean enabled = true;
    
    public DataSet() {
        
    }
    
    public DataSet(DataObject key) {
        setKey(key);
    }
    
    public DataSet(DataObject key, DataObject value){
        setKey(key);
        add(value);
    }
    
    public DataSet(DataObject key, DataObject[] values){
        setKey(key);
        for(DataObject val : values){
            add(val);
        }
    }
    
    public void setKey(DataObject key){
        this.key = key;
    }
    
    public DataObject getKey() {
        return key;
    }
    
    public DataSet getInstance() {
        DataSet clone = new DataSet();
        for(int i=0; i < size(); i++){
            clone.add((DataObject)get(i).getInstance());
        }
        clone.key = key;
        clone.data = data;
        return clone;
    }
    
    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object object) {
        // TODO Auto-generated method stub
        
    }

    public int compareTo(Object obj) {
        if(!(obj instanceof DataSet))
            return -1;
        DataSet comp = (DataSet)obj;
        if(comp.key != null){
            if(comp.key.equals(key))
                return 0;
            return -1;
        }
        if(comp.size() != size())
            return -1;
        for(int i=0; i < size(); i++){
            if(!comp.get(i).equals(get(i)))
                return -1;
        }
        return 0;
    }

    public DataObject getData() {
        return data;
    }

    public void setData(DataObject data) {
        this.data = data;
    }

}
