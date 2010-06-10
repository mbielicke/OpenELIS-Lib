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

import org.openelis.gwt.screen.TabHandler;

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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
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
public class AppButton extends FocusPanel implements ScreenWidgetInt {

    private boolean toggles, enabled, pressed, locked;

    public AppButton() {
        init();
    }

    public void init() {
        final AppButton source = this;

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

        addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if (toggles)
                    setPressed( !pressed);
            }
        });

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

    /**
     * Sinks a KeyPressEvent for this widget attaching a TabHandler that will
     * override the default browser tab order for the tab order defined by the
     * screen for this widget.
     * 
     * @param handler
     *        Instance of TabHandler that controls tabing logic for widget.
     */
    public void addTabHandler(TabHandler handler) {
        addDomHandler(handler, KeyDownEvent.getType());
    }

    /**
     * Can be used to set the look of the Button on the screen.
     * 
     * @param widget
     *        UI widget used for the display of this button
     */
    public void setWidget(Widget widget) {
        setWidget(widget, true);
    }

    public void setWidget(Widget widget, boolean wrap) {
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
            super.setWidget(classPanel);
        } else
            super.setWidget(widget);

    }

    public void setPressed(boolean pressed) {
        assert toggles == true;

        if (this.pressed == pressed)
            return;

        this.pressed = pressed;
        if (pressed)
            addStyleName("Pressed");
        else
            removeStyleName("Pressed");
    }

    /**
     * Unsinks all events on this button leaving it in it's current state for
     * styling but removes functionality.
     */
    public void lock() {
        removeStyleName("Hover");
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
        if (enabled) {
            unlock();
            removeStyleName("disabled");
        } else {
            lock();
            addStyleName("disabled");
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
    
    public boolean isLocked() {
        return locked;
    }
    
    public boolean isPressed() {
        return pressed;
    }
    
    public void setToggles(boolean toggles) {
        this.toggles = toggles;
    }

    public void addFocusStyle(String style) {
       addStyleName("Hover");
    }

    public void removeFocusStyle(String style) {
       removeStyleName("Hover");
    }
}