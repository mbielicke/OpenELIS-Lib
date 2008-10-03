package org.openelis.gwt.widget.table.event;

import java.util.ArrayList;

public class TableModelListenerCollection extends ArrayList<TableModelListener> {
    
    public void fireCellUpdated(SourcesTableModelEvents sender, int row, int col){
        for(TableModelListener listener : this)
            listener.cellUpdated(sender,row,col);
    }
    
    public void fireRowUpdated(SourcesTableModelEvents sender, int row){
        for(TableModelListener listener : this) 
            listener.rowUpdated(sender, row);
    }

    public void fireRowDeleted(SourcesTableModelEvents sender, int row) {
        for(TableModelListener listener : this)
            listener.rowDeleted(sender, row);
    }
    
    public void fireRowAdded(SourcesTableModelEvents sender, int row) {
        for(TableModelListener listener : this)
            listener.rowAdded(sender, row);
    }
    
    public void fireDataChanged(SourcesTableModelEvents sender) {
        for(TableModelListener listener : this)
            listener.dataChanged(sender);
    }
    
    public void fireRowSelected(SourcesTableModelEvents sender, int row){
        for(TableModelListener listener : this)
            listener.rowSelectd(sender, row);
    }
    
    public void fireRowUnselected(SourcesTableModelEvents sender, int row) {
        for(TableModelListener listener : this)
            listener.rowUnselected(sender, row);
    }
    
    public void fireUnload(SourcesTableModelEvents sender) {
        for(TableModelListener listener : this)
            listener.unload(sender);
    }
}
