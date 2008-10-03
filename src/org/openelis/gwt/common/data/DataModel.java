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
import java.util.HashMap;

public class DataModel extends ArrayList<DataSet> implements DataObject, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ArrayList<DataObject> deleted = new ArrayList<DataObject>();
    
    public ArrayList<Integer> selections = new ArrayList<Integer>();
    
    private HashMap<DataObject,DataSet> keyMap = new HashMap<DataObject,DataSet>(); 
    
    private DataSet defaultSet;
    
    public int selected = -1;
    private int page = 0;
    private boolean selectLast;
    public boolean multiSelect;
    
    public boolean add(DataSet set){
        return add(set.getKey(),set);
    }
    
    public boolean add(DataObject key, DataObject value){
        if(value instanceof DataSet)
            return add(key,(DataSet)value);
        DataSet set = new DataSet();
        set.setKey(key);
        set.add(value);
        return add(key,set);
    }
    
    public boolean add(DataObject key, DataObject[] objects) {
        DataSet set = new DataSet();
        for(int i = 0; i < objects.length; i++){
            set.add(objects[i]);
        }
        return add(key,set);
    }
    
    public boolean add(DataObject key, DataSet set){
        keyMap.put(key,set);
        return super.add(set);
    }
    
    public DataSet getByKey(DataObject key) {
        return keyMap.get(key);
    }
    
    public void delete(int index) {
        keyMap.remove(get(index).key);
        deleted.add(remove(index));
    }
    
    public void setDefaultSet(DataSet set) {
        defaultSet = set;
    }
    
    public DataSet createNewSet() {
        return defaultSet.getInstance();
    }
    
    public void addDefualt() {
        add(createNewSet());
    }
            
    public void select(int selection) throws IndexOutOfBoundsException {
        if(selection > size())
            throw new IndexOutOfBoundsException();
        selected = selection;
        if(!multiSelect)
            selections.clear();
        selections.add(selection);
    }
            
    public void unselect(int index) {
        if(index == -1){
            selections.clear();
            selected = -1;
        }else{
            selections.remove(new Integer(index));
            if(selected == index)
                selected = -1;
        }
    }
    
    public int getSelectedIndex() {
        return selections.get(0);
    }
    
    public DataSet getSelected() {
        return get(selected);
    }
    
    public ArrayList<DataSet> getSelections() {
        ArrayList<DataSet> selectionSets = new ArrayList<DataSet>();
        for(int i : selections) 
            selectionSets.add(get(i));
        return selectionSets;
    }
   
    public int getPage(){
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public void clear() {
        super.clear();
        selected = -1;
        selections.clear();
        keyMap.clear();
    }
    
    public void clearSelections() {
        selected = -1;
        selections.clear();
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

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public ArrayList<DataObject> getDeletions() {
        return deleted;
    }

}
