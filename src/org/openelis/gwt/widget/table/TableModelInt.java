package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.DataSorterInt;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.widget.table.event.SourcesTableModelEvents;

import java.util.ArrayList;

public interface TableModelInt extends SourcesTableModelEvents {

    public void addRow();
    
    public void addRow(int index);
    
    public void addRow(DataSet row);
    
    public void addRow(int index, DataSet row);
    
    public void deleteRow(int row);
        
    public void hideRow(int row);
    
    public void showRow(int row);
    
    public DataSet getRow(int row);
    
    public int numRows();
    
    public DataObject getObject(int row, int col);
    
    public void clear();
    
    public DataSet setRow(int index, DataSet row);
    
    public int shownRows();
    
    public DataSet createRow();
    
    public void load(DataModel data);
    
    public void selectRow(int index);
    
    public void selectRow(DataObject key);
    
    public void unselectRow(int index);
    
    public void clearSelections();
    
    public ArrayList<DataSet> getSelections();
    
    public DataSet getSelection();
    
    public boolean getAutoAdd();
    
    public DataSet getAutoAddRow();
    
    public void setAutoAddRow(DataSet row);
    
    public DataModel getData();
    
    public void setCell(int row, int col,Object obj);
    
    public Object getCell(int row, int col);
    
    public void sort(int col, DataSorterInt.SortDirection direction);
    
    public void refresh();
    
    public boolean isSelected(int index);
    
    public void enableMultiSelect(boolean multi);
    
    public void setModel(DataModel data);
    
    public boolean isEnabled(int index);
    
    public void setManager(TableManager manager);
    
    public boolean canSelect( int row);

    public boolean canEdit(int row, int col);

    public boolean canDelete(int row);

    public boolean canAdd(int row);
    
    public boolean canAutoAdd(DataSet autoAddRow);
    
    public DataModel unload();
    
    public boolean isAutoAdd();
    
    public void enableAutoAdd(boolean autoAdd);
    
    
}
