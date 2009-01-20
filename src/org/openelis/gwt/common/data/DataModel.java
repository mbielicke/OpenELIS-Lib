/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.common.data;

import java.util.ArrayList;
import java.util.HashMap;

public class DataModel extends ArrayList<DataSet> implements Data {
    
    private static final long serialVersionUID = 1L;
    
    private ArrayList<Data> deleted = new ArrayList<Data>();
    
    public ArrayList<Integer> selections = new ArrayList<Integer>();
    
    private HashMap<Data,DataSet> keyMap = new HashMap<Data,DataSet>(); 
    
    private DataSet defaultSet;
    
    public int selected = -1;
    private int page = 0;
    private boolean selectLast;
    public boolean multiSelect;
    
    public boolean add(DataSet set){
        return add(set.getKey(),set);
    }
    
    public boolean add(Data key, DataObject value){
        if(value instanceof DataSet)
            return add(key,(DataSet)value);
        DataSet set = new DataSet();
        set.setKey(key);
        set.add(value);
        return add(key,set);
    }
    
    public boolean add(Data key, DataObject[] objects) {
        DataSet set = new DataSet();
        for(int i = 0; i < objects.length; i++){
            set.add(objects[i]);
        }
        set.setKey(key);
        return add(key,set);
    }
    
    public boolean add(Data key, DataSet set){
        keyMap.put(key,set);
        return super.add(set);
    }
    
    public DataSet getByKey(Data key) {
        return keyMap.get(key);
    }
    
    public void delete(int index) {
        keyMap.remove(get(index).key);
        deleted.add(remove(index));
    }
    
    public void delete(DataSet set){
        keyMap.remove(set.getKey());
        deleted.add(set);
        remove(set);
    }
    
    public void setDefaultSet(DataSet set) {
        defaultSet = set;
    }
    
    public DataSet createNewSet() {
        return (DataSet)defaultSet.clone();
    }
    
    public void addDefault() {
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
        page = 0;
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
    
    public Object clone() {
        DataModel clone = new DataModel();
        clone.page = page;
        clone.selected = selected;
        clone.selectLast = selectLast;
        if(defaultSet != null)
            clone.defaultSet = (DataSet)defaultSet.clone();
        
        for(int i = 0; i < size(); i++){
            clone.add((DataSet)get(i).clone());
        }
        return clone;
    }
    
    public ArrayList<Data> getDeletions() {
        return deleted;
    }

}
