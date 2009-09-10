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
package org.openelis.gwt.widget.table.deprecated;

import org.openelis.gwt.common.data.deprecated.AbstractField;
import org.openelis.gwt.common.data.deprecated.FieldType;
import org.openelis.gwt.common.data.deprecated.TableDataModel;
import org.openelis.gwt.common.data.deprecated.TableDataRow;
import org.openelis.gwt.common.deprecated.DataSorter;
import org.openelis.gwt.common.deprecated.DataSorterInt;
import org.openelis.gwt.common.deprecated.DataSorterInt.SortDirection;
import org.openelis.gwt.widget.deprecated.ModelUtil;
import org.openelis.gwt.widget.table.deprecated.event.TableModelListener;
import org.openelis.gwt.widget.table.deprecated.event.TableModelListenerCollection;

import java.util.ArrayList;
import java.util.List;
@Deprecated
public class TableModel implements TableModelInt {

    private static final long serialVersionUID = 1L;
    private TableDataModel<TableDataRow<?>> data;
    public DataSorterInt sorter = new DataSorter();
    private TableModelListenerCollection tableModelListeners;
    public int shownRows; 
    private TableWidget controller;
    
    public boolean autoAdd;
    
    public TableDataRow autoAddRow;
    
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
    
    public <T> void addRow(TableDataRow<T> row) {
        data.add(row);
        if(row.shown)
            shownRows++;
        tableModelListeners.fireRowAdded(this, numRows() - 1);
    }
    
    public <T> void addRow(int index, TableDataRow<T> row) {
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
        
    public <T extends TableDataRow> T getRow(int row) {
        if(data == null)
            return null;
        if(row < numRows())
            return (T)data.get(row);
        if(autoAdd)
            return (T)autoAddRow;
        return null;
    }

    public int numRows() {
        if(data == null)
            return 0;
        return data.size();
    }

    public FieldType getObject(int row, int col) {
        return (FieldType)data.get(row).getCells().get(col);
    }

    public void clear() {
        data.clear();
        shownRows = 0;
        tableModelListeners.fireDataChanged(this);
    }
    
    public <T> TableDataRow<T> setRow(int index, TableDataRow<T> row){
        TableDataRow set =  data.set(index, row);
        tableModelListeners.fireRowUpdated(this, index);
        return set;
    }

    public boolean canDelete(int row) {
        TableDataRow rowSet;
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
        TableDataRow rowSet;
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
        TableDataRow rowSet;
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
        return tableRowEmpty((TableDataRow)getRow(index));
    }
        
    private <T extends TableDataRow> boolean tableRowEmpty(T row){ 
        boolean empty = true;
        List<FieldType> cells = row.getCells();
        for(FieldType field : cells) {//int i=0; i< row.size(); i++){
            if(field.getValue() != null && !"".equals(field.getValue())){
                empty = false;
                break;
            }
        }
        return empty;
    }
    
    public <T> boolean canAutoAdd(TableDataRow<T> addRow) {
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
    
    public <T extends TableDataRow> T createRow() {
        return (T)data.createNewSet();
    }
    
    public <T extends TableDataRow<?>> void load(TableDataModel<T> data) {
        this.data = (TableDataModel<TableDataRow<?>>)data;
        data.multiSelect = multiSelect;
        shownRows = 0;
        for(int i = 0; i < data.size(); i++){
            if(((TableDataRow)data.get(i)).shown)
                shownRows++;
        }        
        tableModelListeners.fireDataChanged(this);
    }
    
    public void selectRow(final int index){
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
    
    public ArrayList<TableDataRow<?>> getSelections() {
        return data.getSelections();
    }


    public boolean getAutoAdd() {
        return autoAdd;
    }


    public TableDataRow<?> getAutoAddRow() {
        return autoAddRow;
    }


    public void setAutoAddRow(TableDataRow<?> row) {
        autoAddRow = row;
    }

    public <T extends TableDataRow<?>> TableDataModel<T> getData() {
        // TODO Auto-generated method stub
        return (TableDataModel<T>)data;
    }
    
    public void setCell(int row, int col, Object value) {
        ((AbstractField)getData().get(row).getCells().get(col)).setValue(value);
        tableModelListeners.fireCellUpdated(this, row, col);
    }
    
    public Object getCell(int row, int col) {
        return ((AbstractField)data.get(row).getCells().get(col)).getValue();
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
      // sorter.sort(data, col, direction);
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

    public <T extends TableDataRow<?>> void setModel(TableDataModel<T> data) {
        this.data = (TableDataModel<TableDataRow<?>>)data;
        
    }

    public void selectRow(Object key) {
        selectRow(data.list.indexOf(ModelUtil.getRowByKey(data,key)));
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

    public <T> TableDataRow<T> getSelection() {
        return (TableDataRow<T>)data.get(data.selected);
    }

    public TableDataModel<TableDataRow<?>> unload() {
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
            ret[i] = (Integer)data.selections.get(i);
        return ret;
    }
    
    public void setCellError(int row, int col, String error) {
        ((AbstractField)data.get(row).getCells().get(col)).addError(error);
        tableModelListeners.fireCellUpdated(this, row, col);
    }
    
    public void clearCellError(int row, int col) {
        ((AbstractField)data.get(row).getCells().get(col)).clearErrors();
        tableModelListeners.fireCellUpdated(this, row, col);
    }
 
    
}
