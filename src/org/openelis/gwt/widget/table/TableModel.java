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
package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.DataSorter;
import org.openelis.gwt.common.DataSorterInt;
import org.openelis.gwt.common.DataSorterInt.SortDirection;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.widget.table.event.TableModelListener;
import org.openelis.gwt.widget.table.event.TableModelListenerCollection;

import java.util.ArrayList;

public class TableModel implements TableModelInt {

    private static final long serialVersionUID = 1L;
    private DataModel data;
    public DataSorterInt sorter = new DataSorter();
    private TableModelListenerCollection tableModelListeners;
    public int shownRows; 
    
    public boolean autoAdd;
    
    public DataSet autoAddRow;
    
    public boolean multiSelect;
    
    public TableManager manager;
    
    public TableModel(TableWidget controller) {
        addTableModelListener(controller);
        addTableModelListener((TableModelListener)controller.renderer);
    }

    public void addRow() {
        addRow(createRow());
    }
    
    public void addRow(int index) {
        addRow(index,createRow());
    }
    
    public void addRow(DataSet row) {
        data.add(row);
        if(row.shown)
            shownRows++;
        tableModelListeners.fireRowAdded(this, numRows() - 1);
    }
    
    public void addRow(int index, DataSet row) {
        data.add(index, row);
        if(row.shown)
            shownRows++;
        tableModelListeners.fireRowAdded(this, index);
    }

    public void deleteRow(int row) {
        if(data.get(row).shown)
            shownRows--;
        data.delete(row);
        tableModelListeners.fireRowDeleted(this, row);
    }
        
    public DataSet getRow(int row) {
        if(row < numRows())
            return data.get(row);
        if(autoAdd)
            return autoAddRow;
        return null;
    }

    public int numRows() {
        return data.size();
    }

    public DataObject getObject(int row, int col) {
        return data.get(row).get(col);
    }


    
    public void clear() {
        data.clear();
        shownRows = 0;
        tableModelListeners.fireDataChanged(this);
    }
    
    public DataSet setRow(int index, DataSet row){
        DataSet set =  data.set(index, row);
        tableModelListeners.fireRowUpdated(this, index);
        return set;
    }

    public boolean canDelete(int row) {
        DataSet rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.get(row);
        if(manager != null)
            return manager.canDelete(rowSet, row);
        return true;
    }

    public boolean canEdit(int row, int col) {
        DataSet rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.get(row);
        if(!rowSet.enabled)
            return false;
        if(manager != null)
            return manager.canEdit(rowSet, row, col);
        if(row == numRows())
            return true;
        return true;
    }

    public boolean canAdd(int row) {
        if(!data.get(row).enabled)
            return false;
        if(manager != null)
            return manager.canAdd(data.get(row), row);
        return true;
    }

    public boolean canSelect(int row) {
        DataSet rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.get(row);
        if(!rowSet.enabled)
            return false;
        if(manager != null)
            return manager.canSelect(rowSet,row);
        if(row == numRows())
            return true;
        return true;
    }

    public boolean canAutoAdd(DataSet addRow) {
        if(manager != null)
            return manager.canAutoAdd(addRow);
        return true;
    }

    public void addTableModelListener(TableModelListener listener) {
        if(tableModelListeners == null)
            tableModelListeners = new TableModelListenerCollection();
        tableModelListeners.add(listener);
    }

    public void removeTableModelListener(TableModelListener listener) {
        if(tableModelListeners != null)
            tableModelListeners.remove(listener);
    }
    
    public int shownRows() {
        return shownRows;
    }
    
    public DataSet createRow() {
        return data.createNewSet();
    }
    
    public void load(DataModel data) {
        this.data = data;
        data.multiSelect = multiSelect;
        shownRows = 0;
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).shown)
                shownRows++;
        }
        tableModelListeners.fireDataChanged(this);
    }
    
    public void selectRow(int index){
        if(index < numRows())
            data.select(index);
        tableModelListeners.fireRowSelected(this, index);
    }
    
    public void unselectRow(int index){
        if(index < numRows())
            data.unselect(index);
        tableModelListeners.fireRowUnselected(this, -1);        
    }
    
    public void clearSelections() {
        data.clearSelections();
    }
    
    public ArrayList<DataSet> getSelections() {
        return data.getSelections();
    }


    public boolean getAutoAdd() {
        return autoAdd;
    }


    public DataSet getAutoAddRow() {
        return autoAddRow;
    }


    public void setAutoAddRow(DataSet row) {
        autoAddRow = row;
    }

    public DataModel getData() {
        // TODO Auto-generated method stub
        return data;
    }
    
    public void setCell(int row, int col, Object value) {
        data.get(row).get(col).setValue(value);
        tableModelListeners.fireCellUpdated(this, row, col);
    }
    
    public Object getCell(int row, int col) {
        return data.get(row).get(col).getValue();
    }
    
    public void hideRow(int row) {
        data.get(row).shown = false;
        shownRows--;
        tableModelListeners.fireDataChanged(this);
    }
    
    public void showRow(int row) {
        data.get(row).shown = true;
        shownRows++;
        tableModelListeners.fireDataChanged(this);
    }

    public void sort(int col, SortDirection direction) {
       sorter.sort(data, col, direction);
       tableModelListeners.fireDataChanged(this);
    }
    
    public void refresh() {
        shownRows = 0;
        for(int i = 0; i < data.size(); i++){
            if(data.get(i).shown)
                shownRows++;
        }
        tableModelListeners.fireDataChanged(this);
    }
    
    public boolean isSelected(int index) {
        return data.selections.contains(index);
    }

    public void enableMultiSelect(boolean multi) {
        this.multiSelect = multi;
        data.multiSelect = multi;
    }

    public void setModel(DataModel data) {
        this.data = data;
        
    }

    public void selectRow(DataObject key) {
        selectRow(data.indexOf(data.getByKey(key)));
    }
    
    public boolean isEnabled(int index) {
        if(index < numRows())
            return data.get(index).enabled;
        return autoAddRow.enabled;
    }
    
    public void setManager(TableManager manager){
        this.manager = manager;
    }

    public DataSet getSelection() {
        return data.get(data.selected);
    }

    public DataModel unload() {
        tableModelListeners.fireUnload(this);
        return data;
    }
    
    public boolean isAutoAdd() {
        return autoAdd;
    }

    public void enableAutoAdd(boolean autoAdd) {
        this.autoAdd = autoAdd;
        tableModelListeners.fireDataChanged(this);
        
    }

    public int getSelectedIndex() {
        return data.getSelectedIndex();
    }

    public int[] getSelectedIndexes() {
        int[] ret = new int[data.selections.size()];
        for(int i = 0; i < data.selections.size(); i++)
            ret[i] = data.selections.get(i);
        return ret;
    }
 
    
}
