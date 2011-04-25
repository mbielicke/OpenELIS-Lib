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

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.Util;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.DateHelper;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * This class extends the TextBox<Datetime> and adds a button for using the
 * CalendarWidget to pick Dates.
 * 
 */
public class Calendar extends TextBox<Datetime> {

    /**
     * Used for Calendar display
     */
    protected HorizontalPanel hp;
    protected Button          button;
    protected PopupPanel      popup;
    protected CalendarWidget  calendar;
    protected MonthYearWidget monthYearWidget;
    protected int             width;

    /**
     * Default no-arg constructor
     */
    public Calendar() {

    }

    /**
     * This method will set the display of the Calendar and set up Event
     * Handlers
     */
    @Override
    public void init() {
        /*
         * Final instance of this class used Anonymous handlers
         */
        final Calendar source = this;

        /*
         * Final instance of the private class KeyboardHandler
         */
        final KeyboardHandler keyHandler = new KeyboardHandler();

        hp = new HorizontalPanel();
        hp.setSpacing(0);
        textbox = new com.google.gwt.user.client.ui.TextBox();

        button = new Button();
        AbsolutePanel image = new AbsolutePanel();
        image.setStyleName("CalendarButton");
        button.setDisplay(image, false);

        hp.add(textbox);
        hp.add(button);

        initWidget(hp);

        hp.setStyleName("Calendar");
        textbox.setStyleName("TextboxUnselected");

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
                BlurEvent.fireNativeEvent(event.getNativeEvent(), source);
                validateValue(true);
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

        /*
         * Registers the keyboard handling this widget
         */
        addHandler(keyHandler, KeyDownEvent.getType());

    }

    /**
     * This method will initialize and show the popup panel for this widget.
     */
    private void showPopup() {

        if (popup == null) {
            popup = new PopupPanel(true);
            popup.setStyleName("DropdownPopup");
            popup.setPreviewingAllNativeEvents(false);
            popup.addCloseHandler(new CloseHandler<PopupPanel>() {
                public void onClose(CloseEvent<PopupPanel> event) {

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
            calendar.setDate(helper.getValue(getText()));
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
				 ((FocusPanel)calendar.getDefinition().getWidget("CalFocus")).setFocus(true);
			}
		});
    }
    
    @Override
    public void setWidth(String w) {
        width = Util.stripUnits(w);
        /*
         * Set the outer panel to full width;
         */
        if (hp != null)
            hp.setWidth(width+"px");

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
        if (enabled)
            sinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        else
            unsinkEvents(Event.ONKEYDOWN | Event.ONKEYUP);
        super.setEnabled(enabled);
    }

    /**
     * Overridden method from TextBox for setting the Exception style.
     */
    @Override
    public void addExceptionStyle(String style) {
        textbox.addStyleName(style);
    }

    /**
     * Overridden method from TextBox for removing the Exception style.
     */
    @Override
    public void removeExceptionStyle(String style) {
        textbox.removeStyleName(style);
    }

}
