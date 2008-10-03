package org.openelis.gwt.widget.table.event;

public interface TableWidgetListener {
    
    public void startedEditing(SourcesTableWidgetEvents sender, int row, int col);
    
    public void finishedEditing(SourcesTableWidgetEvents sender, int row, int col);
    
}
