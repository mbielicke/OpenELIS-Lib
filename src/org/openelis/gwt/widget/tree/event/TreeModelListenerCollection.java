package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.common.data.TreeDataItem;

import java.util.ArrayList;

public class TreeModelListenerCollection extends ArrayList<TreeModelListener> {
    
    public void fireCellUpdated(SourcesTreeModelEvents sender, int row, int col){
        for(TreeModelListener listener : this)
            listener.cellUpdated(sender,row,col);
    }
    
    public void fireRowUpdated(SourcesTreeModelEvents sender, int row){
        for(TreeModelListener listener : this) 
            listener.rowUpdated(sender, row);
    }

    public void fireRowDeleted(SourcesTreeModelEvents sender, int row) {
        for(TreeModelListener listener : this)
            listener.rowDeleted(sender, row);
    }
    
    public void fireRowAdded(SourcesTreeModelEvents sender, int row) {
        for(TreeModelListener listener : this)
            listener.rowAdded(sender, row);
    }
    
    public void fireDataChanged(SourcesTreeModelEvents sender) {
        for(TreeModelListener listener : this)
            listener.dataChanged(sender);
    }
    
    public void fireRowSelected(SourcesTreeModelEvents sender, int row){
        for(TreeModelListener listener : this)
            listener.rowSelectd(sender, row);
    }
    
    public void fireRowUnselected(SourcesTreeModelEvents sender, int row) {
        for(TreeModelListener listener : this)
            listener.rowUnselected(sender, row);
    }
    
    public void fireUnload(SourcesTreeModelEvents sender) {
        for(TreeModelListener listener : this)
            listener.unload(sender);
    }
    
    public void fireRowOpened(SourcesTreeModelEvents sender, int row, TreeDataItem item) {
        for(TreeModelListener listener : this)
            listener.rowOpened(sender,row,item);
    }
    
    public void fireRowClosed(SourcesTreeModelEvents sender, int row, TreeDataItem item) {
        for(TreeModelListener listener : this)
            listener.rowClosed(sender,row,item);
    }
}
