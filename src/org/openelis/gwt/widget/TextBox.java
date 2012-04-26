package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;

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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

/**
 * This class is used by OpenELIS Screens to display and input values in forms
 * and in table cells. TextBox is parameterized to handle different data types
 * with the help of an implementation of WidgetHelper<T>.
 * 
 * @author tschmidt
 * 
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class TextBox<T> extends Composite implements ScreenWidgetInt, 
                                                     Queryable, 
                                                     Focusable,
                                                     HasBlurHandlers, 
                                                     HasFocusHandlers,
                                                     HasValue<T>, 
                                                     HasHelper<T>,
                                                     HasExceptions {
	

    /**
     * Wrapped GWT TextBox
     */
    public TextBase                               textbox;


    /**
     * Exceptions list
     */
    protected ArrayList<LocalizedException>         endUserExceptions, validateExceptions;

    /**
     * Data moved from Field to the widget
     */
    protected int                                   maxLength;
    protected boolean                               queryMode, required,enabled;
    protected T                                     value;

    /**
     * This class replaces the functionality that Field used to provide but now
     * in a static way.
     */
    protected WidgetHelper<T>                       helper    = (WidgetHelper<T>)new StringHelper();
    
    protected TextBox<T>                            source = this;
    
    /**
     * The Constructor now sets the wrapped GWT TextBox as the element widget of
     * this composite and adds an anonymous ValueCahngeHandler to handle input
     * from the user.
     */
    public TextBox() {
        init();
    }

    public void init() {
        textbox = new TextBase();
        
        setEnabled(false);
        
        addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
		        textbox.addStyleName("Focus");
		        if(enabled)
		        	textbox.selectAll();
			}
		});
        
        addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
		    	textbox.removeStyleName("Focus");
		    	if(enabled) {
		    		if (queryMode) 
		    			validateQuery();
		    		else{
		    			finishEditing();
		    		}
		    	}
			}
		});

        initWidget(textbox);
    }
    
    public String getText() {
    	return textbox.getText();
    }
    
    public void setText(String text) {
    	textbox.setText(text);
    }
    
    /**
     * This method is overridden to make sure the Case style is applied to the widget  
     */
    @Override
    public void setStyleName(String style) {
    	textbox.setStyleName(style);
    }

    /**
     * Set the text case for input.
     */
    public void setCase(TextBase.Case textCase) {
    	textbox.setCase(textCase);
    }

    /**
     * Sets the maximum input characters allowed for this text field.
     */
    public void setMaxLength(int maxLength) {
    	this.maxLength = maxLength;
    	textbox.setMaxLength(maxLength);
    }

    /**
     * Set the text alignment.
     */
    public void setTextAlignment(TextAlignment alignment) {
        textbox.setAlignment(alignment);
    }


    /**
     * Method used to set if this widget is required to have a value inputed.
     * @param required
     */
    public void setRequired(boolean required) {
        this.required = required;
    }
    
    // ************** Implementation of ScreenWidgetInt ********************

    /**
     * Enables or disables the textbox for editing.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        textbox.enforceMask = enabled;
        textbox.setReadOnly( !enabled);
    }

    /**
     * Returns whether the text is enabled for editing
     */
    public boolean isEnabled() {
        return enabled;
    }

    // ******** Implementation of Queryable *****************
    /**
     * This method will toggle TextBox into and from query mode and suspend or
     * resume any format restrictions
     */
    public void setQueryMode(boolean query) {
        if (queryMode == query) 
            return;

        queryMode = query;
        textbox.enforceMask = !query && enabled;
        if(maxLength > 0)
        	textbox.setMaxLength(query ? 255 : maxLength);
        textbox.setText("");
    }

    /**
     * Returns a single QueryData object representing the query string entered
     * by the user. The Helper class is used here to create the correct
     * QueryData object for the passed type T.
     */
    public Object getQuery() {
    	Object query;
    	
    	query = helper.getQuery(textbox.getText());
                
        return query;
    }
    
    /**
     * Sets a query string to this widget when loaded from a table model
     */
    public void setQuery(QueryData qd) {
        if(qd != null)
            textbox.setText(qd.getQuery());
        else
            textbox.setText("");
    }
    
    /**
     * Method used to determine if widget is currently in Query mode
     */
    public boolean isQueryMode() {
    	return queryMode;
    }

    // ********** Implementation of HasHelper ***************************
    /**
     * Sets the implentation of the WidgetHelper<T> interface to be used by this
     * widget.
     */
    public void setHelper(WidgetHelper<T> helper) {
        this.helper = helper;
    }

    /**
     * Returns the helper set for this widget
     */
    public WidgetHelper<T> getHelper() {
        return helper;
    }

    // ************** Implementation of HasValue<T> interface ***************

    /**
     * Returns the current value for this widget.
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the current value of this widget without firing the
     * ValueChangeEvent.
     */
    public void setValue(T value) {
        setValue(value, false);
    }

    /**
     * Sets the current value of this widget and will fire a ValueChangeEvent if
     * the value is different than what is currently stored.
     */
    public void setValue(T value, boolean fireEvents) {
    	
        if(!Util.isDifferent(this.value, value)) {
        	if(value != null)
        		textbox.setText(helper.format(value));
            return;
        }
        
        this.value = value;
        if (value != null) {
            textbox.setText(helper.format(value));
        } else {
            textbox.setText("");
        }
        
        if (fireEvents) 
            ValueChangeEvent.fire(this, value);
    }
    
    /**
     * This method takes raw input from widget and will call helper to get the 
     * appropriate value to set and also set any validation exceptions if present
     */
    public void finishEditing() {
    	String text;
    	
    	text = textbox.getText();
    		
    	validateExceptions = null;
        
    	try {
            setValue(helper.getValue(text), true);
            if (required && getValue() == null) 
                throw new LocalizedException("exc.fieldRequiredException");
        } catch (LocalizedException e) {
            addValidateException(e);
        }
        ExceptionHelper.checkExceptionHandlers(this);
    }

    /**
     * Method used to validate the inputed query string by the user.
     */
    public void validateQuery() {
        try {
            validateExceptions = null;
            helper.validateQuery(textbox.getText());
        } catch (LocalizedException e) {
            addValidateException(e);
        }
        ExceptionHelper.checkExceptionHandlers(this);
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
    	if(validateExceptions != null)
    		return true;
    	  
    	if (!queryMode && required && getValue() == null) {
            addValidateException(new LocalizedException("exc.fieldRequiredException"));
            ExceptionHelper.checkExceptionHandlers(this);
    	}
    	  
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
     * Exposing this method on the wrapped widget
     */
    public void selectAll() {
    	textbox.selectAll();
    }
    
    /**
     * Exposing this method on the wrapped widget
     */
    public void setSelectionRange(int pos, int length) {
    	textbox.setSelectionRange(pos, length);
    }
    
    /**
     * Exposing this method on the wrapped widget
     */
    public void unselectAll() {
    	textbox.setSelectionRange(0, 0);
    }
    
    public void setMask(String mask) {
    	textbox.setMask(mask);
    }

    /**
     * This is need for Focusable interface and to allow programmatic setting of
     * focus to this widget. We use the wrapped TextBox to make this work.
     */
    public void setFocus(boolean focused) {
       textbox.setFocus(true);
    }

    // ************ Handler Registration methods *********************

    /**
     * The Screen will add its screenHandler here to register for the
     * onValueChangeEvent
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
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
