package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.common.data.TreeDataItem;

public interface TreeModelListener {
    
    public void cellUpdated(SourcesTreeModelEvents sender, int row , int cell);
    
    public void rowUpdated(SourcesTreeModelEvents sender,  int row);
    
    public void rowDeleted(SourcesTreeModelEvents sender,  int row);
    
    public void rowAdded(SourcesTreeModelEvents sender, int rows);
    
    public void dataChanged(SourcesTreeModelEvents sender);
    
    public void rowSelectd(SourcesTreeModelEvents sender, int row);
    
    public void rowUnselected(SourcesTreeModelEvents sender, int row);
    
    public void unload(SourcesTreeModelEvents sender);
    
    public void rowClosed(SourcesTreeModelEvents sender, int row, TreeDataItem item);
    
    public void rowOpened(SourcesTreeModelEvents sender, int row, TreeDataItem item);

}
