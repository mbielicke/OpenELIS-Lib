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
package org.openelis.gwt.widget.calendar;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.screen.Calendar;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.screen.ScreenEventHandler;
import org.openelis.gwt.services.ScreenService;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Calendar Widget will display a calendar and text boxes with the current time
 * in a popup panel. A user can then select a date and time that will be
 * returned back to a form.
 * 
 * @author tschmidt
 * 
 */
public class CalendarWidget extends Screen implements HasValueChangeHandlers<Datetime> {

    /*
     * Currently selected date by the widget
     */
    private Datetime            selected;
    /*
     * Todays date retrieved from server.
     */
    private Datetime            current;
    
    /*
     * Currently displayed year and month in the calendar
     */
    private int                 year;
    private int                 month;

    /*
     * Buttons from xsl
     */
    protected Button            prevMonth, nextMonth, monthSelect, today;

    protected TextBox<Datetime> time;
    protected Label             monthDisplay;

    protected CalendarTable     table;

    /*
     * Constructor that takes the precision of the date to be used.
     */
    public CalendarWidget(byte begin, byte end) throws Exception {
        super((ScreenDefInt)GWT.create(CalendarDef.class));

        service = new ScreenService("controller?service=org.openelis.gwt.server.CalendarService");

        current = Calendar.getCurrentDatetime(begin, end);

        year = current.get(Datetime.YEAR);
        month = current.get(Datetime.MONTH);

        initialize();
    }

    /*
     * Initialize widgets on Screen.
     */
    @SuppressWarnings("unchecked")
	private void initialize() throws Exception {

        /*
         * Final reference to this class to be used by anon handlers
         */
        final CalendarWidget source = this;

        /*
         * Create a CalendarTable and set in the screen
         */
        table = new CalendarTable();
        ((VerticalPanel)def.getWidget("calContainer")).add(table);

        addScreenHandler(table, new ScreenEventHandler<Object>() {
            public void onDataChange(DataChangeEvent event) {
                selected = selected == null ? current : selected; 
                table.setCalendar(year, month, selected, current);
            }
        });

        table.addSelectionHandler(new SelectionHandler<Datetime>() {
            public void onSelection(SelectionEvent<Datetime> event) {
                ValueChangeEvent.fire(source, event.getSelectedItem());
            }
        });

        monthDisplay = (Label)def.getWidget("MonthDisplay");
        addScreenHandler(monthDisplay, new ScreenEventHandler<String>() {
            public void onDataChange(DataChangeEvent event) {
                monthDisplay.setText( ((Label)def.getWidget("month" + month)).getText() + " " +
                                     (year + 1900));
            }
        });

        monthSelect = (Button)def.getWidget("monthSelect");
        monthSelect.setEnabled(true);

        prevMonth = (Button)def.getWidget("prevMonth");
        prevMonth.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                CalendarImpl cal = CalendarImpl.getInstance();
                cal.set(year, month, 1, 0, 0, 0);
                cal.add(CalendarImpl.MONTH, -1);
                month = cal.get(CalendarImpl.MONTH);
                DataChangeEvent.fire(source);
            }
        });
        prevMonth.setEnabled(true);

        nextMonth = (Button)def.getWidget("nextMonth");
        nextMonth.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                CalendarImpl cal = CalendarImpl.getInstance();
                cal.set(year, month, 1, 0, 0, 0);
                cal.add(CalendarImpl.MONTH, 1);
                month = cal.get(CalendarImpl.MONTH);
                DataChangeEvent.fire(source);
            }
        });
        nextMonth.setEnabled(true);

        today = (Button)def.getWidget("today");
        today.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    ValueChangeEvent.fire(source, service.callDatetime("getCurrentDatetime",
                                                                       selected.startCode,
                                                                       selected.endCode));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });

        /*
         * Show or hide time component based on the widget precision.
         */
        if (current.endCode > Datetime.DAY) {
            time = (TextBox<Datetime>)def.getWidget("time");
            addScreenHandler(time, new ScreenEventHandler<Datetime>() {
                public void onDataChange(DataChangeEvent event) {
                    time.setValue(selected);
                }
            });
            def.getWidget("TimeBar").setVisible(true);
        } else
            def.getWidget("TimeBar").setVisible(false);
        
        /*
         * KeyHandler to let the user arrow around the calendar
         */
        addDomHandler(new KeyDownHandler() {
            public void onKeyDown(KeyDownEvent event) {

                int row = table.getSelectedRow(), col = table.getSelectedCol();

                switch (event.getNativeKeyCode()) {
                    case KeyCodes.KEY_DOWN:
                        row = row + 1 > 6 ? 1 : row + 1;
                        break;
                    case KeyCodes.KEY_UP:
                        row = row - 1 < 1 ? 6 : row - 1;
                        break;
                    case KeyCodes.KEY_LEFT:
                        col = col - 1 < 0 ? 6 : col - 1;
                        break;
                    case KeyCodes.KEY_RIGHT:
                        col = col + 1 > 6 ? 0 : col + 1;
                        break;
                    case KeyCodes.KEY_ENTER:
                        ValueChangeEvent.fire(source, selected);
                }

                selected = table.select(row, col);

            }
        }, KeyDownEvent.getType());

    }

    /**
     * Method registers a ValueChangeHandler<Datetime> to this widget
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Datetime> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    /**
     * Sets the selected date for this calendar and will cause the month year in the 
     * date to be displayed
     */
    public void setDate(Datetime date) {
        selected = date;
        if (date != null) {
            year = date.get(Datetime.YEAR);
            month = date.get(Datetime.MONTH);
        }
        DataChangeEvent.fire(this);
    }

    /**
     *  Draws the passed month and year in the calendar without changing the selected
     *  date.
     */
    public void drawMonth(int year, int month) {
        this.year = year;
        this.month = month;
        DataChangeEvent.fire(this);
    }

    /**
     * Registers a ClickHandler for the monthSelect button from the parent widget 
     * so that it can switch the view to the MonthYearWidget.
     * @param handler
     */
    public void addMonthSelectHandler(ClickHandler handler) {
        monthSelect.addClickHandler(handler);
    }

    /**
     * Returns the current year being displayed in the calendar
     * @return
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the current month being displayed in the calendar 
     * @return
     */
    public int getMonth() {
        return month;
    }
}
