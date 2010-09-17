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

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;

/**
 * This class is used by MenuBar and MenuPanel to display menu options 
 *
 */
public class UMenuItem  extends FocusPanel {

    protected boolean autoClose;
    
    public UMenuItem(String icon, String display, String description) {
        this(icon,display,description,true);
    }
    
    public UMenuItem(String icon, String display, String description, boolean autoClose) {
        Grid grid = new Grid(2,4);
        grid.setStyleName("TopMenuRowContainer");
        
        grid.getCellFormatter().setStylePrimaryName(0,0,"topMenuIcon");
        grid.getCellFormatter().setStylePrimaryName(0,1,"topMenuItemMiddle");
        
        if(!"".equals(icon))
            grid.getCellFormatter().addStyleName(0, 0, icon);
        
        grid.setText(0,1,display);
        grid.getCellFormatter().addStyleName(0,1,"topMenuItemTitle");
        grid.getCellFormatter().addStyleName(0,1,"locked");
        
        if("".equals(description))
            grid.removeRow(1);
        else{
            grid.setText(1,1,description);
            grid.getCellFormatter().setStylePrimaryName(1,1,"topMenuItemMiddle");
            grid.getCellFormatter().addStyleName(1,1,"topMenuItemDesc");
        }
       
        setWidget(grid);
        
        addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                addStyleName("Hover");
            }
        });
        
        addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                removeStyleName("Hover");
            }
        });
        
        this.autoClose = autoClose;
        
    }
    
    protected boolean autoClose() {
        return autoClose;
    }
    
}
