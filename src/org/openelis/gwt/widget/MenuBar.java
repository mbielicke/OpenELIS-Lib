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

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
/**
 *  This class will display MenuItems in Horizontal row and display Menu 
 *  panels below the items
 * 
 */
public class MenuBar extends Composite {
   
    /**
     * Panel that holds the MenuItems
     */
    protected HorizontalPanel panel;
    
    /**
     * Reference to currently displayed child menu 
     */
    protected PopupMenuPanel popMenu;
    
    /**
     * No-Arg constructor 
     */
    public MenuBar() {
        AbsolutePanel ap;
       
        panel = new HorizontalPanel();
        
        /* Add empty div and set to 100% width so items added before will align to the right */
        ap = new AbsolutePanel();
        panel.add(ap);
        panel.setCellWidth(ap, "100%");
        
        initWidget(panel);
        
        setStyleName("topMenuBar");
    }

    /**
     * Method will add a MenuItem to the bar to be displayed.
     * @param menu
     */
    public void addMenu(final Menu menu) {
        panel.insert(menu,panel.getWidgetCount()-1);
        menu.setStyleName("topMenuBarItem");
        menu.showBelow(true);
        menu.hideArrow();
       
        menu.addCommand(new Command() {
            public void execute() {
                popMenu = menu.showSubMenu();
            }
        });
        
        menu.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                menu.addStyleName("Hover");
                if(popMenu != null && popMenu.isShowing()) {
                    popMenu.hide();
                    popMenu = menu.showSubMenu();
                }
            }
        });
    }
    

}
