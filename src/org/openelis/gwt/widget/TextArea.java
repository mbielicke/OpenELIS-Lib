package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;


public class TextArea  extends Composite implements ScreenWidgetInt, Focusable, HasBlurHandlers, HasFocusHandlers, HasValueChangeHandlers<String>, HasValue<String>,  HasExceptions {

    /**
     * Wrapped GWT TextBox
     */
    protected com.google.gwt.user.client.ui.TextArea textarea;

    /**
     * Textbox attributes
     */
    protected TextAlignment                        alignment = TextAlignment.LEFT;

    /**
     * Exceptions list
     */
    protected ArrayList<LocalizedException>         endUserExceptions, validateExceptions;

    /**
     * Data moved from Field to the widget
     */
    protected boolean                                queryMode,required;
    protected String                                 value;

    /**
     * This class replaces the functionality that Field used to provide but now
     * in a static way.
     */
    protected StringHelper                       helper = new StringHelper();

    /**
     * The Constructor now sets the wrapped GWT TextBox as the element widget of
     * this composite and adds an anonymous ValueCahngeHandler to handle input
     * from the user.
     */
    public TextArea() {
        init();
    }
    
    public void init() {
        textarea = new com.google.gwt.user.client.ui.TextArea();
        textarea.addValueChangeHandler(new ValueChangeHandler<String>() {
            /*
             * This event calls validate(true) so that that the valueChangeEvent
             * for the HasValue<T> interface will be fired. In Query mode it
             * will validate the query string through the helper class
             */
            public void onValueChange(ValueChangeEvent<String> event) {
                if (queryMode) {
                    validateQuery();
                } else
                    validateValue(true);
            }

        });
        initWidget(textarea);
    }

    // ************** Methods for TextBox attributes ***********************

    /**
     * Set the text alignment.
     */
    public void setTextAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        textarea.setAlignment(alignment);
    }
    
    public int getCursorPos() {
    	return textarea.getCursorPos();
    }
    
    public void setSelectionRange(int pos, int length) {
    	textarea.setSelectionRange(pos, length);
    }
    
    // ************** Implementation of ScreenWidgetInt ********************

    /**
     * Enables or disables the textbox for editing.
     */
    public void setEnabled(boolean enabled) {
        textarea.setReadOnly( !enabled);
        /*
         * if ( !enabled) unsinkEvents(Event.KEYEVENTS); else
         * sinkEvents(Event.KEYEVENTS);
         */
    }

    /**
     * Returns whether the text is enabled for editing
     */
    public boolean isEnabled() {
        return !textarea.isReadOnly();
    }

    /**
     * This method will toggle textbox into and from query mode and suspend or
     * resume any format restrictions
     */
    public void setQueryMode(boolean query) {
        if (queryMode == query) {
            return;
        } else if (query) {
            queryMode = true;
            textarea.setAlignment(TextAlignment.LEFT);
        } else {
            queryMode = false;
            textarea.setAlignment(TextAlignment.LEFT);
        }
    }

    /**
     * Returns a single QueryData object representing the query string entered
     * by the user. The Helper class is used here to create the correct
     * QueryData object for the passed type T.
     */
    public Object getQuery() {
        return helper.getQuery(textarea.getText());
    }
    
    public void setHelper(WidgetHelper<String> helper) {
        //this.helper = helper;
    }
    
    public WidgetHelper<String> getHelper() {
        return null;
    }

    /**
     * This method is made available so the Screen can on commit make sure all
     * required fields are entered without having the user visit each widget on
     * the screen.
     */
    public void validateValue() {
        validateValue(false);
    }

    /**
     * This method will call the Helper to get the T value from the entered
     * string input. if invalid input is entered, Helper is expected to throw an
     * en exception and that exception will be added to the validate exceptions
     * list.
     * 
     * @param fireEvents
     */
    protected void validateValue(boolean fireEvents) {
        validateExceptions = null;
        try {
            setValue(helper.getValue(textarea.getText()), fireEvents);
            if(required && value == null)
                addValidateException(new LocalizedException("gen.fieldRequiredException"));
        } catch (LocalizedException e) {
            addValidateException(e);
        }
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    /**
     * Method used to validate the inputed query string by the user.
     */
    protected void validateQuery() {
        try {
            validateExceptions = null;
            helper.validateQuery(textarea.getText());
        } catch (LocalizedException e) {
            addValidateException(e);
        }
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    public void addFocusStyle(String style) {
        textarea.addStyleName(style);
    }
    
    public void removeFocusStyle(String style) {
        textarea.removeStyleName(style);
    }
    
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    // ********** Implementation of HasException interface ***************
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

    // ************** Implementation of HasValue<T> interface ***************

    /**
     * Returns the current value for this widget.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the current value of this widget without firing the
     * ValueChangeEvent.
     */
    public void setValue(String value) {
        setValue(value, false);
    }

    /**
     * Sets the current value of this widget and will fire a ValueChangeEvent if
     * the value is different than what is currently stored.
     */
    public void setValue(String value, boolean fireEvents) {

        if(!Util.isDifferent(this.value, value))
            return;
        
        this.value = value;
        if (value != null) {
            textarea.setText(helper.format(value));
        } else {
            textarea.setText("");
        }

        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }
    
    // ************* Implementation of Focusable ******************
    
    /**
     * Method only implemented to satisfy Focusable interface. 
     */
    public int getTabIndex() {
        return -1;
    }
    
    /**
     * Method only implemented to satisfy Focusable interface. 
     */
    public void setTabIndex(int index) {
        
    }

    /**
     * Method only implemented to satisfy Focusable interface. 
     */
    public void setAccessKey(char key) {
        
    }

    /**
     * This is need for Focusable interface and to allow programmatic setting
     * of focus to this widget.  We use the wrapped TextBox to make this work.
     */
    public void setFocus(boolean focused) {
        textarea.setFocus(true);
    }



    // ************ Handler Registration methods *********************

    /**
     * The Screen will add its screenHandler here to register for the
     * onValueChangeEvent
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * This Method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return addDomHandler(handler, BlurEvent.getType());
    }

    /**
     * This method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return addDomHandler(handler, FocusEvent.getType());
    }

    /**
     * Adds a mouseover handler to the textbox for displaying Exceptions
     */
    public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        return addDomHandler(handler, MouseOverEvent.getType());
    }

    /**
     * Adds a MouseOut handler for hiding exceptions display
     */
    public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        return addDomHandler(handler, MouseOutEvent.getType());
    }
}
