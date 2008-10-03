package org.openelis.gwt.widget.table.event;

public interface TableModelListener {
    
    public void cellUpdated(SourcesTableModelEvents sender, int row , int cell);
    
    public void rowUpdated(SourcesTableModelEvents sender,  int row);
    
    public void rowDeleted(SourcesTableModelEvents sender,  int row);
    
    public void rowAdded(SourcesTableModelEvents sender, int rows);
    
    public void dataChanged(SourcesTableModelEvents sender);
    
    public void rowSelectd(SourcesTableModelEvents sender, int row);
    
    public void rowUnselected(SourcesTableModelEvents sender, int row);
    
    public void unload(SourcesTableModelEvents sender);

}
