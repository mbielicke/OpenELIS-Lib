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
import java.util.Iterator;

public class TreeDataModel extends ArrayList<TreeDataItem> implements DataObject, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private HashMap<Integer,TreeDataItem> itemMap = new HashMap<Integer,TreeDataItem>();
    
    private HashMap<DataObject,TreeDataItem> keyMap = new HashMap<DataObject,TreeDataItem>();
    
    public ArrayList<Integer> selections = new ArrayList<Integer>();
    
    private ArrayList<TreeDataItem> deleted = new ArrayList<TreeDataItem>();
    
    private int hashIndex = 0;
    
    public int selected = -1;
    private int page = 0;
    private boolean selectLast;
    public boolean multiSelect;
        

    public boolean add(TreeDataItem item) {
        return add(item.getKey(),item);
    }
    
    public void add(DataObject key, DataObject value){
        TreeDataItem item = new TreeDataItem();
        item.setKey(key);
        item.add(value);
        add(key,item);
    }
    
    public void add(DataObject key, DataObject[] values){
        TreeDataItem item = new TreeDataItem();
        item.setKey(key);
        for(int i = 0; i < values.length; i++){
            item.add(values[i]);
        }
        add(key,item);
    }
    
    public boolean add(DataObject key, TreeDataItem item) {
        keyMap.put(item.getKey(), item);
        if(item.hash < 0){
            item.hash = hashIndex;
            hashIndex++;
        }
        return super.add(item);
    }
    
    public void delete(int index){
        keyMap.remove(get(index).key);
        deleted.add(remove(index));        
    }
    
    public void select(int selection) throws IndexOutOfBoundsException {
        //if(selection > size())
          //  throw new IndexOutOfBoundsException();
        selected = selection;
        if(!multiSelect)
            selections.clear();
        selections.add(new Integer(selection));
    }
    
    public void unselect(int index) {
        selections.remove(new Integer(index));
        if(selected == index)
            selected = -1;
    }
    
    public int getSelectedIndex() {
        return selections.get(0);
    }
    
    public TreeDataItem getSelected() {
        return itemMap.get(selected);
    }
    
    public ArrayList<TreeDataItem> getSelections() {
        ArrayList<TreeDataItem> selectionSets = new ArrayList<TreeDataItem>();
        for(int i : selections) 
            selectionSets.add(itemMap.get(i));
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
        keyMap.clear();
        itemMap.clear();
        selections.clear();
        selected = -1;
    }
    
    public void clearSelections() {
        selections.clear();
        selected = -1;
    }
    
    public void selecttLast(boolean last){
        this.selectLast = last;    
    }
    
    public boolean isSelectLast(){
        return selectLast;
    }
    
    public Object getInstance() {
        TreeDataModel clone = new TreeDataModel();
        clone.page = page;
        clone.selected = selected;
        clone.selectLast = selectLast;
        for(int i = 0; i < size(); i++){
            clone.add((TreeDataItem)get(i).getInstance());
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
    
    public void swapItems(int index1, int index2){
        TreeDataItem item = remove(index2);
        set(index2, remove(index1));
        set(index1,item);
    }
    
    public TreeDataItem createTreeItem(DataObject key) {
        TreeDataItem item = new TreeDataItem();
        item.setKey(key);
        item.hash = hashIndex;
        hashIndex++;
        keyMap.put(key,item);
        itemMap.put(item.hashCode(), item);
        return item;
    }

    /*
    public ArrayList<TreeDataItem> getTreeItems(int start, int numItems){
        ArrayList<TreeDataItem> retTree = new ArrayList<TreeDataItem>();
        for(int i = start; i < start+numItems; i++){
            retTree.add(rows.get(i));
        }
        return retTree;
    }
    */
    
    public ArrayList<TreeDataItem> getVisibleRows() {
        ArrayList<TreeDataItem> rows = new ArrayList<TreeDataItem>();
        Iterator<TreeDataItem> it = iterator();
        while(it.hasNext())
            checkChildItems(it.next(), rows);
        return rows;
    }
    
    public void checkChildItems(TreeDataItem item, ArrayList<TreeDataItem> rows){
        rows.add(item);
        if(item.open && item.size() > 0) {
           Iterator<TreeDataItem> it = item.getItems().iterator();   
           while(it.hasNext())
               checkChildItems(it.next(), rows);
        }
    }

    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return 0;
    }

}
