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

import org.openelis.gwt.screen.ShortcutHandler;
import org.openelis.gwt.screen.TabHandler;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 *   This widget class implements a Button on the screen.  We have not used or extended GWT Button for styling and 
 *   functionality that we have designed for OpenELIS.  
 *   
 *   Setting toggle to true will make the button stay pressed until the button is clicked again.
 *   
 *   All buttons are defaulted to enabled true.  If a button needs to be ensured to be disabled on the initial state of the
 *   screen set enable="false" in the xsl.
 */
public class AppButton extends Composite implements MouseOutHandler, MouseOverHandler, HasClickHandlers, ClickHandler, Focusable, BlurHandler, HasField, KeyPressHandler,HasMouseOutHandlers,HasMouseOverHandlers{     
    
    public enum ButtonState {UNPRESSED,PRESSED,DISABLED,LOCK_PRESSED}
    
    private ButtonState state = ButtonState.UNPRESSED;
    public String action;
    private boolean toggle;
    private FocusPanel panel = new FocusPanel();
	private FocusPanel classPanel = new FocusPanel();
    private HorizontalPanel hp = new HorizontalPanel();
    private AbsolutePanel right = new AbsolutePanel();
    private AbsolutePanel left = new AbsolutePanel();
    private AbsolutePanel content = new AbsolutePanel();
    private boolean enabled;
    private ShortcutHandler shortcut;
    private boolean locked;
    
    /**
     *   Default No-Arg constructor.  Event handlers are added at, but not sunk until enabe(true) is called.   
     */
    public AppButton() {
        hp.add(left);
        hp.add(content);
        hp.add(right);
        classPanel.add(hp);
        panel.add(classPanel);
        initWidget(panel);
        left.addStyleName("ButtonLeftSide");
        right.addStyleName("ButtonRightSide");
        content.addStyleName("ButtonContent");
        addMouseOutHandler(this);
        addMouseOverHandler(this);
        addClickHandler(this);
        panel.addBlurHandler(this);
        panel.addKeyPressHandler(this);
        panel.unsinkEvents(Event.ONKEYPRESS);
    }
    
    /**
     * Sinks a KeyPressEvent for this widget attaching a TabHandler that will override the default
     * browser tab order for the tab order defined by the screen for this widget.
     * 
     * @param handler
     *        Instance of TabHandler that controls tabing logic for widget.
     */
    public void addTabHandler(TabHandler handler) {
    	addDomHandler(handler,KeyPressEvent.getType());
    }
    
    /**
     * Can be used to set the look of the Button on the screen.
     * 
     * @param widget
     *        UI widget used for the display of this button
     */
    public void setWidget(Widget widget) {
        content.add(widget);
    }
    
    /**
     * Sets the current look and functionality of a button on the screen.
     * 
     * @param state
     *        an Enumeration with values {UNPRESSED,PRESSED,DISABLED,LOCK_PRESSED}
     */
    public void setState(ButtonState state){
        this.state = state;
         if(state == ButtonState.UNPRESSED){
            panel.removeStyleName("disabled");
            panel.removeStyleName("Pressed");
            unlock();
        }
        if(state == ButtonState.PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
            unlock();
        }
        if(state == ButtonState.DISABLED){
            panel.removeStyleName("Pressed");
            panel.addStyleName("disabled");
            lock();
        }
        if(state == ButtonState.LOCK_PRESSED){
            panel.removeStyleName("disabled");
            panel.addStyleName("Pressed");
            lock();
        }
    }
    
    /**
     * Unsinks all events on this button leaving it in it's current state for styling 
     * but removes functionality.
     */
    public void lock(){
        panel.removeStyleName("Hover");
        locked = true;
        unsinkEvents(Event.ONCLICK);
        panel.unsinkEvents(Event.ONMOUSEOUT);
        panel.unsinkEvents(Event.ONMOUSEOVER);
    }
    
    /**
     * Sinks all events on this button to restore functionality leaving the current state
     * styling.
     */
    public void unlock(){
        sinkEvents(Event.ONCLICK);
        panel.sinkEvents(Event.ONMOUSEOUT);
        panel.sinkEvents(Event.ONMOUSEOVER);
        locked = false;
        
    }
   
    /**
     * Sets the current CSS style to the widget. 
     * 
     * @param styleName
     *        Corresponding CSS class name 
     */
    public void setStyleName(String styleName){
	  classPanel.setStyleName(styleName);
    }

    /**
     * Registers a ClickHandler to the button but does not sink the event.  Call enable(true) to sink 
     * the event.
     * 
     * @param handler
     *        instance of ClickHandler to be registered to the button.
     */
	public HandlerRegistration addClickHandler(ClickHandler handler){
	    return addHandler(handler, ClickEvent.getType());
	}

	public void setFocus() {
		panel.addStyleName("Hover");
		//sinkEvents(Event.ONKEYUP);
	}
	
	public void blur() {
		panel.removeStyleName("Hover");
		//unsinkEvents(Event.ONKEYUP);
	}
	
	/**
	 * Removes "Hover" style from Button when event received.
	 * 
	 * @param event
	 */
    public void onMouseOut(MouseOutEvent event) {
        panel.removeStyleName("Hover");
    }

    /**
     * Adds "Hover" style to the button when the event is received and if the
     * button is enabled
     * 
     * @param event
     */
    public void onMouseOver(MouseOverEvent event) {
        if(enabled)
            panel.addStyleName("Hover");
    }
    
    /**
     * Call this function with true to enable the button and false to disable.
     * 
     * @param enabled
     */
    public void enable(boolean enabled) {
    	this.enabled = enabled;
    	if(enabled)
    		setState(ButtonState.UNPRESSED);
    	else
    		setState(ButtonState.DISABLED);
    }
    
    /**
     * Method to check if the button is enabled.
     * 
     * @return
     *      True if the button is enabled
     */
    public boolean isEnabled() {
    	return enabled;
    }
    
    /**
     * This method is used to handle toggle if the Button's toggle value is set to true.
     */
    public void onClick(ClickEvent event) {
    	if(toggle) {
    		if(state == ButtonState.UNPRESSED)
    			setState(ButtonState.PRESSED);
    		else if(state == ButtonState.PRESSED)
    			setState(ButtonState.UNPRESSED);
    	}
    }
    
    /**
     * Returns the current state of the Button.
     * 
     * @return
     *    an Enumeration with a value of {UNPRESSED,PRESSED,DISABLED,LOCK_PRESSED}
     */
    public ButtonState getState() {
		return state;
	}

    /**
     * Returns the action value assigned to this button.
     * 
     * @return 
     *      A string value that can be assigned to the button in the xsl definition
     *      and can be inspected in a click handler
     */
	public String getAction() {
		return action;
	}

	/**
	 * Sets the action value for this button.
	 * 
	 * @param action
	 *      A string value that can be assigned to the button in the xsl definition
     *      and can be inspected in a click handler
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Method to check if the Buttons is in toggle mode.
	 * @return
	 *     boolean value that returns true if button is in toggle mode
	 */
	public boolean isToggle() {
		return toggle;
	}

	/**
	 * A method to put this button into and out of toggle mode.
	 * 
	 * @param toggle
	 *        boolean value
	 */
	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}

	public int getTabIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setAccessKey(char key) {
		// TODO Auto-generated method stub
		
	}

	public void setFocus(boolean focused) {
		if(focused){
			panel.addStyleName("Hover");
			panel.sinkEvents(Event.ONKEYPRESS);
		}else{
			panel.removeStyleName("Hover");
			panel.unsinkEvents(Event.ONKEYPRESS);
		}
		panel.setFocus(focused);
	}

	public void setTabIndex(int index) {
		// TODO Auto-generated method stub
		
	}

	public void onBlur(BlurEvent event) {
		panel.removeStyleName("Hover");
		
	}

	public void addException(Exception exception) {
		// TODO Auto-generated method stub
		
	}

	public void addExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	public HandlerRegistration addFieldValueChangeHandler(
			ValueChangeHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	public void checkValue() {
		// TODO Auto-generated method stub
		
	}

	public void clearExceptions() {
		// TODO Auto-generated method stub
		
	}

	public ArrayList getExceptions() {
		// TODO Auto-generated method stub
		return null;
	}

	public Field getField() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getFieldValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void getQuery(ArrayList list, String key) {
		// TODO Auto-generated method stub
		
	}

	public Object getWidgetValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeExceptionStyle(String style) {
		// TODO Auto-generated method stub
		
	}

	public void setField(Field field) {
		// TODO Auto-generated method stub
		
	}

	public void setFieldValue(Object value) {
		// TODO Auto-generated method stub
		
	}

	public void setQueryMode(boolean query) {
		// TODO Auto-generated method stub
		
	}

	public void onKeyPress(KeyPressEvent event) {
		if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
			ClickEvent.fireNativeEvent(Document.get().createClickEvent(0, 0, 0, 0, 0, false, false, false, false), this);
		}
	}

	public boolean isLocked() {
		return locked;
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

}
