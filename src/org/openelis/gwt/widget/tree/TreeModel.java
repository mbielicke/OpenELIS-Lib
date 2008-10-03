package org.openelis.gwt.widget.tree;

import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.common.data.TreeDataModel;
import org.openelis.gwt.widget.tree.event.SourcesTreeModelEvents;
import org.openelis.gwt.widget.tree.event.TreeModelListener;
import org.openelis.gwt.widget.tree.event.TreeModelListenerCollection;

import java.util.ArrayList;

public class TreeModel implements SourcesTreeModelEvents, TreeModelInt {
    
    private TreeDataModel data;
    public ArrayList<TreeDataItem> rows = new ArrayList<TreeDataItem>();
    private TreeModelListenerCollection treeModelListeners;
    public int shownRows; 
    
    public boolean multiSelect;
    
    public TreeManager manager;
    
    public TreeModel(TreeWidget controller) {
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
            return manager.canDelete(item, row);
        return true;
    }

    public boolean canEdit(int row, int col) {
        TreeDataItem item = rows.get(row);
        if(!item.enabled)
            return false;
        if(manager != null)
            return manager.canEdit(item, row, col);
        if(row == numRows())
            return true;
        return true;
    }

    public boolean canAdd(int row) {
        if(!rows.get(row).enabled)
            return false;
        if(manager != null)
            return manager.canAdd(rows.get(row), row);
        return true;
    }

    public boolean canSelect(int row) {
        TreeDataItem item = rows.get(row);
        if(!item.enabled)
            return false;
        if(manager != null)
            return manager.canSelect(item,row);
        if(row == numRows())
            return true;
        return true;
    }
    
    public boolean canToggle(int row) {
        if(rows.get(row).open)
            return canClose(row);
        return canOpen(row);
    }
    
    public boolean canOpen(int row) {
        if(manager != null)
            return manager.canOpen(rows.get(row),row);
        return true;
    }
    
    public boolean canClose(int row) {
        if(manager != null)
            return manager.canClose(rows.get(row),row);
        return true;
    }

    public void clear() {
        data.clear();
        rows.clear();
        treeModelListeners.fireDataChanged(this);
    }

    public void clearSelections() {
        data.clearSelections();
    }


    public void deleteRow(int row) {
        if(rows.get(row).shown)
            shownRows--;
        data.remove(rows.get(row).hashCode());
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

    public DataObject getObject(int row, int col) {
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
        return data.selections.contains(rows.get(index).hashCode());
    }

    public void load(TreeDataModel data) {
        this.data = data;
        rows = data.getVisibleRows();
        data.multiSelect = multiSelect;
        shownRows = 0;
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).shown)
                shownRows++;
        }
        treeModelListeners.fireDataChanged(this);
    }

    public int numRows() {
        return rows.size();
    }

    public void refresh() {
        shownRows = 0;
        rows = data.getVisibleRows();
        for(int i = 0; i < rows.size(); i++){
            if(rows.get(i).shown)
                shownRows++;
        }
        treeModelListeners.fireDataChanged(this);
    }

    public void selectRow(int index){
        if(index < numRows())
            data.select(rows.get(index).hashCode());
        treeModelListeners.fireRowSelected(this, index);
    }

    public void selectRow(DataObject key) {
       //selectRow(data.keyMap.get());
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
        }else if(index < numRows())
            data.unselect(rows.get(index).hashCode());
        treeModelListeners.fireRowUnselected(this, -1);        
    }

    public void setCell(int row, int col, Object value) {
        rows.get(row).get(col).setValue(value);
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
        rows.get(row).toggle();
        refresh();
        if(rows.get(row).open)
            treeModelListeners.fireRowOpened(this,row,rows.get(row));
        else
            treeModelListeners.fireRowClosed(this,row,rows.get(row));
    }



}
