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

public class DataSet implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private ArrayList<DataObject> objects = new ArrayList<DataObject>();
    
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
    
    public DataSet getInstance() {
        DataSet clone = new DataSet();
        for(int i=0; i < size(); i++){
            clone.addObject((DataObject)getObject(i).getInstance());
        }
        clone.key = key;
        return clone;
    }
    
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        DataSet comp = (DataSet)obj;
        if(!(obj instanceof DataSet))
            return false;
        if(comp.key != null){
            if(comp.key.equals(key))
                return true;
            return false;
        }
        if(comp.size() != size())
            return false;
        for(int i=0; i < size(); i++){
            if(!comp.getObject(i).equals(getObject(i)))
                return false;
        }
        return true;
        }

}
