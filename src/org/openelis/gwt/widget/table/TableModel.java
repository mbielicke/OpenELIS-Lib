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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.DataSorter;
import org.openelis.gwt.common.DataSorterInt;
import org.openelis.gwt.common.DataSorterInt.SortDirection;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.Field;
import org.openelis.gwt.common.data.FieldType;
import org.openelis.gwt.widget.table.event.TableModelListener;
import org.openelis.gwt.widget.table.event.TableModelListenerCollection;

import java.util.ArrayList;

public class TableModel implements TableModelInt {

    private static final long serialVersionUID = 1L;
    private DataModel<Object> data;
    public DataSorterInt sorter = new DataSorter();
    private TableModelListenerCollection tableModelListeners;
    public int shownRows; 
    private TableWidget controller;
    
    public boolean autoAdd;
    
    public DataSet<Object> autoAddRow;
    
    public boolean multiSelect;
    
    public TableManager manager;
    
    public TableModel(TableWidget controller) {
        this.controller = controller;
        addTableModelListener(controller);
        addTableModelListener((TableModelListener)controller.renderer);
    }

    public void addRow() {
        addRow(createRow());
    }
    
    public void addRow(int index) {
        addRow(index,createRow());
    }
    
    public void addRow(DataSet<? extends Object> row) {
        data.add((DataSet<Object>)row);
        if(row.shown)
            shownRows++;
        tableModelListeners.fireRowAdded(this, numRows() - 1);
    }
    
    public void addRow(int index, DataSet<Object> row) {
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
        
    public DataSet<Object> getRow(int row) {
        if(row < numRows())
            return data.get(row);
        if(autoAdd)
            return autoAddRow;
        return null;
    }

    public int numRows() {
        return data.size();
    }

    public FieldType getObject(int row, int col) {
        return (FieldType)data.get(row).get(col);
    }

    public void clear() {
        data.clear();
        shownRows = 0;
        tableModelListeners.fireDataChanged(this);
    }
    
    public DataSet<?> setRow(int index, DataSet<?> row){
        DataSet<?> set =  data.set(index, row);
        tableModelListeners.fireRowUpdated(this, index);
        return set;
    }

    public boolean canDelete(int row) {
        DataSet<Object> rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.get(row);
        if(manager != null)
            return manager.canDelete(controller,rowSet, row);
        else if(controller.enabled)
            return true;
        return false;
    }

    public boolean canEdit(int row, int col) {
        DataSet<Object> rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.get(row);
        if(!rowSet.enabled)
            return false;
        if(manager != null)
            return manager.canEdit(controller,rowSet, row, col);
        if(!controller.enabled)
            return false;
        else if(row == numRows())
            return true;
        return true;
    }

    public boolean canAdd(int row) {
        if(!data.get(row).enabled)
            return false;
        if(manager != null)
            return manager.canAdd(controller,data.get(row), row);
        if(controller.enabled)
            return true;
        return false;
    }

    public boolean canSelect(int row) {
        DataSet<Object> rowSet;
        if(row == numRows())
            rowSet = autoAddRow;
        else
            rowSet = data.get(row);
        if(!rowSet.enabled)
            return false;
        if(manager != null)
            return manager.canSelect(controller,rowSet,row);
        if(!controller.enabled)
            return false;
        else if(row == numRows())
            return true;
        return true;
    }
    
    public boolean tableRowEmpty(int index){
        return tableRowEmpty(getRow(index));
    }
        
    private boolean tableRowEmpty(DataSet<Object> row){ 
        boolean empty = true;
        for(int i=0; i< row.size(); i++){
            if(((DataObject)row.get(i)).getValue() != null && !"".equals(((DataObject)row.get(i)).getValue())){
                empty = false;
                break;
            }
        }
        return empty;
    }
    
    public boolean canAutoAdd(DataSet<Object> addRow) {
        if(manager != null)
            return manager.canAutoAdd(controller,addRow);
        return !tableRowEmpty(addRow);
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
    
    public DataSet<Object> createRow() {
        return data.createNewSet();
    }
    
    public void load(DataModel<? extends Object> data) {
        this.data = (DataModel<Object>)data;
        data.multiSelect = multiSelect;
        shownRows = 0;
        for(int i = 0; i < data.size(); i++){
            if(((DataSet)data.get(i)).shown)
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
        
        controller.activeRow = -1;
        
        tableModelListeners.fireRowUnselected(this, index);        
    }
    
    public void clearSelections() {
        data.clearSelections();
    }
    
    public ArrayList<DataSet<Object>> getSelections() {
        return data.getSelections();
    }


    public boolean getAutoAdd() {
        return autoAdd;
    }


    public DataSet<Object> getAutoAddRow() {
        return autoAddRow;
    }


    public void setAutoAddRow(DataSet<Object> row) {
        autoAddRow = row;
    }

    public DataModel<? extends Object> getData() {
        // TODO Auto-generated method stub
        return data;
    }
    
    public void setCell(int row, int col, Object value) {
        ((AbstractField)data.get(row).get(col)).setValue(value);
        tableModelListeners.fireCellUpdated(this, row, col);
    }
    
    public Object getCell(int row, int col) {
        return ((AbstractField)data.get(row).get(col)).getValue();
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

    public void setModel(DataModel<Object> data) {
        this.data = data;
        
    }

    public void selectRow(Object key) {
        selectRow(data.list.indexOf(data.getByKey(key)));
    }
    
    public boolean isEnabled(int index) {
        if(index < numRows())
            return data.get(index).enabled;
        return autoAddRow.enabled;
    }
    
    public void setManager(TableManager manager){
        this.manager = manager;
    }
    
    public TableManager getManager() {
        return manager;
    }

    public DataSet<Object> getSelection() {
        return (DataSet<Object>)data.get(data.selected);
    }

    public DataModel<Object> unload() {
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
    
    public void setCellError(int row, int col, String error) {
        ((AbstractField)data.get(row).get(col)).addError(error);
        tableModelListeners.fireCellUpdated(this, row, col);
    }
    
    public void clearCellError(int row, int col) {
        ((AbstractField)data.get(row).get(col)).clearErrors();
        tableModelListeners.fireCellUpdated(this, row, col);
    }
 
    
}
