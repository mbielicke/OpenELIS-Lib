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
package org.openelis.gwt.widget.calendar;

import java.util.ArrayList;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.Exceptions;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.constants.Constants;
import org.openelis.gwt.resources.CalendarCSS;
import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.DateHelper;
import org.openelis.gwt.widget.ExceptionHelper;
import org.openelis.gwt.widget.HasExceptions;
import org.openelis.gwt.widget.HasHelper;
import org.openelis.gwt.widget.Queryable;
import org.openelis.gwt.widget.ScreenWidgetInt;
import org.openelis.gwt.widget.TextBase;
import org.openelis.gwt.widget.WidgetHelper;

import com.google.gwt.core.client.Scheduler;
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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

/**
 * This class extends the TextBox<Datetime> and adds a button for using the
 * CalendarWidget to pick Dates.
 * 
 */
public class Calendar extends Composite implements ScreenWidgetInt,
												   Queryable,
												   Focusable,
												   HasBlurHandlers,
												   HasFocusHandlers,
												   HasValue<Datetime>,
												   HasHelper<Datetime>,
												   HasExceptions {
												  
    /**
     * Used for Calendar display
     */
    protected Grid                                  display;
    protected Button                                button;
    protected PopupPanel                            popup;
    protected CalendarWidget                        calendar;
    protected MonthYearWidget                       monthYearWidget;
    protected int                                   width;
    protected boolean                               showingCalendar,queryMode,required;

    protected TextBase                              textbox;
    
    protected Datetime                              value;
    
    protected WidgetHelper<Datetime>                helper = new DateHelper();
    
    final Calendar                                  source;
    
    /**
     * Exceptions list
     */
    protected Exceptions                            exceptions;
    
    protected CalendarCSS                           css;
    
    /**
     * Default no-arg constructor
     */
    public Calendar() {
    	init();
    	source = this;
    }

    /**
     * This method will set the display of the Calendar and set up Event
     * Handlers
     */
    public void init() {
    	css = OpenELISResources.INSTANCE.calendar();
    	css.ensureInjected();
    	
        /*
         * Final instance of the private class KeyboardHandler
         */
        final KeyboardHandler keyHandler = new KeyboardHandler();

        display  = new Grid(1,2);
        display.setCellSpacing(0);
        display.setCellPadding(0);
        
        textbox = new TextBase();

        button = new Button();
        AbsolutePanel image = new AbsolutePanel();
        image.setStyleName(css.CalendarButton());
        button.setWidget(image);

        display.setWidget(0,0,textbox);
        display.setWidget(0,1,button);
        display.getCellFormatter().setWidth(0, 1, "14px");
        
        initWidget(display);

        display.setStyleName(css.SelectBox());
        textbox.setStyleName(css.Calendar());

        
        /*
         * Set the focus style when the Focus event is fired Externally
         */
        addFocusHandler(new FocusHandler() {
        	public void onFocus(FocusEvent event) {
        		if(isEnabled())
        			display.addStyleName(css.Focus());
        	}
        });

        /*
         * Removes the focus style when the Blue event is fires externally
         */
        addBlurHandler(new BlurHandler() {
        	public void onBlur(BlurEvent event) {
        		display.removeStyleName(css.Focus());
        		finishEditing(true);
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
            	if(!showingCalendar && isEnabled())
            		BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
            }
        });

        /*
         * Register click handler to button to show the popup table
         */
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                showPopup();
            }
        });
        
        button.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				showingCalendar = true;
			}
		});
        
        exceptions = new Exceptions();

        /*
         * Registers the keyboard handling this widget
         */
        addHandler(keyHandler, KeyDownEvent.getType());
        
    }
    
    /**
     * This method is overwritten to implement case management. Use the
     * setValue/getValue methods for normal screen use.
     */
    public String getText() {
        return textbox.getText();
    }
    
    public void setText(String text) {
        textbox.setText(text);
    }

    /**
     * This method will initialize and show the popup panel for this widget.
     */
    private void showPopup() {
    	showingCalendar = true;
        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setStyleName(css.Popup());
            popup.setPreviewingAllNativeEvents(false);
            popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                public void onClose(CloseEvent<PopupPanel> event) {
                	showingCalendar = false;
                	if(event.isAutoClosed())
                		display.removeStyleName(css.Focus());
                }
            });
        }
        try {
            if (calendar == null) {
                /*
                 * Set new CalendarWidget withe the precision used by this widget
                 */
                calendar = new CalendarWidget( ((DateHelper)helper).getBegin(),
                                              ((DateHelper)helper).getEnd());
                /*
                 * CalendarWidget will fire a ValueChangeEvent<Datetime> when the user selects
                 * a date.
                 */
                calendar.addValueChangeHandler(new ValueChangeHandler<Datetime>() {
                    public void onValueChange(ValueChangeEvent<Datetime> event) {
                        popup.hide();
                        textbox.setText(helper.format(event.getValue()));
                        textbox.setFocus(true);
                    }
                });
                /*
                 * Add a handler to the CalendarWidget for when the user selects the MonthSelect button.
                 * We will then switch the popup view to the MonthYearWidget and setting it to the current 
                 * month year displayed in the calendar widget. 
                 */
                calendar.addMonthSelectHandler(new ClickHandler() {
                    public void onClick(ClickEvent event) {
                        if (monthYearWidget == null) { 
                            monthYearWidget = new MonthYearWidget();
                            /*
                             * Set popup back to calendar with the selected month and year
                             */
                            monthYearWidget.addOKHandler(new ClickHandler() {
                                public void onClick(ClickEvent event) {
                                    calendar.drawMonth(monthYearWidget.getYear(),
                                                       monthYearWidget.getMonth());
                                    popup.setWidget(calendar);
                                }
                            });
                            /*
                             * Set popup back to calendar with month and year it has set
                             */
                            monthYearWidget.addCancelHandler(new ClickHandler() {
                                public void onClick(ClickEvent event) {
                                    popup.setWidget(calendar);
                                }
                            });
                        }
                        monthYearWidget.setYear(calendar.getYear());
                        monthYearWidget.setMonth(calendar.getMonth());
                        popup.setWidget(monthYearWidget);
                }
                });
            }
            /*
             * Sets the calendar to the current month and date entered in the widget.  If null 
             * is passed then the current date from the server will be displayed and selected.
             */
            calendar.setDate(helper.getValue(textbox.getText()));
            popup.setWidget(calendar);

        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.showRelativeTo(this);

        /*
         * SetFocus to the popup so the calendar will take over the key events
         */
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
				 ((FocusPanel)calendar.def.getWidget("CalFocus")).setFocus(true);
			}
		});
    }
    
    @Override
    public void setWidth(String w) {
        width = Util.stripUnits(w);
        /*
         * Set the outer panel to full width;
         */
        if (display != null)
            display.setWidth(width+"px");

        /*
         * set the Textbox to width - 14 to account for button.
         */
        
        textbox.setWidth((width - 14) + "px");
        
    }
    
    public int getWidth() {
        return width;
    }
    
    @Override
    public void setHeight(String height) {
        textbox.setHeight(height);
        button.setHeight(height);
    }

    /**
     * This private class will handle key events for this widget
     * 
     * @author tschmidt
     * 
     */
    private class KeyboardHandler implements KeyDownHandler {

        public void onKeyDown(KeyDownEvent event) {
            switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_ENTER:
                    showPopup();
            }
        }
    }

    /**
     * Overridden method from TextBox for enabling and disabling the widget
     */
    @Override
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
        textbox.enforceMask(enabled);
        textbox.setReadOnly(!enabled);
        if (enabled)
            sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        else
            unsinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
    }

    /**
     * Overridden method from TextBox for setting the Exception style.
     */
    @Override
    public void addExceptionStyle() {
    	if(ExceptionHelper.isWarning(this))
    		addStyleName(css.InputWarning());
    	else
    		addStyleName(css.InputError());
    }

    /**
     * Overridden method from TextBox for removing the Exception style.
     */
    @Override
    public void removeExceptionStyle() {
        removeStyleName(css.InputError());
        removeStyleName(css.InputWarning());
    }
    
    @Override
    public void setHelper(WidgetHelper<Datetime> helper) {
    	DateHelper dh;
    	
    	this.helper = helper;
    	
    	setDefaultMask();
    }
    
    public void setPrecision(byte begin, byte end) {
    	assert(begin < end);
    	
    	((DateHelper)getHelper()).setBegin(begin);
    	((DateHelper)getHelper()).setEnd(end);
    	
    	calendar = null;
    	
    	setDefaultMask();
    }
    
    private void setDefaultMask() {
    	DateHelper dh;
    	
    	dh = (DateHelper)getHelper();
    	/*
    	 * Setting default mask based on precision of helper
    	 * internationalized mask pictures should be set from 
    	 * xsl, but defaults are provided if none set.
    	 */
    	if(dh.getBegin() > Datetime.DAY) {
    		textbox.setMask(Constants.get().timeMask());
    	} else if (dh.getEnd() < Datetime.HOUR){
    		textbox.setMask(Constants.get().dateMask());
    	} else {
    		textbox.setMask(Constants.get().dateTimeMask());
    	}
    }

    /**
     * Returns the current value for this widget.
     */
    public Datetime getValue() {
        return value;
    }

    /**
     * Sets the current value of this widget without firing the
     * ValueChangeEvent.
     */
    public void setValue(Datetime value) {
        setValue(value, false);
    }

    /**
     * Sets the current value of this widget and will fire a ValueChangeEvent if
     * the value is different than what is currently stored.
     */
    public void setValue(Datetime value, boolean fireEvents) {
    	
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
     * This method is made available so the Screen can on commit make sure all
     * required fields are entered without having the user visit each widget on
     * the screen.
     */
    public void finishEditing() {
        finishEditing(false);
    }

    /**
     * This method will call the Helper to get the T value from the entered
     * string input. if invalid input is entered, Helper is expected to throw an
     * en exception and that exception will be added to the validate exceptions
     * list.
     * 
     * @param fireEvents
     */
    protected void finishEditing(boolean fireEvents) {
    	String text;
    	
    	if(isEnabled()) {
    		if(queryMode) {
    			validateQuery();
    		}else {
    			
    			text = textbox.getText();
    		
    			clearValidateExceptions();
        
    			try {
    				setValue(helper.getValue(text), fireEvents);
    				if (required && value == null) 
    					addValidateException(new LocalizedException(Constants.get().fieldRequired()));
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
            getValidateExceptions();
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
		if (getValidateExceptions() != null)
			return true;

		if (!queryMode && required && getValue() == null) {
			addValidateException(new LocalizedException(Constants.get().fieldRequired()));
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
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Datetime> handler) {
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

	@Override
	public WidgetHelper<Datetime> getHelper() {
		return helper;
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
        textbox.enforceMask(!query && isEnabled());
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

	@Override
	public boolean isEnabled() {
		return !textbox.isReadOnly();
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

}
