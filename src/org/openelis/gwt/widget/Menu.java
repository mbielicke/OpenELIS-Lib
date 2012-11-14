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

import java.util.Iterator;

import org.openelis.gwt.resources.MenuCSS;
import org.openelis.gwt.resources.OpenELISResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is will create a display entry that is placed into a MenuBar or into 
 * a Menu to display a sub menu of MenuItems when either moused over or clicked.
 * 
 */
public class Menu extends Composite implements HasWidgets {
	@UiTemplate("Menu.ui.xml")
	interface MenuUiBinder extends UiBinder<HTML, Menu>{};
	public static final MenuUiBinder uiBinder = GWT.create(MenuUiBinder.class);

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
    
    protected MenuCSS        css;
    
    protected SimplePanel    outer;
    
    @UiField
    protected TableCellElement icon,display,arrow,description;
    
    @UiField
    protected TableRowElement bottomRow;

    /**
     * No-Arg constructor that sets up Hovering
     */
    public Menu() {
    	outer = new SimplePanel();
    	initWidget(outer);
    	outer.setWidget(uiBinder.createAndBindUi(this));
    	
    	css = OpenELISResources.INSTANCE.menuCss();
    	css.ensureInjected();
    	
        panel = new PopupMenuPanel();
        panel.addStyleName(css.MenuPanel());
		panel.setVisible(false);		
		
		setEnabled(true);
    }

    /**
     * This constructor is used to display top level MenuItems in MenuBar.  The String passed
     * will be created into a Label and set as the display for this menu.
     * @param display
     */
    public void setLabel(String label) {
    	com.google.gwt.user.client.ui.Label display = new com.google.gwt.user.client.ui.Label(label);
        //display.setStyleName(css.ScreenLabel());
        display.setWordWrap(false);
        outer.setWidget(display);
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
    	setIcon(icon);
    	setDisplay(display);
    	setDescription(description);
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
            addStyleName(css.disabled());
        } else {
            sinkEvents(Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
            removeStyleName(css.disabled());
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
    
    public void setIcon(String icon) {
    	this.icon.setAttribute("class",icon);
    }
    
    public void setDisplay(String display) {
    	this.display.setInnerText(display);
    }
    
    public void setDescription(String description) {
    	if(description != null || "".equals(description)){
    		this.description.setInnerText(description);
    		bottomRow.removeAttribute("style");
    	}else
    		bottomRow.setAttribute("style", "display:none;");
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
    	if(arrow != null)
    		arrow.setAttribute("style","display:none;");
    }

    public void showArrow() {
    	if(arrow != null)
    		arrow.removeAttribute("style");
    }

	@Override
	public void add(Widget w) {
		if(w instanceof MenuItem)
			addItem((MenuItem)w);
		else if(w instanceof Menu)
			addItem((Menu)w);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		// TODO Auto-generated method stub
		return false;
	}
}
