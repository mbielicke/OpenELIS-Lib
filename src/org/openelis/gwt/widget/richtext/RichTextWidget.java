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
package org.openelis.gwt.widget.richtext;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.widget.ExceptionHelper;
import org.openelis.gwt.widget.HasExceptions;
import org.openelis.gwt.widget.ScreenWidgetInt;
import org.openelis.gwt.widget.WidgetHelper;

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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class RichTextWidget extends Composite implements ScreenWidgetInt, Focusable, HasBlurHandlers, HasFocusHandlers, HasValueChangeHandlers<String>, HasValue<String>,  HasExceptions {

	private VerticalPanel vp = new VerticalPanel();
	public RichTextArea area;
	public RichTextToolbar toolbar;
	private boolean tools, required;
	private HandlerRegistration focReg;
	
	protected String value;
	
    /**
     * Exceptions list
     */
    protected ArrayList<LocalizedException>         endUserExceptions, validateExceptions;


	public RichTextWidget() {
		area = new RichTextArea();
		toolbar = new RichTextToolbar(area);
	}

	public RichTextWidget(boolean tools) {
		this();
		init(tools);
	}

	public void init(boolean tools){
		this.tools = tools;
		initWidget(vp);
		DOM.setStyleAttribute(vp.getElement(),"background","white");
		vp.setSpacing(0);

		if(tools){
			vp.add(toolbar);
			vp.add(area);
			vp.setCellWidth(toolbar, "100%");
		}else{
			vp.add(area);
		}
		area.setSize("100%","100%");
        area.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                if(!area.isEnabled())
                    area.setFocus(false);
            }
        });
		//Font and Font size can not be set until the area recieves focus.  We set up this handler to 
		//set the font and size that we want to default then remove the handler so we don't repeat it.
		focReg = area.addFocusHandler(new FocusHandler() {
			public void onFocus(FocusEvent event) {
				area.getFormatter().setFontName("Verdana");
				area.getFormatter().setFontSize(RichTextArea.FontSize.X_SMALL);
				focReg.removeHandler();
			}
		});
	}

	public void setText(String text){
		area.setHTML(text);
	}

	public String getText(){
		return area.getHTML();
	}

	public String getClearText() {
		return area.getText();
	}

	public void setWidth(String width){
		vp.setWidth(width);
	}

	public void setHeight(String height){
		vp.setHeight(height);
	}

	public void enable(boolean enabled) {
		if(tools) {
			toolbar.enable(enabled);
		}
		area.setEnabled(enabled);
	}

	public void onFocus(FocusEvent event) {
		if(!area.isEnabled())
			area.setFocus(false);
	}

	public void onLostFocus(Widget sender) {
		// TODO Auto-generated method stub

	}

	public String getValue() {
		return area.getHTML();
	}

	public void setValue(String value) {
		setValue(value,false);
	}

	public void setValue(String value, boolean fireEvents) {
		String old = area.getHTML();
		area.setHTML(value);
		if(fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, old, value);
	}

    // ************** Implementation of ScreenWidgetInt ********************

    /**
     * Enables or disables the textbox for editing.
     */
    public void setEnabled(boolean enabled) {
        area.setEnabled(enabled);
        /*
         * if ( !enabled) unsinkEvents(Event.KEYEVENTS); else
         * sinkEvents(Event.KEYEVENTS);
         */
    }

    /**
     * Returns whether the text is enabled for editing
     */
    public boolean isEnabled() {
        return area.isEnabled();
    }

    /**
     * This method will toggle textbox into and from query mode and suspend or
     * resume any format restrictions
     */
    public void setQueryMode(boolean query) {

    }

    /**
     * Returns a single QueryData object representing the query string entered
     * by the user. The Helper class is used here to create the correct
     * QueryData object for the passed type T.
     */
    public Object getQuery() {
        return null;
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
        setValue(area.getHTML(), fireEvents);
        if(required && value == null)
            addValidateException(new LocalizedException("gen.fieldRequiredException"));
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    /**
     * Method used to validate the inputed query string by the user.
     */
    protected void validateQuery() {
    }
    
    public void addFocusStyle(String style) {
        area.addStyleName(style);
    }
    
    public void removeFocusStyle(String style) {
        area.removeStyleName(style);
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
        area.setFocus(true);
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
