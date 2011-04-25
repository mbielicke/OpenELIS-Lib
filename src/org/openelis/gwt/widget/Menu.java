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
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is will create a display entry that is placed into a MenuBar or into 
 * a Menu to display a sub menu of MenuItems when either moused over or clicked.
 * 
 */
public class Menu extends Composite {

    /**
     * Reference to the open child menu if present
     */
    protected PopupMenuPanel panel;

    /**
     * Flags used for checking if this item has a child Menu and it's position
     * and if enabled
     */
    protected boolean         showBelow, enabled, showPanel;

    /**
     * Reference to Parent Menu
     */
    protected Menu           parent;

    /**
     * No-Arg constructor that sets up Hovering
     */
    public Menu() {
    	
        panel = new PopupMenuPanel();
        panel.addStyleName("MenuPanel");
		panel.setVisible(false);
		
        addHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                addStyleName("Hover");
            }
        }, MouseOverEvent.getType());

        addHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
            	if(!panel.isShowing())
            		removeStyleName("Hover");
            }
        }, MouseOutEvent.getType());
        
    }

    /**
     * This constructor is used to display top level MenuItems in MenuBar.  The String passed
     * will be created into a Label and set as the display for this menu.
     * @param display
     */
    public Menu(String display) {
        this();
        Label<String> label;

        label = new Label<String>(display);
        label.setStyleName("ScreenLabel");
        initWidget(label);
        setEnabled(true);
    }

    /**
     * This constructor will create a MenuItem entry that will contain a MenuArrow to show that
     * this item has a sub menu associated with it.
     * @param icon
     * @param display
     * @param description
     */
    public Menu(String icon, String display, String description) {
        this();
        Grid grid = new Grid(2, 3);
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        grid.setStyleName("TopMenuRowContainer");

        grid.getCellFormatter().setStylePrimaryName(0, 0, "topMenuIcon");
        grid.setText(0, 0, "");
        grid.getCellFormatter().setStylePrimaryName(0, 1, "topMenuItemMiddle");

        if ( !"".equals(icon))
            grid.getCellFormatter().addStyleName(0, 0, icon);
        else
        	grid.getCellFormatter().setVisible(0, 0, false);

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

        grid.getCellFormatter().setStyleName(0, 2, "MenuArrow");
        grid.setText(0,2,"");

        initWidget(grid);
        
        setEnabled(true);
        
        panel.addCloseHandler(new CloseHandler<PopupPanel>() {
			public void onClose(CloseEvent<PopupPanel> event) {
				removeStyleName("Hover");
			}
		});
		
    }

    /**
     * Constructor that takes a Widget as a param to be used as the display
     * widget
     * 
     * @param display
     */
    public Menu(Widget display) {
        this();
        initWidget(display);
        setEnabled(true);
    }

    /**
     * The Command passed will be executed when a user clicks on it.  This method can be called more than once
     * to add multiple commands to the Menu.  The Commands will be executed in the order they are added.
     * @param command
     */
    public void addCommand(final Command command) {
        addHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                command.execute();
            }
        }, ClickEvent.getType());
    }
    
    public void setSelfShow() {
    	addDomHandler(new ClickHandler() {
    		public void onClick(ClickEvent event) {
    			showSubMenu();
    		}
    	},ClickEvent.getType()); 
    }

    /**
     * This method will enable/disable the menu depending on value of the passsed param
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if ( !enabled) {
            unsinkEvents(Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
            addStyleName("disabled");
        } else {
            sinkEvents(Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
            removeStyleName("disabled");
        }
    }
    
    /**
     * Method used to determine if the Menu is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * This method allows containing widgets to addd a MouseOverHandler to this menu
     * @param handler
     * @return
     */
    protected HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addHandler(handler, MouseOverEvent.getType());
    }

    /**
     * Adds a child item to this item
     * 
     * @param panel
     * @param showBelow
     */
    public void addItem(MenuItem item) {
    	showPanel = true;
        panel.addItem(item);
        if (item.autoClose()) {
            item.addCommand(new Command() {
                public void execute() {
                    Menu pMenu;

                    pMenu = parent;
                    while (pMenu != null) {
                        pMenu.panel.hide();
                        pMenu = pMenu.getParentMenu();
                    }
                }
            });
        }

    }

    /**
     * Adds a Menu as ant entry to this Menu.
     * @param menu
     */
    public void addItem(Menu menu) {
    	showPanel = true;
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
    protected PopupMenuPanel showSubMenu() {
    	
        if (panel.isShowing() || !showPanel)
            return panel;

        /*
        if (showBelow)
            panel.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + getOffsetHeight());
        else
            panel.setPopupPosition(getAbsoluteLeft() + getOffsetWidth(), getAbsoluteTop());

		*/
        
        panel.showRelativeTo(this);

        return panel;
    }

    /**
     * Method to set the containing Menu of this Menu
     * @param parent
     */
    protected void setParentMenu(Menu parent) {
        this.parent = parent;
    }

    /**
     * Method to return the containing Menu of this menu
     * @return
     */
    protected Menu getParentMenu() {
        return parent;
    }
    
    /**
     * We override onAttach to call setEnabled() because of a bug in GWT that will not correctly
     * sink or unsink the events until the widget is attached to the DOM.
     */
    @Override
    protected void onAttach() {
        super.onAttach();
        setEnabled(enabled);
    }
    
    public void hideArrow() {
    	((Grid)getWidget()).getCellFormatter().setVisible(0, 2, false);
    }

    public void showArrow() {
    	((Grid)getWidget()).getCellFormatter().setVisible(0, 2, true);
    }
}
