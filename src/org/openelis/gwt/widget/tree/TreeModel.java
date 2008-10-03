package org.openelis.gwt.widget.tree;

import org.openelis.gwt.common.DataSorterInt.SortDirection;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.common.data.TreeDataModel;
import org.openelis.gwt.widget.table.TableManager;
import org.openelis.gwt.widget.table.TableWidget;
import org.openelis.gwt.widget.table.event.TableModelListener;
import org.openelis.gwt.widget.tree.event.SourcesTreeModelEvents;
import org.openelis.gwt.widget.tree.event.TreeModelListener;
import org.openelis.gwt.widget.tree.event.TreeModelListenerCollection;

import java.util.ArrayList;

public class TreeModel implements SourcesTreeModelEvents {
    
    private TreeDataModel data;
    private TreeModelListenerCollection treeModelListeners;
    public int shownRows; 
    
    public DataSet autoAddRow;
    
    public boolean multiSelect;
    
    public TableManager manager;
    
    public TreeModel(TreeWidget controller) {
        addTreeModelListener(controller);
        addTreeModelListener((TreeModelListener)controller.renderer);
    }

    public void addRow(DataSet row) {
        data.add((TreeDataItem)row);
    }

    public void addRow(int index, DataSet row) {
        data.add(index,(TreeDataItem)row);
    }

    public boolean canDelete(int row) {
        DataSet rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.rows.get(row);
        if(manager != null)
            return manager.canDelete(rowSet, row);
        return true;
    }

    public boolean canEdit(int row, int col) {
        DataSet rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.rows.get(row);
        if(!rowSet.enabled)
            return false;
        if(manager != null)
            return manager.canEdit(rowSet, row, col);
        if(row == numRows())
            return true;
        return true;
    }

    public boolean canAdd(int row) {
        if(!data.rows.get(row).enabled)
            return false;
        if(manager != null)
            return manager.canAdd(data.rows.get(row), row);
        return true;
    }

    public boolean canSelect(int row) {
        DataSet rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.rows.get(row);
        if(!rowSet.enabled)
            return false;
        if(manager != null)
            return manager.canSelect(rowSet,row);
        if(row == numRows())
            return true;
        return true;
    }

    public void clear() {
        data.clear();
        treeModelListeners.fireDataChanged(this);
    }

    public void clearSelections() {
        data.clearSelections();
    }

    public DataSet createRow() {
        // TODO Auto-generated method stub
        return null;
    }

    public void deleteRow(int row) {
        if(data.rows.get(row).shown)
            shownRows--;
        data.remove(row);
        treeModelListeners.fireRowDeleted(this, row);
    }

    public void enableMultiSelect(boolean multi) {
        this.multiSelect = multi;
        data.multiSelect = multi;
    }

    public boolean getAutoAdd() {
        // TODO Auto-generated method stub
        return false;
    }

    public DataSet getAutoAddRow() {
        // TODO Auto-generated method stub
        return null;
    }

    public TreeDataModel getData() {
        // TODO Auto-generated method stub
        return data;
    }

    public DataObject getObject(int row, int col) {
        return data.rows.get(row).get(col);
    }


    public DataSet getRow(int row) {
        return data.rows.get(row);
    }

    public DataSet getSelection() {
        return data.rows.get(data.selected);
    }

    public ArrayList<TreeDataItem> getSelections() {
        return data.getSelections();
    }

    public void hideRow(int row) {
        data.rows.get(row).shown = false;
        shownRows--;
        treeModelListeners.fireDataChanged(this);
    }

    public boolean isAutoAdd() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isEnabled(int index) {
        if(index < numRows())
            return data.rows.get(index).enabled;
        return autoAddRow.enabled;
    }

    public boolean isSelected(int index) {
        // TODO Auto-generated method stub
        return false;
    }

    public void load(TreeDataModel data) {
        this.data = data;
        data.setRows();
        data.multiSelect = multiSelect;
        shownRows = 0;
        for(int i = 0; i < data.size(); i++){
            if(data.rows.get(i).shown)
                shownRows++;
        }
        treeModelListeners.fireDataChanged(this);
    }

    public int numRows() {
        // TODO Auto-generated method stub
        return data.rows.size();
    }

    public void refresh() {
        shownRows = 0;
        data.setRows();
        for(int i = 0; i < data.rows.size(); i++){
            if(data.rows.get(i).shown)
                shownRows++;
        }
        treeModelListeners.fireDataChanged(this);
    }

    public void selectRow(int index){
        if(index < numRows())
            data.select(index);
        treeModelListeners.fireRowSelected(this, index);
    }

    public void selectRow(DataObject key) {
       // selectRow(data.indexOf(data.getByKey(key)));
    }

    public void setAutoAddRow(DataSet row) {
        // TODO Auto-generated method stub

    }

    public void setManager(TableManager manager){
        this.manager = manager;
    }

    public void setModel(TreeDataModel data) {
        this.data = data;
        
    }

    public DataSet setRow(int index, DataSet row) {
        // TODO Auto-generated method stub
        return null;
    }

    public void showRow(int row) {
        data.rows.get(row).shown = true;
        shownRows++;
        treeModelListeners.fireDataChanged(this);
    }

    public int shownRows() {
        // TODO Auto-generated method stub
        return shownRows;
    }

    public void sort(int col, SortDirection direction) {
        // TODO Auto-generated method stub

    }

    public TreeDataModel unload() {
        treeModelListeners.fireUnload(this);
        return data;
    }

    public void unselectRow(int index){
        if(index < numRows())
            data.unselect(index);
        treeModelListeners.fireRowUnselected(this, -1);        
    }

    public void updateCell(int row, int col, Object value) {
        data.rows.get(row).get(col).setValue(value);
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

}
