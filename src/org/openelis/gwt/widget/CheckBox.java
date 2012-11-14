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

import org.openelis.gwt.common.Exceptions;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.resources.CheckboxCSS;

import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasValue;

/**
 * This class is used to display and edit a CheckBox in Screen forms and
 * TableCells
 * 
 * @author tschmidt
 * 
 */
public class CheckBox extends FocusPanel implements ScreenWidgetInt, 
													Queryable, 
													HasBlurHandlers,
													HasFocusHandlers, 
													HasValueChangeHandlers<String>,
													HasValue<String>, 
													HasExceptions {

    /*
     * Fields for query mode
     */
    protected boolean                        queryMode;

    /*
     * Fields used for Exceptions
     */
    protected Exceptions                     exceptions;
    
    protected Check                          check;
     
    protected CheckBox                       source = this;
    

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
    public CheckBox(Check.Mode mode) {
    	init();
        check.setMode(mode);
    }

    protected void init() {
    	check = new Check();
    	
    	check.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
    		public void onValueChange(ValueChangeEvent<Boolean> event) {
    			fireValueChange(event.getValue() != null ? ("Y".equals(event.getValue()) ? "Y" : "N") : null);
    		}
		});
        
    	setWidget(check);
        
        exceptions = new Exceptions();
        
    }

    /**
     * Returns what mode the Checkbox is currently in
     * 
     * @return
     */
    public Check.Mode getMode() {
        return check.getMode();
    }



    // ******** Implementation of ScreenWidgetInt ***********************
    /**
     * Method to enable/disable the checkbox
     */
    public void setEnabled(boolean enabled) {
    	check.setEnabled(enabled);
    }

    /**
     * Method to determine if the checkbox is enabled
     */
    public boolean isEnabled() {
        return check.isEnabled();
    }

    // ********* Implementation of Queryable *****************

    /**
     * Puts Checkbox into and out of Query Mode
     */
    public void setQueryMode(boolean query) {
        if(queryMode == query)
        	return;
        
        queryMode = query;
        
        if(query)
        	check.setMode(Check.Mode.THREE_STATE);
        else
        	check.setMode(Check.Mode.TWO_STATE);
        	
    }

    /**
     * Returns a QueryData object of type string only if checkbox is set to "Y"
     * or "N"
     */
    public Object getQuery() {
        if(!queryMode)
        	return null;

        if (check.isUnknown())
            return null;

        return new QueryData(QueryData.Type.STRING,check.isChecked() ? "Y" : "N");
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
    
    /**
     * Method used to determine if widget is currently in Query mode
     */
    public boolean isQueryMode() {
    	return queryMode;
    }

    // ******* Implementation of HasValue<String> ******

    /**
     * Returns the current value for this widget
     */
    public String getValue() {
        if(check.isChecked())
        	return "Y";
        if(check.isUnchecked())
        	return "N";
        
        return null;
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

        if(!Util.isDifferent(value, getValue()))
            return;
        
        if(value == null) {
        	if(check.getMode() == Check.Mode.TWO_STATE)
               	check.uncheck();
        	else
        		check.unkown();
        }else if("Y".equals(value))
        	check.check();
        else
        	check.uncheck();

        if (fireEvents)
            ValueChangeEvent.fire(this, value);
    }

    /**
     * This is a stub becuase I don't think required applies to a checkbox
     */
    public void validateValue() {

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
		return exceptions.hasExceptions();
	}

	/**
	 * Adds a manual Exception to the widgets exception list.
	 */
	public void addException(LocalizedException error) {
		exceptions.addException(error);
		ExceptionHelper.checkExceptionHandlers(this);
	}

	protected void addValidateException(LocalizedException error) {
		exceptions.addValidateException(error);

	}

	/**
	 * Combines both exceptions list into a single list to be displayed on the
	 * screen.
	 */
	public ArrayList<LocalizedException> getValidateExceptions() {
		return exceptions.getValidateExceptions();
	}

	public ArrayList<LocalizedException> getEndUserExceptions() {
		return exceptions.getEndUserExceptions();
	}

	/**
	 * Clears all manual and validate exceptions from the widget.
	 */
	public void clearExceptions() {
		exceptions.clearExceptions();
		removeExceptionStyle();
		ExceptionHelper.clearExceptionHandlers(this);
	}

	public void clearEndUserExceptions() {
		exceptions.clearEndUserExceptions();
		ExceptionHelper.checkExceptionHandlers(this);
	}

	public void clearValidateExceptions() {
		exceptions.clearValidateExceptions();
		ExceptionHelper.checkExceptionHandlers(this);
	}
    /**
     * Will add the style to the widget.
     */
    public void addExceptionStyle() {
    	if(ExceptionHelper.isWarning(this))
    		addStyleName(check.css.InputWarning());
    	else
    		addStyleName(check.css.InputError());
    		
    }

    /**
     * will remove the style from the widget
     */
    public void removeExceptionStyle() {
        removeStyleName(check.css.InputError());
        removeStyleName(check.css.InputWarning());
    }

    public void validateQuery() {
        // TODO Auto-generated method stub
        
    }

	@Override
	public void finishEditing() {
		// TODO Auto-generated method stub
		
	}
	
	private void fireValueChange(String value) {
		ValueChangeEvent.fire(this, value);
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return check.addMouseOverHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return check.addMouseOutHandler(handler);
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return check.addFocusHandler(handler);
	}

	@Override
	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return check.addBlurHandler(handler);
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}
	
	public void setCSS(CheckboxCSS css) {
		check.setCSS(css);
	}

}