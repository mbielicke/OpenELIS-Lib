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

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;

/**
 * This class is used to display and edit a CheckBox in Screen forms and
 * TableCells
 * 
 * @author tschmidt
 * 
 */
public class CheckBox extends FocusPanel implements ScreenWidgetInt, Queryable, HasBlurHandlers,
                                        HasFocusHandlers, HasValueChangeHandlers<String>,
                                        HasValue<String>, HasExceptions {

    /*
     * Enum to define checkbox modes.  This can be removed if it is decided that
     * a chekbox can only have three states in query mode only and can do normal 
     * input as a three state.
     */
    public enum Mode {
        TWO_STATE, THREE_STATE
    };

    /*
     * Enum to define possible check values and styles
     */
    public enum Value {
        UNCHECKED("N", "Unchecked"), CHECKED("Y", "Checked"), UNKNOWN(null, "Unknown");

        private String value;
        private String style;

        Value(String value, String style) {
            this.value = value;
            this.style = style;
        }

        public String getValue() {
            return value;
        }

        public String getStyle() {
            return style;
        }

        public static Value getValue(String value) {
            if ("Y".equals(value))
                return Value.CHECKED;
            else if ("N".equals(value))
                return Value.UNCHECKED;
            else
                return Value.UNKNOWN;
        }
    };

    /*
     * Fields for state and value
     */
    private Value                           value = Value.UNCHECKED;
    private Mode                            mode  = Mode.TWO_STATE;

    /*
     * Fields for query mode
     */
    protected boolean                       queryMode, enabled;

    /*
     * Fields used for Exceptions
     */
    protected ArrayList<LocalizedException> endUserExceptions, validateExceptions;

    /**
     * Default no-arg constructor
     */
    public CheckBox() {
        init();
    }

    /**
     * Constructor to set the mode of the Checkbox
     * 
     * @param mode
     */
    public CheckBox(Mode mode) {
        init();
        setMode(mode);
    }

    /**
     * This method will set the Checkbox display and set Event handlers
     */
    public void init() {
        setValue(Value.UNCHECKED.getValue());

        /**
         * If clicked call changeValue to rotate the state of the checkbox based
         * on its mode.
         */
        addHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                changeValue();
            }
        }, ClickEvent.getType());

        /**
         * If Enter key hit while focused call changeValue to rotate the state
         * of the checkbox based on its mode.
         */
        addHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    changeValue();
                    //event.stopPropagation();
                }
            }
        }, KeyDownEvent.getType());
    }

    /**
     * Changes the checkbox to be either TWO_STATE or THREE_STATE
     * 
     * @param type
     */
    public void setMode(Mode type) {
        this.mode = type;
        if (type == Mode.THREE_STATE)
            setValue(Value.UNKNOWN.getValue());
        else
            setValue(Value.UNCHECKED.getValue());
    }

    /**
     * Returns what mode the Checkbox is currently in
     * 
     * @return
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Method called form event handlers to switch the value of the checkbox
     */
    public void changeValue() {
        switch (value) {
            case CHECKED:
                setValue(Value.UNCHECKED.getValue(), true);
                break;
            case UNCHECKED:
                if (mode == Mode.THREE_STATE || queryMode)
                    setValue(Value.UNKNOWN.getValue(), true);
                else
                    setValue(Value.CHECKED.getValue(), true);
                break;
            case UNKNOWN:
                setValue(Value.CHECKED.getValue(), true);
                break;
        }
        ;
    }

    // ******** Implementation of ScreenWidgetInt ***********************
    /**
     * Method to enable/disable the checkbox
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled)
            sinkEvents(Event.ONCLICK | Event.ONKEYDOWN);
        else
            unsinkEvents(Event.ONCLICK | Event.ONKEYDOWN);
    }

    /**
     * Method to determine if the checkbox is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the focus stlye to this widget
     */
    public void addFocusStyle(String style) {
        addStyleName(style);
    }

    /**
     * Removes the focus style from the widget
     */
    public void removeFocusStyle(String style) {
        removeStyleName(style);
    }

    // ********* Implementation of Queryable *****************

    /**
     * Puts Checkbox into and out of Query Mode
     */
    public void setQueryMode(boolean query) {
        queryMode = query;
    }

    /**
     * Returns a QueryData object of type string only if checkbox is set to "Y"
     * or "N"
     */
    public Object getQuery() {
        if(!queryMode)
        	return null;

        if (value == Value.UNKNOWN)
            return null;

        return new QueryData(QueryData.Type.STRING,value.getValue());
    }
    
    /**
     * This method is called when setting a query value in a table.  
     */
    public void setQuery(QueryData qd) {
        if(qd != null)
            setValue(qd.getQuery());
        else
            setValue(null);
    }

    // ******* Implementation of HasValue<String> ******

    /**
     * Returns the current value for this widget
     */
    public String getValue() {
        return value.getValue();
    }

    /**
     * Sets the value of this widget without firing value change event
     */
    public void setValue(String value) {
        setValue(value, false);
    }

    /**
     * Sets the value of this widget. Will fire a ValueChangeEvent if fireEvents
     * passed as true and the new value is not equals to old value
     */
    public void setValue(String value, boolean fireEvents) {

        if(!Util.isDifferent(this.value, value))
            return;
        
        this.value = Value.getValue(value);
        setStylePrimaryName(this.value.getStyle());

        if (fireEvents)
            ValueChangeEvent.fire(this, this.value.getValue());
    }

    /**
     * This is a stub becuase I don't think required applies to a checkbox
     */
    public void validateValue() {

    }

    // ***** Implementation of HasValueChangeHandler ***************

    /**
     * Register ValueChangeHandler
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    // ******** Implementation of HasExceptions ***************

    /**
     * Convenience method to check if a widget has exceptions so we do not need
     * to go through the cost of merging the logical and validation exceptions
     * in the getExceptions method.
     * 
     * @return
     */
    public boolean hasExceptions() {
        return endUserExceptions != null || validateExceptions != null;
    }

    /**
     * Adds a manual Exception to the widgets exception list.
     */
    public void addException(LocalizedException error) {
        if (endUserExceptions == null)
            endUserExceptions = new ArrayList<LocalizedException>();
        endUserExceptions.add(error);
        ExceptionHelper.checkExceptionHandlers(this);
    }

    protected void addValidateException(LocalizedException error) {
        if (validateExceptions == null)
            validateExceptions = new ArrayList<LocalizedException>();
        validateExceptions.add(error);
    }

    /**
     * Combines both exceptions list into a single list to be displayed on the
     * screen.
     */
    public ArrayList<LocalizedException> getValidateExceptions() {
        return validateExceptions;
    }

    public ArrayList<LocalizedException> getEndUserExceptions() {
        return endUserExceptions;
    }

    /**
     * Clears all manual and validate exceptions from the widget.
     */
    public void clearExceptions() {
        endUserExceptions = null;
        validateExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    public void clearEndUserExceptions() {
        endUserExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    public void clearValidateExceptions() {
        validateExceptions = null;
        ExceptionHelper.checkExceptionHandlers(this);
    }

    /**
     * Will add the style to the widget.
     */
    public void addExceptionStyle(String style) {
        addStyleName(style);
    }

    /**
     * will remove the style from the widget
     */
    public void removeExceptionStyle(String style) {
        removeStyleName(style);
    }

    public void validateQuery() {
        // TODO Auto-generated method stub
        
    }

}