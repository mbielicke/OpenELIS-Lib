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

import java.util.ArrayList;

import org.openelis.gwt.resources.MenuCSS;
import org.openelis.gwt.resources.OpenELISResources;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;

public class MenuItem extends Composite {
	
	protected String icon,display,description;
	
	protected ArrayList<Command> commands;

    /**
     * Flags to determine if the MenuItem should autoClose all menus and 
     * if the menuItem is enabled.
     */

	protected boolean autoClose, enabled;
	
	protected MenuCSS css = OpenELISResources.INSTANCE.menuCss();
	
	protected int eventsToSink;
	
	protected Grid grid;
    
    /**
     * Constructor that will create a MenuItem that will autoClose by default.
     * @param icon
     * @param display
     * @param description
     */
    public MenuItem(String icon, String display, String description) {
        this(icon,display,description,true);
    }
    
    /**
     * Constructor that will crate a MenuItem that wll autoClose by the param passed
     * @param icon
     * @param display
     * @param description
     * @param autoClose
     */
    public MenuItem(String icon, String display, String description, boolean autoClose) {
    	this.icon = icon;
    	this.display = display;
    	this.description = description;
    	    	
        grid = new Grid(2,2);

        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        

        grid.setText(0, 0, "");

        
        if(!"".equals(icon))
            grid.getCellFormatter().addStyleName(0, 0, icon);
        else
        	grid.getCellFormatter().setVisible(0, 0, false);
        
        grid.setText(0,1,display);

        
        if("".equals(description))
            grid.removeRow(1);
        else
            grid.setText(1,1,description);

        initWidget(grid);
                        
        this.autoClose = autoClose;   
        
        setEnabled(true);
        
        setCSS(OpenELISResources.INSTANCE.menuCss());
    }
    
    /**
     * Method to enable/disable the MenuItem
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(!enabled) {
            unsinkEvents(Event.ONCLICK);
            addStyleName(css.disabled());
        }else{
            sinkEvents(Event.ONCLICK);
            removeStyleName(css.disabled());
        }
    }
    
    public boolean isEnabled() {
    	return enabled;
    }
    
    /**
     * The Command passed will be executed when a user clicks on it.  This method can be called more than once
     * to add multiple commands to the MenuItem.  The Commands will be executed in the order they are added.
     * @param command
     */
    public void addCommand(final Command command) {
    	if(commands == null) {
    		commands = new ArrayList<Command>();
    		addHandler(new ClickHandler() {
    			public void onClick(ClickEvent event) {
    				execute();
    			}
    		}, ClickEvent.getType());
    	}
    	commands.add(command);
    }
    
    public void execute() {
		for(Command comm : commands) 
			comm.execute();
    }
    
    public ArrayList<Command> getCommands() {
    	return commands;
    }
    
    /**
     * Method used to determine if the All Menus should be closed when this item is clicked
     * @return
     */
    protected boolean autoClose() {
        return autoClose;
    }
    
    public String getIcon() {
    	return icon;
    }
    
    public String getDisplay() {
    	return display;
    }
    
    public String getDescription() {
    	return description;
    }
    
    /**
     * Method used by containing widgets to register a MouseOverHandler to this MenuItem.
     * @param handler
     * @return
     */
    protected HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addHandler(handler,MouseOverEvent.getType());
    }
    
    public void setCSS(MenuCSS css) {
    	css.ensureInjected();
    	
    	if(!isEnabled()) {
    		removeStyleName(this.css.disabled());
    		addStyleName(css.disabled());
    	}
    	
    	this.css = css;
    	
        grid.setStyleName(css.TopMenuRowContainer());
        grid.getCellFormatter().setStylePrimaryName(0,0,css.topMenuIcon());
        grid.getCellFormatter().setStylePrimaryName(0,1,css.topMenuItemMiddle());
        grid.getCellFormatter().addStyleName(0,1,css.topMenuItemTitle());
        grid.getCellFormatter().addStyleName(0,1,css.locked());
        
        if(grid.getRowCount() > 1) {
        	grid.getCellFormatter().setStylePrimaryName(1,0,css.topMenuIcon());
            grid.getCellFormatter().setStylePrimaryName(1,1,css.topMenuItemMiddle());
            grid.getCellFormatter().addStyleName(1,1,css.topMenuItemDesc());
        }
    	
    }
	/**
	 * These methods were added to ensure the button will be correctly enabled or disabled 
	 * when it is first drawn.
	 */
	@Override
	public void sinkEvents(int eventBitsToAdd) {
		if(isOrWasAttached())
			super.sinkEvents(eventBitsToAdd);
		else
			eventsToSink |= eventBitsToAdd;
	}
    
	@Override
	public void unsinkEvents(int eventBitsToRemove) {
		if(isOrWasAttached())
			super.unsinkEvents(eventBitsToRemove);
		else
			eventsToSink &= ~eventBitsToRemove;
	}
    
	@Override
	protected void onAttach() {
		super.onAttach();
		super.sinkEvents(eventsToSink);
	}
}
