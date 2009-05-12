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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class TreeDataItem extends TableDataRow<Integer> implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public String leafType;

    private ArrayList<TreeDataItem> items = new ArrayList<TreeDataItem>();
    
    public TreeDataItem parent = null;
    
    public boolean loaded;
    
    public boolean open;
    
    public boolean lazy;
    
    public int depth;
        
    public int y;
    
    public int x;
    
    public int childIndex = -1;
  
    public boolean selected; 
  
    public TreeDataItem() {
        
    }
    
    public TreeDataItem(int size) {
        super(size);
    }
    
    public Object clone() {
        TreeDataItem clone = new TreeDataItem(size());
        for(int i=0; i < size(); i++){
            clone.cells[i] = (FieldType)cells[i].clone();
        }
        for(int i=0; i < items.size(); i++){
            clone.addItem((TreeDataItem)getItem(i).clone());
        }
        
        clone.key = key;
        clone.parent = parent;
        clone.leafType = leafType;
        clone.selected = selected;
        clone.childIndex = childIndex;
        clone.depth = depth;
        if(data!=null)
        clone.data = (FieldType)data.clone();
        return clone;
    }
    
    public void addItem(TreeDataItem item) {
        items.add(item);
        item.depth = depth+1;
        item.parent = this;
        item.increaseDepth();
        item.childIndex = items.size()-1;
        
    }
    
    public void addItem(int index, TreeDataItem item) {
        items.add(index, item);
        item.depth = depth+1;
        item.increaseDepth();
        item.parent = this;
        for(int i = 0; i < items.size(); i++)
            items.get(i).childIndex = i;
    }
    
    public void increaseDepth(){
        if(items.size() > 0){
            Iterator<TreeDataItem> it = items.iterator();
            while(it.hasNext()){
                TreeDataItem item = it.next();
                item.depth = depth+1;
                item.parent = this;
                item.increaseDepth();
            }
        }
    }
    
    public void setItem(int index, TreeDataItem item){
        items.add(index,item);
    }
    
    public TreeDataItem getItem(int index) {
        return items.get(index);
    }
    
    public void swapItems(int index1, int index2){
        TreeDataItem item = items.remove(index2);
        items.set(index2, items.remove(index1));
        items.set(index1,item);
    }
    
    public void removeItem(int index) {
        items.remove(index);
        for(int i = 0; i < items.size(); i++) 
            items.get(i).childIndex = i;
    }
    
    public int shownItems() {
        int shown = 0;
        if(open){
            for(TreeDataItem item : items) {
                shown++;
                shown += item.shownItems();
            }
        }
        return shown;
    }
    
    public ArrayList<TreeDataItem> getItems() {
        return items;
    }
    
    public void toggle() {
        open = !open;
        if(!open && items.size() > 0) {
            close();
        }
    }
    
    public void close() {
        Iterator<TreeDataItem> it = items.iterator();
        while(it.hasNext()){
            TreeDataItem item = it.next();
            item.open = false;
            if(items.size() > 0)
                item.close();
        }
    }
    
    public TreeDataItem getPreviousSibling() {
        if(childIndex < 1)
            return null;
        return parent.getItem(childIndex -1);

    }
    
    public TreeDataItem getNextSibling() {
        if(childIndex < 0 || childIndex == parent.getItems().size())
            return null;
        return parent.getItem(childIndex+1);
    }

    public boolean hasChildren() {
        return items.size() > 0 || (lazy && !loaded);
    }
    
    public TreeDataItem getLastChild() {
        if(items.size() > 0)
            return items.get(items.size()-1);
        return null;
    }
    
    public TreeDataItem getFirstChild() {
        if(items.size() > 0)
            return items.get(0);
        return null;
    }
    
    
}
