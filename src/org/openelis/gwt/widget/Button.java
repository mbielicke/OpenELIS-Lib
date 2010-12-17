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

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget class implements a Button on the screen. We have not used or
 * extended GWT Button for styling and functionality that we have designed for
 * OpenELIS.
 * 
 * Setting toggle to true will make the button stay pressed until the button is
 * clicked again.
 * 
 * All buttons are defaulted to enabled true. If a button needs to be ensured to
 * be disabled on the initial state of the screen set enable="false" in the xsl.
 */
public class Button extends FocusPanel implements ScreenWidgetInt {

    /*
     * State variables for the button.
     */
    private boolean toggles, enabled, pressed, locked;
    private String  action;
    
    /*
     * Default styles used for the button
     */
    protected String HOVER = "Hover",PRESSED = "Pressed", DISABLED = "DISABLED";

    /**
     * Default no-arg constructor
     */
    public Button() {
        init();
    }
    
    public Button(String icon,String label) {
    	init();
    	setDisplay(icon,label);
    }

    /**
     * Sets up event handling for the button.
     */
    public void init() {
        final Button source = this;

        /**
         * MouseOverHandler to add the Hover Style.
         */
        addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                addStyleName(HOVER);
            }
        });

        /**
         * MouseOutHandler to remove the HoverStyle.
         */
        addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                removeStyleName(HOVER);
            }
        });

        /**
         * Click Handler to check if the button toggles and to set the 
         * Pressed Style.
         */
        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (toggles)
                    setPressed( !pressed);
            }
        });

        /**
         * Change the KeyEvent to ClickEvent and fire on this button.
         */
        addKeyDownHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    NativeEvent clickEvent = com.google.gwt.dom.client.Document.get()
                                                                               .createClickEvent(
                                                                                                 0,
                                                                                                 getAbsoluteLeft(),
                                                                                                 getAbsoluteTop(),
                                                                                                 -1,
                                                                                                 -1,
                                                                                                 event.isControlKeyDown(),
                                                                                                 event.isAltKeyDown(),
                                                                                                 event.isShiftKeyDown(),
                                                                                                 event.isMetaKeyDown());

                    ClickEvent.fireNativeEvent(clickEvent, source);
                    event.stopPropagation();
                }
            }
        });

    }
    
    public void setDisplay(String icon,String label) {
    	Grid grid;
    	grid = new Grid(1,2);
    	grid.setCellPadding(0);
    	grid.setCellSpacing(0);
    	grid.getCellFormatter().setStyleName(0, 0, icon);
    	grid.setText(0, 1, label); 
    	grid.getCellFormatter().setStyleName(0, 1, "ScreenLabel");
    	DOM.setStyleAttribute(grid.getCellFormatter().getElement(0,1),"paddingBottom","3px");
    	setDisplay(grid,true);
    }

    /**
     * Can be used to set the look of the Button on the screen.
     * 
     * @param widget
     *        UI widget used for the display of this button
     */
    public void setDisplay(Widget widget) {
        setDisplay(widget, true);
    }

    /**
     * This method will set the display of the button.  If the wrap parameter 
     * is passed true then the passed widget will be wrapped in the button border
     * styling, if false the passed widget is the only thing set as the display. 
     * @param widget
     * @param wrap
     */
    public void setDisplay(Widget widget, boolean wrap) {
        if (wrap) {
            AbsolutePanel content = new AbsolutePanel();
            content.add(widget);
            content.addStyleName("ButtonContent");
            HorizontalPanel hp = new HorizontalPanel();
            hp.add(new AbsolutePanel());
            hp.getWidget(0).addStyleName("ButtonLeftSide");
            hp.add(content);
            hp.add(new AbsolutePanel());
            hp.getWidget(2).addStyleName("ButtonRightSide");
            
            FocusPanel classPanel = new FocusPanel();
            classPanel.add(hp);
            setWidget(classPanel);
        } else
            setWidget(widget);

    }

    /**
     * If pressed equals true the Pressed style will be applied to the button if the 
     * button allows toggles
     * @param pressed
     */
    public void setPressed(boolean pressed) {
        if(!toggles)
        	return;

        /*
         * Do nothing if pressed state is the same as what 
         * was passed to us
         */
        if (this.pressed == pressed)
            return;

        this.pressed = pressed;
        if (pressed)
            addStyleName(PRESSED);
        else
            removeStyleName(PRESSED);
    }

    /**
     * Unsinks all events on this button leaving it in it's current state for
     * styling but removes functionality.
     */
    public void lock() {
        removeStyleName(HOVER);
        unsinkEvents(Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
        locked = true;
    }

    /**
     * Sinks all events on this button to restore functionality leaving the
     * current state styling.
     */
    public void unlock() {
        sinkEvents(Event.ONCLICK | Event.ONMOUSEOUT | Event.ONMOUSEOVER);
        locked = false;
    }

    /**
     * Call this function with true to enable the button and false to disable.
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setPressed(false);
        if (enabled) {
            unlock();
            removeStyleName(DISABLED);
        } else {
            lock();
            addStyleName(DISABLED);
        }
    }

    /**
     * Method to check if the button is enabled.
     * 
     * @return True if the button is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Method to check if the button is locked.
     * @return
     */
    public boolean isLocked() {
        return locked;
    }
    
    /**
     * Method to check if the button is pressed.
     * @return
     */
    public boolean isPressed() {
        return pressed;
    }
    
    /**
     * Method to set if this button toggles or not.
     * @param toggles
     */
    public void setToggles(boolean toggles) {
        this.toggles = toggles;
    }

    /**
     * Method to add a focus style to the button.
     */
    public void addFocusStyle(String style) {
       addStyleName(HOVER);
    }

    /**
     * Method remove a focus style from the button.
     */
    public void removeFocusStyle(String style) {
       removeStyleName(HOVER);
    }
    
    /**
     * Method used to set the style class to be used for Hovering over the button
     * @param style
     */
    public void setHoverStyle(String style) {
        HOVER = style;
    }
    
    /**
     * Method used to set the style class to be used for a Pressed button.
     * @param style
     */
    public void setPressedStyle(String style) {
        PRESSED = style;
    }
    
    /**
     * Method used to set the style class to be used for a disabled button.
     * @param style
     */
    public void setDisabledStyle(String style) {
        DISABLED = style;
    }
    
    /**
     * This a string value that can be set to the button.  Used mostly for 
     * query strings and useful for distinguishing buttons used in ButtonGroup 
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
    }
    
    /**
     * Returns the action string set for this button.
     * @param action
     * @return
     */
    public String getAction() {
        return action;
    }
    
    
    
    
}