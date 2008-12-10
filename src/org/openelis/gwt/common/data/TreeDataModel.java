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
import java.util.Iterator;

public class TreeDataModel extends ArrayList<TreeDataItem> implements Data {
    
    private static final long serialVersionUID = 1L;
    
    private HashMap<Integer,TreeDataItem> itemMap = new HashMap<Integer,TreeDataItem>();
    
    private HashMap<Data,TreeDataItem> keyMap = new HashMap<Data,TreeDataItem>();
    
    public ArrayList<Integer> selections = new ArrayList<Integer>();
    
    private ArrayList<TreeDataItem> deleted = new ArrayList<TreeDataItem>();
    
    public HashMap<String,TreeDataItem> leaves = new HashMap<String,TreeDataItem>();
    
    private int hashIndex = 0;
    
    public int selected = -1;
    private int page = 0;
    private boolean selectLast;
    public boolean multiSelect;
        

    public boolean add(TreeDataItem item) {
        return add(item.getKey(),item);
    }
    
    public void add(int index, TreeDataItem item) {
        int topIndex = indexOf(itemMap.get(index));
        item.parent = null;
        keyMap.put(item.getKey(), item);
        setHash(item);
        super.add(topIndex,item);
    }
    
    public void add(Data key, DataObject value){
        TreeDataItem item = new TreeDataItem();
        item.setKey(key);
        item.add(value);
        add(key,item);
    }
    
    public void add(Data key, DataObject[] values){
        TreeDataItem item = new TreeDataItem();
        item.setKey(key);
        for(int i = 0; i < values.length; i++){
            item.add(values[i]);
        }
        add(key,item);
    }
    
    public boolean add(Data key, TreeDataItem item) {
        keyMap.put(item.getKey(), item);
        setHash(item);
        return super.add(item);
    }
    
    private void setHash(TreeDataItem item) {
        item.hash = hashIndex++;
        itemMap.put(item.hashCode(), item);
        for(TreeDataItem sub : item.getItems())
            setHash(sub);
    }
    
    public void delete(int index){
        TreeDataItem item = itemMap.get(index);
        if(item.parent != null){
            item.parent.getItems().remove(item);
            itemMap.remove(index);
        }else{
            keyMap.remove(item.key);
            deleted.add(item);
            remove(item);
        }
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
    
    public Object clone() {
        TreeDataModel clone = new TreeDataModel();
        clone.page = page;
        for(Integer sel : selections)
            clone.selections.add(sel);
        clone.selected = selected;
        clone.selectLast = selectLast;
        for(TreeDataItem leaf : leaves.values())
            clone.leaves.put(leaf.leafType,(TreeDataItem)leaf.clone()); 
        for(int i = 0; i < size(); i++){
            clone.add((TreeDataItem)get(i).clone());
        }
        return clone;
    }
    
    public void swapItems(int index1, int index2){
        TreeDataItem item = remove(index2);
        set(index2, remove(index1));
        set(index1,item);
    }
    
    public TreeDataItem createTreeItem(String leafType, Data key) {
        TreeDataItem item = (TreeDataItem)leaves.get(leafType).clone();
        item.setKey(key);
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
        if(item.hash < 0)
            setHash(item);
        if(item.open && item.shownItems() > 0) {
           Iterator<TreeDataItem> it = item.getItems().iterator();   
           while(it.hasNext())
               checkChildItems(it.next(), rows);
        }
    }
    
    public ArrayList<TreeDataItem> getDeletions() {
        return deleted;
    }

}
