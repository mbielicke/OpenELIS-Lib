package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.AppScreen;

public class TableMouseHandler implements TableMouseHandlerInt {
    
    private TableWidget controller;
    
    public TableMouseHandler(TableWidget controller) {
        this.controller = controller;
    }
    
    /**
     * This method is catches click events on page index for paged tables.
     */
    public void onClick(Widget sender) {
        if(controller.view.table.isAttached()){
            if(sender instanceof AppScreen){
                if(controller.focused && !DOM.isOrHasChild(controller.view.getElement(), ((AppScreen)sender).clickTarget)){
                    controller.focused = false;
                    if(controller.editingCell != null){
                        controller.tableWidgetListeners.fireFinishedEditing(controller, controller.activeRow, controller.activeCell);
                    }
                    controller.activeCell = -1;
                    //controller.setFocus(true);
                }
                return;
            }
        }
 
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }



}
