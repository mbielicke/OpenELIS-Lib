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

public class TreeDataModel implements FieldType {
    
    private static final long serialVersionUID = 1L;
    
    public ArrayList<TreeDataItem> deleted = new ArrayList<TreeDataItem>();
    
    public ArrayList<TreeDataItem> list = new ArrayList<TreeDataItem>();
    
    public HashMap<String,TreeDataItem> leaves = new HashMap<String,TreeDataItem>();
        
    private int page = 0;
    private boolean selectLast;
    public boolean multiSelect;
        

    public boolean add(TreeDataItem item) {
        item.childIndex = list.size();
        item.depth = 0;
        item.parent = null;
        return list.add(item);
    }
    
    public void add(int index, TreeDataItem item) {
        item.parent = null;
        item.depth = 0;
        list.add(index, item);
        for(int i = 0; i < list.size(); i++)
            list.get(i).childIndex = i;
    }
    
    //public void add(Key key, DataObject value){
    //    TreeDataItem item = new TreeDataItem();
     //   item.setKey((DataObject)key);
      //  item.add(value);
       // add(key,item);
   // }
    
    //public void add(Data key, DataObject[] values){
      //  TreeDataItem item = new TreeDataItem();
        //item.setKey((DataObject)key);
        //for(int i = 0; i < values.length; i++){
          //  item.add(values[i]);
        //}
        //add(key,item);
   // }
    
    
    public void delete(int index){
        deleted.add(list.get(index));
        list.remove(index);
        for(int i = 0; i < list.size(); i++)
            list.get(i).childIndex = i;
    }
    
    /*
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
    */
    public int getPage(){
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public void clear() {
        list.clear();
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
        for(int i = 0; i < list.size(); i++){
            TreeDataItem itemClone = (TreeDataItem)list.get(i).clone();
            clone.add(itemClone);
        }
        return clone;
    }
    
    public void swapItems(int index1, int index2){
        TreeDataItem item = list.remove(index2);
        list.set(index2, list.remove(index1));
        list.set(index1,item);
    }
    
    public TreeDataItem createTreeItem(String leafType) {
        TreeDataItem item = (TreeDataItem)leaves.get(leafType).clone();
        return item;
    }
    
    public ArrayList<TreeDataItem> getDeletions() {
        return deleted;
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object obj) {
        // TODO Auto-generated method stub
        
    }

    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public int size() {
        return list.size();
    }
    
    public TreeDataItem get(int index) {
        return list.get(index);
    }

}
