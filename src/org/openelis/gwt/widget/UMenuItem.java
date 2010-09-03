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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

/**
 * This clas is used by MenuBar and MenuPanel to display menu options 
 *
 */
public class UMenuItem  extends FocusPanel {
  
    /**
     * Reference to the open child menu if present
     */
    protected UMenuPanel childMenu;
    
    /**
     * Flags used for checking if this item has a child Menu and it's position 
     */
    protected boolean hasChild, showBelow;
    
    /**
     * No-Arg constructor that sets up Hovering
     */
    public UMenuItem() {
       
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
        
    }
    
    /**
     * Constructor that takes a Widget as a param to be used as the display widget
     * @param display
     */
    public UMenuItem(Widget display) {
        this();
        setDisplay(display);
    }
    
    /**
     * Method used to set the widget used for displaying this item
     * @param display
     */
    public void setDisplay(Widget display) {
        setWidget(display);
    }
    
    /**
     * Adds a child menu to this item 
     * @param panel
     * @param showBelow
     */
    public void addChildPanel(UMenuPanel panel, final boolean showBelow) {
        childMenu = panel;
        hasChild = true;
        this.showBelow = showBelow;
    }
   
    /**
     * Method used to determine if this item has a child menu
     * @return
     */
    protected boolean hasChildMenu() {
        return hasChild;
    }
    
    /**
     * Method called to show the child menu
     * @return
     */
    protected UMenuPanel showChild() {
        if(childMenu == null || childMenu.isShowing())
            return null;
        
        if(showBelow)
            childMenu.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop()+getOffsetHeight());
         else
            childMenu.setPopupPosition(getAbsoluteLeft() + getOffsetWidth(), getAbsoluteTop());
        
        childMenu.show();
        
        return childMenu;
    }
        
    /**
     * Static method that can be used by other classes to create the display for a default MenuBar Item 
     * @param text
     * @return
     */
    public static Widget createMenuBarItem(String text) {
        Label label;
        
        label = new Label(text);
        label.setStyleName("ScreenLabel");
        return label;
    }
    
    /**
     * Static method that can be called by other classed to create the display for a default Application
     * Menu item
     * @param icon
     * @param text
     * @param description
     * @param hasChild
     * @return
     */
    public static Widget createAppMenuItem(String icon, String text, String description, boolean hasChild) {
        Grid grid = new Grid(2,4);
        grid.setStyleName("TopMenuRowContainer");
        
        grid.getCellFormatter().setStylePrimaryName(0,0,"topMenuIcon");
        grid.getCellFormatter().setStylePrimaryName(0,1,"topMenuItemMiddle");
        if(!"".equals(icon))
            grid.getCellFormatter().addStyleName(0, 0, icon);
        grid.setText(0,1,text);
        grid.getCellFormatter().addStyleName(0,1,"topMenuItemTitle");
        grid.getCellFormatter().addStyleName(0,1,"locked");
        
        if(hasChild) 
            grid.getCellFormatter().setStyleName(0,3,"MenuArrow");
        
        if("".equals(description))
            grid.removeRow(1);
        else{
            grid.setText(1,1,description);
            grid.getCellFormatter().setStylePrimaryName(1,1,"topMenuItemMiddle");
            grid.getCellFormatter().addStyleName(1,1,"topMenuItemDesc");
        }
       
        return grid;
    }
    
    /**
     * Static method that can be called by other classes to create the display for a default FilterMenu;
     * @param text
     * @return
     */
    public static Widget createFilterMenuItem(String text) {
        Grid grid;
        CheckBox check;
        
        grid = new Grid(1,2);
        grid.setStyleName("TopMenuRowContainer");
        
        grid.getCellFormatter().setStylePrimaryName(0,0,"topMenuIcon");
        grid.getCellFormatter().setStylePrimaryName(0,1,"topMenuItemMiddle");
        
        check = new CheckBox();
        check.setEnabled(true);
        grid.setWidget(0, 0, check);
        
        grid.setText(0, 1, text);
        grid.getCellFormatter().addStyleName(0,1,"topMenuItemTitle");
        grid.getCellFormatter().addStyleName(0,1,"locked");
        
        return grid;
    }
    
}
