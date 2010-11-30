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
package org.openelis.gwt.widget;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ModalWindow extends Window {
    
    private AbsolutePanel modalPanel;
    private AbsolutePanel modalGlass;
    private PickupDragController dragController;
    private AbsolutePositionDropController dropController;
    public static final int position=100;
    
    public ModalWindow() {
        super();
        modalGlass = new AbsolutePanel();
        modalGlass.setStyleName("GlassPanel");
        modalGlass.setHeight(com.google.gwt.user.client.Window.getClientHeight()+"px");
        modalGlass.setWidth(com.google.gwt.user.client.Window.getClientWidth()+"px");
        
        RootPanel.get().add(modalGlass);
        RootPanel.get().setWidgetPosition(modalGlass, 0, 0);
        modalPanel = new AbsolutePanel();
        modalPanel.setStyleName("ModalPanel");
        modalPanel.setHeight(com.google.gwt.user.client.Window.getClientHeight()+"px");
        modalPanel.setWidth(com.google.gwt.user.client.Window.getClientWidth()+"px");
        modalPanel.add(this,position,position);
        RootPanel.get().add(modalPanel); 
        RootPanel.get().setWidgetPosition(modalPanel,0,0);
        setVisible(true);
        dragController = new PickupDragController(modalPanel,true);
        dropController = new AbsolutePositionDropController(modalPanel);
        dragController.registerDropController(dropController);
        dragController.makeDraggable(this,cap);
        dragController.setBehaviorDragProxy(true);
    }
    
    public void setContent(Widget content, int x, int y) {
        modalPanel.setWidgetPosition(this, x, y);        
        setContent(content);
    }
    
    @Override
    public void close() {
        if(modalGlass != null) {
            removeFromParent();
            RootPanel.get().remove(modalGlass);
            RootPanel.get().remove(modalPanel);
            //return;
        }
        super.close();
    }

}
