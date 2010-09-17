/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
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
public class UMenu extends FocusPanel {

    /**
     * Reference to the open child menu if present
     */
    protected UMenuPanel panel;

    /**
     * Flags used for checking if this item has a child Menu and it's position
     */
    protected boolean    showBelow;

    /**
     * Reference to Parent Menu
     */
    protected UMenu      parent;

    /**
     * No-Arg constructor that sets up Hovering
     */
    public UMenu() {

        panel = new UMenuPanel();
        panel.addStyleName("MenuPanel");

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

    public UMenu(String display) {
        this();
        Label label;

        label = new Label(display);
        label.setStyleName("ScreenLabel");
        setWidget(label);
    }

    public UMenu(String icon, String display, String description) {
        this();
        Grid grid = new Grid(2, 4);
        grid.setStyleName("TopMenuRowContainer");

        grid.getCellFormatter().setStylePrimaryName(0, 0, "topMenuIcon");
        grid.getCellFormatter().setStylePrimaryName(0, 1, "topMenuItemMiddle");

        if ( !"".equals(icon))
            grid.getCellFormatter().addStyleName(0, 0, icon);

        grid.setText(0, 1, display);
        grid.getCellFormatter().addStyleName(0, 1, "topMenuItemTitle");
        grid.getCellFormatter().addStyleName(0, 1, "locked");

        if ("".equals(description))
            grid.removeRow(1);
        else {
            grid.setText(1, 1, description);
            grid.getCellFormatter().setStylePrimaryName(1, 1, "topMenuItemMiddle");
            grid.getCellFormatter().addStyleName(1, 1, "topMenuItemDesc");
        }

        grid.getCellFormatter().setStyleName(0, 3, "MenuArrow");

        setWidget(grid);
    }

    /**
     * Constructor that takes a Widget as a param to be used as the display
     * widget
     * 
     * @param display
     */
    public UMenu(Widget display) {
        this();
        setDisplay(display);
    }

    /**
     * Method used to set the widget used for displaying this item
     * 
     * @param display
     */
    public void setDisplay(Widget display) {
        setWidget(display);
    }

    /**
     * Adds a child menu to this item
     * 
     * @param panel
     * @param showBelow
     */
    public void addItem(UMenuItem item) {
        panel.addItem(item);
        if (item.autoClose()) {
            item.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    UMenu pMenu;

                    panel.hide();
                    pMenu = parent;
                    while (pMenu != null) {
                        pMenu.panel.hide();
                        pMenu = pMenu.getParentMenu();
                    }
                }
            });
        }

    }

    public void addItem(UMenu menu) {
        panel.addItem(menu);
        menu.setParentMenu(this);
    }

    /**
     * Adds a line into the item's subMenu to create a separation in the panel
     */
    public void addMenuSeparator() {
        panel.addMenuSeparator();
    }

    /**
     * This method will set the flag to have the item show its subMenu either
     * below the item or to the side of the item.
     * 
     * @param showBelow
     */
    public void showBelow(boolean showBelow) {
        this.showBelow = showBelow;
    }

    /**
     * Method called to show the child menu
     * 
     * @return
     */
    protected UMenuPanel showSubMenu() {
        if (panel.isShowing())
            return panel;

        if (showBelow)
            panel.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + getOffsetHeight());
        else
            panel.setPopupPosition(getAbsoluteLeft() + getOffsetWidth(), getAbsoluteTop());

        panel.show();

        return panel;
    }

    protected void setParentMenu(UMenu parent) {
        this.parent = parent;
    }

    protected UMenu getParentMenu() {
        return parent;
    }

}
