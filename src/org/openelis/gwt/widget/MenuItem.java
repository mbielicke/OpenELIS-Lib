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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
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
    	
        Grid grid = new Grid(2,2);
        grid.setStyleName("TopMenuRowContainer");
        grid.setCellPadding(0);
        grid.setCellSpacing(0);
        
        grid.getCellFormatter().setStylePrimaryName(0,0,"topMenuIcon");
        grid.setText(0, 0, "");
        grid.getCellFormatter().setStylePrimaryName(0,1,"topMenuItemMiddle");
        
        if(!"".equals(icon))
            grid.getCellFormatter().addStyleName(0, 0, icon);
        else
        	grid.getCellFormatter().setVisible(0, 0, false);
        
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
       
        initWidget(grid);
        
        addHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                addStyleName("Hover");
            }
        },MouseOverEvent.getType());
        
        addHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                removeStyleName("Hover");
            }
        },MouseOutEvent.getType());
        
        addHandler(new MouseDownHandler() {
        	public void onMouseDown(MouseDownEvent event) {
        		removeStyleName("Hover");
        	}
        },MouseDownEvent.getType());
                
        this.autoClose = autoClose;   
        
        setEnabled(true);
    }
    
    /**
     * Method to enable/disable the MenuItem
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(!enabled) {
            unsinkEvents(Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
            addStyleName("disabled");
        }else{
            sinkEvents(Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
            removeStyleName("disabled");
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
    	removeStyleName("Hover");
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
    
    /**
     * We override onAttach to call setEnabled() because of a bug in GWT that will not correctly
     * sink or unsink the events until the widget is attached to the DOM.
     */
    @Override
    protected void onAttach() {
        super.onAttach();
        setEnabled(enabled);
    }
}
