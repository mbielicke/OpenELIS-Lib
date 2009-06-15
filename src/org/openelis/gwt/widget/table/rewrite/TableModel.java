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
package org.openelis.gwt.widget.table.rewrite;

import org.openelis.gwt.common.DataSorter;
import org.openelis.gwt.common.DataSorterInt;
import org.openelis.gwt.common.DataSorterInt.SortDirection;
import org.openelis.gwt.common.rewrite.data.TableDataRow;
import org.openelis.gwt.widget.table.rewrite.event.TableModelListener;
import org.openelis.gwt.widget.table.rewrite.event.TableModelListenerCollection;

import java.util.ArrayList;
import java.util.List;

public class TableModel implements TableModelInt {

    private static final long serialVersionUID = 1L;
    private ArrayList<TableDataRow> data;
    public DataSorterInt sorter = new DataSorter();
    private TableModelListenerCollection tableModelListeners;
    public int shownRows; 
    private TableWidget controller;
    public ArrayList<TableDataRow> deleted;// = new ArrayList<DataSet<Key>>();
    public ArrayList<Integer> selections = new ArrayList<Integer>(1);
    private int selected;
    
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
    
    public void addRow(TableDataRow row) {
        data.add(row);
        if(row.shown)
            shownRows++;
        tableModelListeners.fireRowAdded(this, numRows() - 1);
    }
    
    public void addRow(int index, TableDataRow row) {
        data.add(index, row);
        if(row.shown)
            shownRows++;
        tableModelListeners.fireRowAdded(this, index);
    }

    public void deleteRow(int row) {
        if(data.get(row).shown)
            shownRows--;
        if(deleted == null)
            deleted = new ArrayList<TableDataRow>(1);
        if(row < data.size()){
            TableDataRow tmp = data.remove(row);
            deleted.add(tmp);
        }
        tableModelListeners.fireRowDeleted(this, row);
    }
        
    public TableDataRow getRow(int row) {
        if(data == null)
            return null;
        if(row < numRows())
            return data.get(row);
        if(autoAdd)
            return autoAddRow;
        return null;
    }

    public int numRows() {
        if(data == null)
            return 0;
        return data.size();
    }

    public Object getObject(int row, int col) {
        return data.get(row).cells[col];
    }

    public void clear() {
        data.clear();
        shownRows = 0;
        tableModelListeners.fireDataChanged(this);
    }
    
    public TableDataRow setRow(int index, TableDataRow row){
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
        
    private boolean tableRowEmpty(TableDataRow row){ 
        boolean empty = true;
        List cells = row.getCells();
        for(Object field : cells) {
            if(field != null && !"".equals(field)){
                empty = false;
                break;
            }
        }
        return empty;
    }
    
    public boolean canAutoAdd(TableDataRow addRow) {
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
    
    public TableDataRow createRow() {
        return new TableDataRow(controller.columns.size());
    }
    
    public void load(ArrayList<TableDataRow> data) {
        this.data = data;
        shownRows = 0;
        for(int i = 0; i < data.size(); i++){
            if(((TableDataRow)data.get(i)).shown)
                shownRows++;
        }        
        tableModelListeners.fireDataChanged(this);
    }

    public void unselect(int index) {
        if(index == -1){
            selections.clear();
            selected = -1;
        }else if(index < selections.size()){
            selections.remove(new Integer(index));
            if(selected == index)
                selected = -1;
        }
    }
    public void selectRow(final int index) throws IndexOutOfBoundsException{
        if(index > data.size())
            throw new IndexOutOfBoundsException();
        selected = index;
        if(!multiSelect)
            selections.clear();
        selections.add(index);
       tableModelListeners.fireRowSelected(this, index);
    
       
    }
    
    public void unselectRow(int index){
        if(index == -1){
            selections.clear();
            selected = -1;
        }else if(index < selections.size()){
            selections.remove(new Integer(index));
            if(selected == index)
                selected = -1;
        }
        controller.activeRow = -1;
        tableModelListeners.fireRowUnselected(this, index);        
    }
    
    public void clearSelections() {
        selections.clear();
    }
    
    public ArrayList<TableDataRow> getSelections() {
    	ArrayList<TableDataRow>  sels = new ArrayList<TableDataRow>();
    	for(int index : selections) {
    		sels.add(getRow(index));
    	}
        return sels;
    }


    public boolean getAutoAdd() {
        return autoAdd;
    }


    public TableDataRow getAutoAddRow() {
        return autoAddRow;
    }


    public void setAutoAddRow(TableDataRow row) {
        autoAddRow = row;
    }

    public ArrayList<TableDataRow> getData() {
        return data;
    }
    
    public void setCell(int row, int col, Object value) {
        data.get(row).cells[col] = value;
        tableModelListeners.fireCellUpdated(this, row, col);
    }
    
    public Object getCell(int row, int col) {
        return data.get(row).cells[col];
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
        return selections.contains(index);
    }

    public void enableMultiSelect(boolean multi) {
        this.multiSelect = multi;
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

    public TableDataRow getSelection() {
        return data.get(selected);
    }

    public ArrayList<TableDataRow> unload() {
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
        return selected;
    }

    public int[] getSelectedIndexes() {
        int[] ret = new int[selections.size()];
        for(int i = 0; i < selections.size(); i++)
            ret[i] = (Integer)selections.get(i);
        return ret;
    }
   
    
    public void setCellError(int row, int col, String error) {
    //    ((AbstractField)data.get(row).getCells().get(col)).addError(error);
      //  tableModelListeners.fireCellUpdated(this, row, col);
    }
    
    public void clearCellError(int row, int col) {
    //    ((AbstractField)data.get(row).getCells().get(col)).clearErrors();
     //   tableModelListeners.fireCellUpdated(this, row, col);
    }
 
    public void selectRow(Object key) {
    	for(int i = 0; i < data.size(); i++) {
    		if(data.get(i).key.equals(key)){
    			selectRow(i);
    			break;
    		}
    	}
    }
    
}
