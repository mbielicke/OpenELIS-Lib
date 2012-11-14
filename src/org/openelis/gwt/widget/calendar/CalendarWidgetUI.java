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

import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.constants.Constants;
import org.openelis.gwt.screen.Calendar;
import org.openelis.gwt.widget.Button;
import org.openelis.gwt.widget.DateHelper;
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Calendar Widget will display a calendar and text boxes with the current time
 * in a popup panel. A user can then select a date and time that will be
 * returned back to a form.
 * 
 * @author tschmidt
 * 
 */
public class CalendarWidgetUI extends Composite implements HasValueChangeHandlers<Datetime> {
	
	@UiTemplate("calendar.ui.xml")
	interface CalendarUiBinder extends UiBinder<Widget, CalendarWidgetUI>{};
	public static final CalendarUiBinder uiBinder = GWT.create(CalendarUiBinder.class);

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
    private int                 year,month;

    /*
     * Buttons from xsl
     */
    @UiField
    protected FocusPanel        prevMonth, nextMonth, monthSelect;

    /*
    @UiField(provided=true)
    protected TextBox<Datetime> time;
    */
    
    @UiField
    protected Label             monthDisplay;
 
    /*
    @UiField
    protected HorizontalPanel   TimeBar;
    */

    @UiField
    protected CalendarTableUI   table;
    
    private byte                begin,end;

    /*
     * Constructor that takes the precision of the date to be used.
     */
    public CalendarWidgetUI(byte begin, byte end) throws Exception {
    	/*
    	time = new TextBox<Datetime>();
    	
    	DateHelper helper = new DateHelper();
    	helper.setBegin((byte)3);
    	helper.setEnd((byte)5);
    	helper.setPattern("HH:MM");
    	
    	time.setHelper(helper);
    	*/
    	initWidget(uiBinder.createAndBindUi(this));

        //service = new ScreenService("controller?service=org.openelis.gwt.server.CalendarService");

        this.begin = begin;
        this.end = end;
        
        current = Calendar.get().getCurrentDatetime(begin, end);

        year = current.get(Datetime.YEAR);
        month = current.get(Datetime.MONTH);

        initialize();
    }

    /*
     * Initialize widgets on Screen.
     */
    @SuppressWarnings("deprecation")
	private void initialize() throws Exception {

        /*
         * Final reference to this class to be used by anon handlers
         */
        final CalendarWidgetUI source = this;

        table.addSelectionHandler(new SelectionHandler<Datetime>() {
            public void onSelection(SelectionEvent<Datetime> event) {
            	Datetime value;
            	Date date;
            	
            	value = event.getSelectedItem();
            	if(end > Datetime.DAY){
            		date = new Date(value.getDate().getYear(),
            				        value.getDate().getMonth(),
            				        value.getDate().getDate());
            				        //time.getValue().getDate().getHours(),
            				        //time.getValue().getDate().getMinutes());
            		value = Datetime.getInstance(begin,end,date);
            	}
                ValueChangeEvent.fire(source, value);
            }
        });

        //monthSelect.setEnabled(true);

        prevMonth.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                CalendarImpl cal = CalendarImpl.getInstance();
                cal.set(year, month, 1, 0, 0, 0);
                cal.add(CalendarImpl.MONTH, -1);
                month = cal.get(CalendarImpl.MONTH);
                setDateDisplay();
            }
        });
        //prevMonth.setEnabled(true);

        nextMonth.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                CalendarImpl cal = CalendarImpl.getInstance();
                cal.set(year, month, 1, 0, 0, 0);
                cal.add(CalendarImpl.MONTH, 1);
                month = cal.get(CalendarImpl.MONTH);
                setDateDisplay();
            }
        });
        //nextMonth.setEnabled(true);

        /*
        today.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    ValueChangeEvent.fire(source, Calendar.get().getCurrentDatetime(selected.getStartCode(),
                                                                                    selected.getEndCode()));
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        */

        /*
         * Show or hide time component based on the widget precision.
         
        if (current.getEndCode() > Datetime.DAY) 
            TimeBar.setVisible(true);
        */
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
        setDateDisplay();
    }
    
    private void setDateDisplay() {
    	String monthText = "";
        
    	selected = selected == null ? current : selected; 
        table.setCalendar(year, month, selected, current);
        
        switch(month) {
        	case 0 :
        		monthText = Constants.get().month0();
        		break;
        	case 1 : 
        		monthText = Constants.get().month1();
        		break;
        	case 2 : 
        		monthText = Constants.get().month2();
        		break;
        	case 3 : 
        		monthText = Constants.get().month3();
        		break;
        	case 4 : 
        		monthText = Constants.get().month4();
        		break;
        	case 5 : 
        		monthText = Constants.get().month5();
        		break;
        	case 6 : 
        		monthText = Constants.get().month6();
        		break;
        	case 7 : 
        		monthText = Constants.get().month7();
        		break;
        	case 8 : 
        		monthText = Constants.get().month8();
        		break;
        	case 9 : 
        		monthText = Constants.get().month9();
        		break;
        	case 10 : 
        		monthText = Constants.get().month10();
        		break;
        	case 11 : 
        		monthText = Constants.get().month11();
        		break;
        }
        
        monthDisplay.setText(monthText + " " + (year + 1900));
        /*
        if (current.getEndCode() > Datetime.DAY) 
        	time.setValue(selected);
        */
    }

    /**
     *  Draws the passed month and year in the calendar without changing the selected
     *  date.
     */
    public void drawMonth(int year, int month) {
        this.year = year;
        this.month = month;
        setDateDisplay();
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
