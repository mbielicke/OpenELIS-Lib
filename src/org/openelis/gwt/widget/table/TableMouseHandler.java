/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
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
                        controller.finishEditing();
                        //controller.tableWidgetListeners.fireFinishedEditing(controller, controller.activeRow, controller.activeCell);
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
        Window.alert("mouse down");
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
