package org.openelis.gwt.widget.tree.event;

import java.util.ArrayList;

public class TreeWidgetListenerCollection extends ArrayList<TreeWidgetListener>{
    
   public void fireStartedEditing(SourcesTreeWidgetEvents sender, int row, int col){
       for(TreeWidgetListener listener : this)
           listener.startedEditing(sender, row, col);
   }
    
   public void fireFinishedEditing(SourcesTreeWidgetEvents sender, int row, int col){
       for(TreeWidgetListener listener : this)
           listener.finishedEditing(sender, row, col);
   }
    

}
