package org.openelis.gwt.client.widget.table;

import org.openelis.gwt.common.data.DataModel;

public interface TableManagerSmall {
    public boolean canSelect(int row, TableControllerSmall controller);

    public boolean canEdit(int row, int col, TableControllerSmall controller);

    public boolean canDelete(int row, TableControllerSmall controller);

    public boolean action(int row, int col, TableControllerSmall controller);

    public boolean canInsert(int row, TableControllerSmall controller);

    public void finishedEditing(int row, int col, TableControllerSmall controller);
    
    public boolean doAutoAdd(int row, int col, TableControllerSmall controller);
    
    public void rowAdded(int row, TableControllerSmall controller);
    
    public void getPage(int page);
    
    public void getNextPage();
    
    public void getPreviousPage();
    
    public void setModel(TableControllerSmall controller, DataModel model);
}
