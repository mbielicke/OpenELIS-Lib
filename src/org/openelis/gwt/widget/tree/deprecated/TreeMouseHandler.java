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
package org.openelis.gwt.widget.tree.deprecated;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.deprecated.AppScreen;

import java.util.Vector;

@Deprecated
public class TreeMouseHandler implements TreeMouseHandlerInt {
    
    private TreeWidget controller;
    
    public TreeMouseHandler(TreeWidget controller) {
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
                        controller.treeWidgetListeners.fireStopEditing(controller, controller.activeRow, controller.activeCell);
                    }
                    controller.activeCell = -1;
                }
                return;
            }
        }
 
    }
    
    Timer delay;

    public void onMouseDown(final Widget sender, final int x, final int y) {
    /*    if(controller.drag == null || !controller.model.canDrag(((TreeRow)sender).modelIndex))
            return;
        if(delay != null)
            delay.cancel();
        delay = new Timer() {
            public void run() {
                final TreeRow proxy = ((TreeRow)sender).getProxy();
                proxy.removeStyleName("TreeHighlighted");
                AbsolutePanel dragIndicator = new AbsolutePanel();
                dragIndicator.setStyleName("DragStatus");
                dragIndicator.addStyleName("NoDrop");
                HorizontalPanel hp = new HorizontalPanel();
                hp.add(dragIndicator);
                hp.add(proxy);
                hp.setStyleName(sender.getStyleName());
                proxy.addDragListener(controller.drag);
                RootPanel.get().add(hp);
                MouseDragGestureRecognizer mouse = MouseDragGestureRecognizer.getGestureMouse(proxy);
                mouse.setDrag(hp);
                MouseDragGestureRecognizer.setDropMap(controller.screenWidget.getDropMap());
                MouseDragGestureRecognizer.setWidgetPosition(hp,
                                                              sender.getAbsoluteLeft(),
                                                              sender.getAbsoluteTop());
                DeferredCommand.addCommand(new Command() {
                    public void execute() {
                        MouseDragGestureRecognizer.getGestureMouse(proxy)
                                                  .onMouseDown(proxy, x, y);
                    }
                });
            }
            
        };
        delay.schedule(500);
      */  
    }
    
    public void onMouseEnter(Widget sender) {
       sender.addStyleName("TreeHighlighted");
        
    }

    public void onMouseLeave(Widget sender) {
        sender.removeStyleName("TreeHighlighted");
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
       if(delay != null){
           delay.cancel();
           delay = null;
       }
        
    }

	public void onMouseOut(MouseOutEvent event) {
		  ((Widget)event.getSource()).removeStyleName("TreeHighlighted");
		
	}

	public void onMouseOver(MouseOverEvent event) {
		 ((Widget)event.getSource()).addStyleName("TreeHighlighted");
		
	}



}
