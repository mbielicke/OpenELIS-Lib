package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

public class Label<T> extends com.google.gwt.user.client.ui.Label implements HasValue<T>, HasExceptions, HasHelper<T> {
    
    T value;
    WidgetHelper<T> helper                         =(WidgetHelper<T>)new StringHelper();
    
    /**
     * Exceptions list
     */
    protected ArrayList<LocalizedException>         endUserExceptions, validateExceptions;
    
    public Label() {
    	super();
    }
    
    public Label(T value) {
    	super();
    	setValue(value);
    }

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		setValue(value,false);
	}
	

	public void setValue(T value, boolean fireEvents) {
		this.value = value;
		setText(helper.format(value));
	}

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        // TODO Auto-generated method stub
        return null;
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
        ExceptionHelper.getInstance().checkExceptionHandlers(this);
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
        ExceptionHelper.getInstance().checkExceptionHandlers(this);
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
    
    public void setHelper(WidgetHelper<T> helper) {
        this.helper = helper;
    }
    
    public WidgetHelper<T> getHelper() {
        return helper;
    }



}
