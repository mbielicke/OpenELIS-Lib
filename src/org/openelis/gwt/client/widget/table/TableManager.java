package org.openelis.gwt.client.widget.table;

import org.openelis.gwt.common.data.DataModel;

public interface TableManager {
    public boolean canSelect(int row, TableController controller);

    public boolean canEdit(int row, int col, TableController controller);

    public boolean canDelete(int row, TableController controller);

    public boolean action(int row, int col, TableController controller);

    public boolean canInsert(int row, TableController controller);

    public void finishedEditing(int row, int col, TableController controller);
    
    public boolean doAutoAdd(int row, int col, TableController controller);
    
    public void rowAdded(int row, TableController controller);
    
    public void getPage(int page);
    
    public void getNextPage();
    
    public void getPreviousPage();
    
    public void setModel(TableController controller, DataModel model);
}
