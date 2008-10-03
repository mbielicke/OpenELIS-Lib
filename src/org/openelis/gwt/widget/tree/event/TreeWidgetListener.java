package org.openelis.gwt.widget.tree.event;

public interface TreeWidgetListener {
    
    public void startedEditing(SourcesTreeWidgetEvents sender, int row, int col);
    
    public void finishedEditing(SourcesTreeWidgetEvents sender, int row, int col);
    
}
