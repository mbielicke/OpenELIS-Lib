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

public class TreeDataModel extends ArrayList<TreeDataItem> implements Data {
    
    private static final long serialVersionUID = 1L;
            
    public ArrayList<TreeDataItem> selections = new ArrayList<TreeDataItem>();
    
    private ArrayList<TreeDataItem> deleted = new ArrayList<TreeDataItem>();
    
    public HashMap<String,TreeDataItem> leaves = new HashMap<String,TreeDataItem>();
        
    private int page = 0;
    private boolean selectLast;
    public boolean multiSelect;
        

    public boolean add(TreeDataItem item) {
        return add(item.getKey(),item);
    }
    
    public void add(TreeDataItem item, TreeDataItem newItem) {
        item.parent = null;
        super.add(indexOf(item),newItem);
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
        return super.add(item);
    }
    
    public void delete(TreeDataItem item){
        if(item.parent != null){
            item.parent.getItems().remove(item);
        }
        deleted.add(item);
        remove(item);
    }
    
    public void select(TreeDataItem item) throws IndexOutOfBoundsException {
        if(!multiSelect)
            selections.clear();
        selections.add(item);
    }
    
    public void unselect(TreeDataItem item) {
        selections.remove(item);
    }
        
    public TreeDataItem getSelected() {
        return selections.get(0);
    }
    
    public ArrayList<TreeDataItem> getSelections() {
        return selections;
    }
    
    public int getPage(){
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public void clear() {
        super.clear();
        selections.clear();
    }
    
    public void clearSelections() {
        selections.clear();
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
        clone.selectLast = selectLast;
        for(TreeDataItem leaf : leaves.values())
            clone.leaves.put(leaf.leafType,(TreeDataItem)leaf.clone()); 
        for(int i = 0; i < size(); i++){
            TreeDataItem itemClone = (TreeDataItem)get(i).clone();
            clone.add(itemClone);
            if(selections.contains(get(i))){
                clone.selections.add(itemClone);
            }
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
    
    public ArrayList<TreeDataItem> getDeletions() {
        return deleted;
    }

}
