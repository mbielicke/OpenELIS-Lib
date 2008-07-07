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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.common.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class DataModel implements DataObject, Serializable {
    
    private static final long serialVersionUID = 1L;

    private ArrayList<DataSet> entries = new ArrayList<DataSet>();
    
    private HashMap<DataObject,DataSet> keyMap = new HashMap<DataObject,DataSet>(); 
    
    private int selected = -1;
    private int page = 0;
    private boolean selectLast;
    
    public int size() {
        return entries.size();
    }
    
    public void add(DataSet set) {
        entries.add(set);
        if(set.getKey() != null){
            keyMap.put(set.getKey(), set);
        }
    }
    
    public void add(DataObject key, DataObject value){
        DataSet data = new DataSet();
        data.setKey(key);
        data.addObject(value);
        add(data);
    }
    
    public void add(DataObject key, DataObject[] values){
        DataSet data = new DataSet();
        data.setKey(key);
        for(int i = 0; i < values.length; i++){
            data.addObject(values[i]);
        }
        add(data);
    }

    public void delete(int index) {
        entries.remove(index);
    }
    
    public void delete(DataObject key){
        entries.remove(keyMap.get(key));
        keyMap.remove(key);
    }
    
    public void delete(DataSet set){
        if(set.getKey() != null){
            keyMap.remove(set.getKey());
        }
        entries.remove(set);
    }

    public DataSet get(int index) {
        return (DataSet)entries.get(index);
    }
    
    public DataSet get(DataObject key){
        return (DataSet)keyMap.get(key);
    }
    
    public void select(int selection) throws IndexOutOfBoundsException {
        if(selection > entries.size())
            throw new IndexOutOfBoundsException();
        selected = selection;
    }
    
    public void select(DataObject key) {
        selected = entries.indexOf(keyMap.get(key));
    }
    
    public void select(DataSet set){
        selected = entries.indexOf(set);
    }
    
    public int getSelectedIndex() {
        return selected;
    }
    
    public DataSet getSelected() {
        return get(selected);
    }
    
    public int indexOf(DataObject key){
        return entries.indexOf(keyMap.get(key));
    }
    
    public int indexOf(DataSet set){
        return entries.indexOf(set);
    }
    
    public int getPage(){
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public void clear() {
        entries = new ArrayList<DataSet>();
        keyMap = new HashMap<DataObject,DataSet>();
        selected = -1;
    }
    
    public void selecttLast(boolean last){
        this.selectLast = last;    
    }
    
    public boolean isSelectLast(){
        return selectLast;
    }
    
    public Object getInstance() {
        DataModel clone = new DataModel();
        clone.page = page;
        clone.selected = selected;
        clone.selectLast = selectLast;
        for(int i = 0; i < size(); i++){
            clone.add((DataSet)get(i).getInstance());
        }
        return clone;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object object) {
        // TODO Auto-generated method stub
        
    }

}
