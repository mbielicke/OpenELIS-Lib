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
package org.openelis.gwt.widget.tree;

import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.Data;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.common.data.TreeDataModel;
import org.openelis.gwt.widget.tree.event.SourcesTreeModelEvents;
import org.openelis.gwt.widget.tree.event.TreeModelListener;
import org.openelis.gwt.widget.tree.event.TreeModelListenerCollection;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeModel implements SourcesTreeModelEvents, TreeModelInt {
    
    private TreeDataModel data;
    public ArrayList<TreeDataItem> rows = new ArrayList<TreeDataItem>();
    private TreeModelListenerCollection treeModelListeners;
    public int shownRows; 
    public TreeWidget controller;
    public boolean multiSelect;
    public ArrayList<Integer> selectedRows = new ArrayList<Integer>();
    public TreeServiceCallInt treeService;

    
    public TreeManager manager;
    
    public TreeModel(TreeWidget controller) {
        this.controller = controller;
        addTreeModelListener(controller);
        addTreeModelListener((TreeModelListener)controller.renderer);
    }

    public void addRow(TreeDataItem row) {
        data.add((TreeDataItem)row);
    }

    public void addRow(int index, TreeDataItem row) {
        data.add(index,(TreeDataItem)row);
    }

    public boolean canDelete(int row) {
        TreeDataItem item = rows.get(row);
        if(manager != null)
            return manager.canDelete(controller,item, row);
        if(controller.enabled)
            return true;
        return false;
    }

    public boolean canEdit(int row, int col) {
        TreeDataItem item = rows.get(row);
        if(!item.enabled)
            return false;
        if(manager != null)
            return manager.canEdit(controller, item, row, col);
        if(controller.enabled)
            return true;
        return false;
    }

    public boolean canAdd(int row) {
        if(!rows.get(row).enabled)
            return false;
        if(manager != null)
            return manager.canAdd(controller,rows.get(row), row);
        if(controller.enabled)
            return true;
        return false;
    }

    public boolean canSelect(int row) {
        TreeDataItem item = rows.get(row);
        if(!item.enabled)
            return false;
        if(manager != null)
            return manager.canSelect(controller,item,row);
        if(controller.enabled)
            return true;
        return false;
    }
    
    public boolean canToggle(int row) {
        if(rows.get(row).open)
            return canClose(row);
        return canOpen(row);
    }
    
    public boolean canOpen(int row) {
        if(manager != null)
            return manager.canOpen(controller,rows.get(row),row);
        if(controller.enabled)
            return true;
        return false;
    }
    
    public boolean canClose(int row) {
        if(manager != null)
            return manager.canClose(controller,rows.get(row),row);
        if(controller.enabled)
            return true;
        return false;
    }
    
    public boolean canDrag(int row) {
        if(manager != null)
            return manager.canDrag(controller,rows.get(row),row);
        if(controller.enabled)
            return true;
        return false;
    }

    public boolean canDrop(Widget dragWidget, int targetRow) {
        if(manager != null)
            return manager.canDrop(controller,dragWidget,rows.get(targetRow),targetRow);
        if(controller.enabled)
            return true;
        return false;
    }
    
    public void drop(Widget dragWidget, int targetRow) {
        if(manager != null)
            manager.drop(controller,dragWidget,rows.get(targetRow),targetRow);
        TreeDataItem dropItem = getRow(targetRow);
        TreeDataItem dragItem = (TreeDataItem)((TreeRow)dragWidget).item.clone();
        if(dropItem.depth == dragItem.depth && dropItem.parent == dragItem.parent){
            deleteRow(((TreeRow)dragWidget).modelIndex);
            addRow(targetRow, dragItem);
        }
    }

    public void clear() {
        data.clear();
        rows.clear();
        refresh();
    }

    public void clearSelections() {
        data.clearSelections();
    }


    public void deleteRow(int row) {
        if(selectedRows.contains(row)){
            unselectRow(row);
        }
        if(rows.get(row).shown)
            shownRows--;
        data.delete(rows.get(row).hashCode());
        rows.remove(row);
        treeModelListeners.fireRowDeleted(this, row);
    }

    public void enableMultiSelect(boolean multi) {
        this.multiSelect = multi;
        data.multiSelect = multi;
    }

    public TreeDataModel getData() {
        // TODO Auto-generated method stub
        return data;
    }

    public Data getObject(int row, int col) {
        return rows.get(row).get(col);
    }


    public TreeDataItem getRow(int row) {
        return rows.get(row);
    }

    public TreeDataItem getSelection() {
        return data.get(data.selected);
    }

    public ArrayList<TreeDataItem> getSelections() {
        return data.getSelections();
    }

    public void hideRow(int row) {
        rows.get(row).shown = false;
        shownRows--;
        treeModelListeners.fireDataChanged(this);
    }

    public boolean isEnabled(int index) {
        if(index < numRows())
            return rows.get(index).enabled;
        return false;
    }

    public boolean isSelected(int index) {
        return selectedRows.contains(index);
    }

    public void load(TreeDataModel data) {
        this.data = data;
        data.multiSelect = multiSelect;
        refresh();
    }

    public int numRows() {
        return rows.size();
    }

    public void refresh() {
        shownRows = 0;
        rows = data.getVisibleRows();
        selectedRows.clear();
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).shown)
                shownRows++;
            if(data.selections.contains(rows.get(i).hash))
                selectedRows.add(i);
        }
        treeModelListeners.fireDataChanged(this);
    }

    public void selectRow(int index){
        if(index < numRows()){
            if(!multiSelect || selectedRows.size() == 0)
                selectedRows.add(index);
            else
                selectedRows.set(0,index);
            data.select(rows.get(index).hashCode());
        }    
        treeModelListeners.fireRowSelected(this, index);
    }


    public void setManager(TreeManager manager){
        this.manager = manager;
    }

    public void setModel(TreeDataModel data) {
        this.data = data;
        
    }

    public void showRow(int row) {
        rows.get(row).shown = true;
        shownRows++;
        treeModelListeners.fireDataChanged(this);
    }

    public int shownRows() {
        return shownRows;
    }

    public TreeDataModel unload() {
        treeModelListeners.fireUnload(this);
        return data;
    }

    public void unselectRow(int index){
        if(index < 0) {
            data.clearSelections();
            data.selected = -1;
            selectedRows.clear();
        }else if(index < numRows()){
            data.unselect(rows.get(index).hashCode());
            selectedRows.remove(new Integer(index));
        }
        treeModelListeners.fireRowUnselected(this, -1);        
    }

    public void setCell(int row, int col, Object value) {
        ((DataObject)rows.get(row).get(col)).setValue(value);
        treeModelListeners.fireCellUpdated(this, row, col);
    }

    public void addTreeModelListener(TreeModelListener listener) {
        if(treeModelListeners == null)
            treeModelListeners = new TreeModelListenerCollection();
        treeModelListeners.add(listener);
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        if(treeModelListeners != null)
            treeModelListeners.remove(listener);
    }

    public Object getCell(int row, int col) {
        return rows.get(row).get(col);
    }

    public TreeDataItem setRow(int index, TreeDataItem row) {
        return null;
    }
    
    public void toggle(int row) {
        if(rows.get(row).lazy && !rows.get(row).loaded){
            treeService.getChildNodes(this,row);
            return;
        }
        rows.get(row).toggle();
        refresh();
        if(rows.get(row).open)
            treeModelListeners.fireRowOpened(this,row,rows.get(row));
        else
            treeModelListeners.fireRowClosed(this,row,rows.get(row));
    }
    
    public int getSelectedRowIndex() {
        if(selectedRows.size() == 0)
            return -1;
        return selectedRows.get(0);
    }
    
    public int[] getSelectedRowIndexes() {
        int[] ret = new int[selectedRows.size()];
        for(int i = 0;  i < selectedRows.size(); i++)
            ret[i] = selectedRows.get(i);
        return ret;
    }

    public void clearCellError(int row, int col) {
        ((AbstractField)rows.get(row).get(col)).clearErrors();
        treeModelListeners.fireCellUpdated(this, row, col);
    }

    public void setCellError(int row, int col, String Error) {
        ((AbstractField)rows.get(row).get(col)).addError(Error);
        treeModelListeners.fireCellUpdated(this, row, col);
        
    }
    
    public TreeDataItem createTreeItem(String leafType, DataObject key) {
        return data.createTreeItem(leafType, key);
    }
    
    public void setLeaves(HashMap<String,TreeDataItem> leaves){
        data.leaves = leaves;
    }

    public void drop(Widget dragWidget) {
        if(manager != null)
            manager.drop(controller, dragWidget);
    }



}
