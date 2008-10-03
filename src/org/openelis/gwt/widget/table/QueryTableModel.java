package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.DataSorterInt.SortDirection;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.widget.table.event.TableModelListener;

import java.util.ArrayList;

public class QueryTableModel implements TableModelInt{
    
    private FormRPC data;

    public void addRow() {
        // STUB
    }

    public void addRow(int index) {
        // STUB
    }

    public void addRow(DataSet row) {
        // STUB
    }

    public void addRow(int index, DataSet row) {
        // STUB
    }

    public boolean canAdd(int row) {
        // STUB
        return false;
    }

    public boolean canAutoAdd(DataSet addRow) {
        // STUB
        return false;
    }

    public boolean canDelete(int row) {
        // STUB
        return false;
    }

    public boolean canEdit(int row, int col) {
        return true;
    }

    public boolean canSelect(int row) {
        return true;
    }

    public void clear() {
        data.reset();
    }

    public void clearSelections() {
        
    }

    public DataSet createRow() {
        // STUB
        return null;
    }

    public void deleteRow(int row) {
        // STUB
    }

    public void enableMultiSelect(boolean multi) {
        // STUB
        
    }

    public boolean getAutoAdd() {
        // STUB
        return false;
    }

    public DataSet getAutoAddRow() {
        // STUB
        return null;
    }

    public DataModel getData() {
        // STUB
        return null;
    }

    public DataObject getObject(int row, int col) {
        // STUB
        return null;
    }

    public DataSet getRow(int row) {
        // STUB
        return null;
    }

    public DataSet getSelection() {
        // TODO Auto-generated method stub
        return null;
    }

    public ArrayList<DataSet> getSelections() {
        // TODO Auto-generated method stub
        return null;
    }

    public void hideRow(int row) {
        // TODO Auto-generated method stub
        
    }

    public boolean isAutoAdd() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isEnabled(int index) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSelected(int index) {
        // TODO Auto-generated method stub
        return false;
    }

    public void load(DataModel data) {
        // TODO Auto-generated method stub
        
    }

    public int numRows() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void refresh() {
        // TODO Auto-generated method stub
        
    }

    public void selectRow(int index) {
        // TODO Auto-generated method stub
        
    }

    public void selectRow(DataObject key) {
        // TODO Auto-generated method stub
        
    }

    public void setAutoAddRow(DataSet row) {
        // TODO Auto-generated method stub
        
    }

    public void setManager(TableManager manager) {
        // TODO Auto-generated method stub
        
    }

    public void setModel(DataModel data) {
        // TODO Auto-generated method stub
        
    }

    public DataSet setRow(int index, DataSet row) {
        // TODO Auto-generated method stub
        return null;
    }

    public void showRow(int row) {
        // TODO Auto-generated method stub
        
    }

    public int shownRows() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void sort(int col, SortDirection direction) {
        // TODO Auto-generated method stub
        
    }

    public DataModel unload() {
        // TODO Auto-generated method stub
        return null;
    }

    public void unselectRow(int index) {
        // TODO Auto-generated method stub
        
    }

    public void updateCell(int row, int col, Object obj) {
        // TODO Auto-generated method stub
        
    }

    public void addTableModelListener(TableModelListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void removeTableModelListener(TableModelListener listener) {
        // TODO Auto-generated method stub
        
    }

    public void enableAutoAdd(boolean autoAdd) {
        // TODO Auto-generated method stub
        
    }
    

}
