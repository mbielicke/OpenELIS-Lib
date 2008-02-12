package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.widget.table.TableController;

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
    
    public void getNextPage(TableController controller);
    
    public void getPreviousPage(TableController controller);
    
    public void setModel(TableController controller, DataModel model);
    
    public void validateRow(int row, TableController controller);
}
