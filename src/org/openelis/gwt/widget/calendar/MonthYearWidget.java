package org.openelis.gwt.widget.calendar;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.Button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;

/**
 * This screen is used by the Calendar widget to display buttons to select
 * a month and year to be displayed in the calendar
 * @author tschmidt
 *
 */
public class MonthYearWidget extends Screen {
	
    /*
     * Currently selected month and year.
     */
    protected int month = -1,year  = -1;
    
    protected Button ok,cancel;

    /**
     * Default no-arg constructor
     */
	public MonthYearWidget() {
		super((ScreenDefInt)GWT.create(MonthYearDef.class));
		initialize();
	}
	
	/**
	 * Initialize widgets on the screen and set up handlers
	 */
	private void initialize(){
	    Button button;
	    
	    /*
	     * Add ClickHandlers to the month buttons to set the month
	     * they represent.
	     */
	    for(int i = 0; i < 12; i++) {
	        final int monthIndex = i;
	        button = (Button)def.getWidget("month"+i);
	        button.addClickHandler(new ClickHandler() {
	           public void onClick(ClickEvent event) {
	               setMonth(monthIndex);
	           } 
	        });
	        button.setEnabled(true);
	    }

	    /*
	     * Add clickHandlers to the Year buttons to set the year
	     * they represent.
	     */
	    for(int i = 0; i < 10; i++) {
	        final int yearIndex = i;
	        button = (Button)def.getWidget("year"+i);
	        button.addClickHandler(new ClickHandler() {
	            public void onClick(ClickEvent event) {
	                setYear(Integer.parseInt(((Label)def.getWidget("year"+yearIndex+"Text")).getText())-1900);
	            }
	        });
	        button.setEnabled(true);
	    }
	    
	    /*
	     * Add ClickHandler to Previous Decade button to change the year buttons values down one decade.
	     */
        final Button prevDecade = (Button)def.getWidget("prevDecade");
        prevDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = Integer.parseInt(((Label)def.getWidget("year0Text")).getText())-10;
               setDecade(yr);
               toggleYear(-1);
            } 
        });
        prevDecade.setEnabled(true);
        
        /*
         * Add ClickHandler to the Next Decade button to change the year buttons values  up one decade.
         */
        final Button nextDecade = (Button)def.getWidget("nextDecade");
        nextDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = Integer.parseInt(((Label)def.getWidget("year0Text")).getText())+10;
               setDecade(yr);
               toggleYear(-1);
            } 
        });
        nextDecade.setEnabled(true);
        
        ok = (Button)def.getWidget("ok");
        ok.setEnabled(true);
        
        cancel = (Button)def.getWidget("cancel");
        cancel.setEnabled(true);
	}
	
	/**
	 * Sets the year buttons to current decade based on the first year passed. 
	 */
	private void setDecade(int yr) {
	    for(int i = 0; i < 10; i++)
	        ((Label)def.getWidget("year"+i+"Text")).setText(String.valueOf(yr+i));
	}
	
	/**
	 * Add ClickHandler to OK button from parent to handle selection of month year
	 * @param handler
	 */
	public void addOKHandler(ClickHandler handler) {
	    ok.addClickHandler(handler);
	}
	
	/**
	 * Add ClickHandler to Cancel button from parent to hide month year screen.
	 * @param handler
	 */
	public void addCancelHandler(ClickHandler handler) {
	    cancel.addClickHandler(handler);
	}
	
	/**
	 * Returns currently selected year
	 * @return
	 */
	public int getYear() {
	    return year;
	}
	
	/**
	 * Returns currently selected month
	 * @return
	 */
	public int getMonth() {
	    return month;
	}
	
	/**
	 * Sets the selected year to the passed in year
	 * @param year
	 */
	public void setYear(int year) {
	    this.year = year;
	    setDecade((year / 10 * 10) + 1900);
	    toggleYear(year % (year / 10 * 10));
	}
	
	/**
	 * Sets the selected month to the passed in month
	 * @param month
	 */
	public void setMonth(int month) {
	    this.month = month;
	    toggleMonth(month);
	}

	/**
	 * Runs through year buttons to set the selected year to be the only 
	 * pressed year
	 * @param yr
	 */
	private void toggleYear(int yr) {
	    for(int i = 0; i < 10; i++) 
	        ((Button)def.getWidget("year"+i)).setPressed(yr == i);
	}

	/**
	 * Runs through month buttons set the selected month to be the only
	 * pressed month
	 * @param month
	 */
	private void toggleMonth(int month) {
	    for(int i = 0; i < 12; i++)
	        ((Button)def.getWidget("month"+i)).setPressed(month == i);
	}

	
}
