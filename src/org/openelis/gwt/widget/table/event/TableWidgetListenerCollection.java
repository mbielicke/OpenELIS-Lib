package org.openelis.gwt.widget.table.event;

import java.util.ArrayList;

public class TableWidgetListenerCollection extends ArrayList<TableWidgetListener>{
    
   public void fireStartedEditing(SourcesTableWidgetEvents sender, int row, int col){
       for(TableWidgetListener listener : this)
           listener.startedEditing(sender, row, col);
   }
    
   public void fireFinishedEditing(SourcesTableWidgetEvents sender, int row, int col){
       for(TableWidgetListener listener : this)
           listener.finishedEditing(sender, row, col);
   }
    

}
