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

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class EditBox extends Composite implements ScreenWidgetInt,
                                                  Queryable,
                                                  Focusable,
                                                  HasBlurHandlers,
                                                  HasFocusHandlers,
                                                  HasValue<String>,
                                                  HasHelper<String>,
                                                  HasExceptions {

    /**
     * Used for EditBox display
     */
    protected Grid                                  display;
    protected Button                                button;
    protected PopupPanel                            popup;
    protected TextArea                              ta;
    protected boolean                               required,showing,queryMode;
    protected int                                   maxLength;
    
    protected TextBase                              textbox;
    
    protected String                                value;
    
    protected EditBox                               source = this;
    
    protected WidgetHelper<String>                  helper = new StringHelper();
    
    /**
     * Exceptions list
     */
    protected Exceptions                           exceptions;
    
    
    /**
     * Default no-arg constructor
     */
    public EditBox() {
    	init();
    }
    
    public void init() {
    	
    	display = new Grid(1,2);
    	display.setCellSpacing(0);
    	display.setCellPadding(0);
    	
    	textbox = new TextBase();
    	
    	button = new Button(null,"...");
    	
    	display.setWidget(0, 0, textbox);
    	display.setWidget(0, 1, button);
    	
    	initWidget(display);
    	
    	display.setStyleName("SelectBox");
    	textbox.setStyleName("TextboxUnselected");
    	
        /*
         * Set the focus style when the Focus event is fired Externally
         */
        addFocusHandler(new FocusHandler() {
        	public void onFocus(FocusEvent event) {
        		if(isEnabled()) {
        			display.addStyleName("Focus");
        			selectAll();
        		}
        	}
        });

        /*
         * Removes the focus style when the Blue event is fires externally
         */
        addBlurHandler(new BlurHandler() {
        	public void onBlur(BlurEvent event) {
        		display.removeStyleName("Focus");
        		unselectAll();
        		finishEditing();
        	}
        });
        
        /*
         * Since HorizontalPanel is not a Focusable widget we need to listen to
         * the textbox focus and blur events and pass them through to the
         * handlers registered to source.
         */
        textbox.addFocusHandler(new FocusHandler() {
            public void onFocus(FocusEvent event) {
                FocusEvent.fireNativeEvent(event.getNativeEvent(), source);
            }
        });

        textbox.addBlurHandler(new BlurHandler() {
            public void onBlur(BlurEvent event) {

            	if(!showing && isEnabled()) 
            		BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
            	
            }
        });
    	
    	button.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				showPopup();
			}
		});
    	
    	button.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				showing = true;
			}
		});
    	
    	exceptions = new Exceptions();
    }

    private void showPopup() {
    	showing = true;
        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setPreviewingAllNativeEvents(false);
            popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                public void onClose(CloseEvent<PopupPanel> event) {
                    setValue(ta.getText(), true);
                    showing = false;
                }
            });

            ta = new TextArea();
            ta.setWidth("300px");
            ta.setStyleName("ScreenTextArea");
            
            ta.addKeyDownHandler(new KeyDownHandler() {
				@Override
				public void onKeyDown(KeyDownEvent event) {
					switch(event.getNativeKeyCode()) {
						case KeyCodes.KEY_TAB :
							popup.hide();
							setFocus(true);
							KeyDownEvent.fireNativeEvent(event.getNativeEvent(), source);
							break;
						case KeyCodes.KEY_ENTER :
							popup.hide();
							setFocus(true);
							break;
					}
				}
            });
            
            
            popup.setWidget(ta);
            popup.setStyleName("DropdownPopup");

        }
        ta.setText(textbox.getText());
        popup.showRelativeTo(this);
        ta.setFocus(true);
        ta.selectAll();
    }
    

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		setValue(value,false);
		
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
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
     * This method will call the Helper to get the T value from the entered
     * string input. if invalid input is entered, Helper is expected to throw an
     * en exception and that exception will be added to the validate exceptions
     * list.
     * 
     * @param fireEvents
     */
    public void finishEditing() {
    	String text;
    	
    	if(isEnabled()) {
    		if(queryMode)
    			validateQuery();
    		else {
    			
    			text = textbox.getText();
    	
    			if(textbox.enforceMask && textbox.picture.equals(text)) {
    				text = "";
    				textbox.setText("");
    			}
    		
    			clearValidateExceptions();
        
    			try {
    				setValue(helper.getValue(text), true);
    				if (required && value == null) 
    					addValidateException(new LocalizedException("exc.fieldRequiredException"));
    			} catch (LocalizedException e) {
    				addValidateException(e);
    			}
    			ExceptionHelper.checkExceptionHandlers(this);
    		}
    	}
    }

    /**
     * Method used to validate the inputed query string by the user.
     */
    public void validateQuery() {
        try {
            clearValidateExceptions();
            helper.validateQuery(textbox.getText());
        } catch (LocalizedException e) {
            addValidateException(e);
        }
        ExceptionHelper.checkExceptionHandlers(this);
    }
    
    // ******** Implementation of Queryable *****************
    /**
     * This method will toggle TextBox into and from query mode and suspend or
     * resume any format restrictions
     */
    public void setQueryMode(boolean query) {
        if (queryMode == query) {
            return;
        } else if (query) {
            queryMode = true;
            textbox.enforceMask = false;
            textbox.setMaxLength(255);
            textbox.setAlignment(TextAlignment.LEFT);
        } else {
            queryMode = false;
            textbox.enforceMask = true;
            textbox.setMaxLength(maxLength);
            textbox.setAlignment(TextAlignment.LEFT);
            textbox.setText("");
        }
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
    
    // ********** Implementation of HasException interface ***************
    /**
     * Convenience method to check if a widget has exceptions so we do not need
     * to go through the cost of merging the logical and validation exceptions
     * in the getExceptions method.
     * 
     * @return
     */
    public boolean hasExceptions() {
    	if(getValidateExceptions() != null)
    		return true;
    	  
    	if (required && getValue() == null) {
            addValidateException(new LocalizedException("exc.fieldRequiredException"));
            ExceptionHelper.checkExceptionHandlers(this);
    	}
    	  
    	return getEndUserExceptions() != null || getValidateExceptions() != null;
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
    		addStyleName("InputWarning");
    	else
    		addStyleName("InputError");
    }

    /**
     * will remove the style from the widget
     */
    public void removeExceptionStyle() {
        removeStyleName("InputError");
        removeStyleName("InputWarning");
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * This Method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addBlurHandler(BlurHandler handler) {
        return addHandler(handler, BlurEvent.getType());
    }

    /**
     * This method is here so the Focus logic of ScreenPanel can be notified
     */
    public HandlerRegistration addFocusHandler(FocusHandler handler) {
        return addHandler(handler, FocusEvent.getType());
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
    
    /**
     * Overridden method from TextBox for enabling and disabling the widget
     */
    @Override
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
        textbox.setReadOnly(!enabled);
        if (enabled)
            sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        else
            unsinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
    }


	@Override
	public boolean isEnabled() {
		return !textbox.isReadOnly();
	}

	@Override
	public void setHelper(WidgetHelper<String> helper) {
		this.helper = helper;
	}

	@Override
	public WidgetHelper<String> getHelper() {
		return helper;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public void setMaxLength(int max) {
		this.maxLength = max;
		textbox.setMaxLength(max);
	}
	
	public void setCase(TextBase.Case textCase) {
		textbox.setCase(textCase);
	}
}
