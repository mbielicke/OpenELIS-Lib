package org.openelis.gwt.widget.calendar;

import org.openelis.gwt.widget.Button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * This screen is used by the Calendar widget to display buttons to select
 * a month and year to be displayed in the calendar
 * @author tschmidt
 *
 */
public class MonthYearWidgetUI extends Composite implements HasChangeHandlers {
	
	@UiTemplate("monthyear.ui.xml")
	interface MonthYearUiBinder extends UiBinder<Widget, MonthYearWidgetUI>{};
	public static final MonthYearUiBinder uiBinder = GWT.create(MonthYearUiBinder.class);
	
    /*
     * Currently selected month and year.
     */
    protected int month = -1,year  = -1;
    
    @UiField
    protected Button     month0,
    					 month1,
    					 month2,
    					 month3,
    					 month4,
    					 month5,
    					 month6,
    					 month7,
    					 month8,
    					 month9,
    					 month10,
    					 month11,
    					 prevDecade,
    					 nextDecade,
    					 year0,
    					 year1,
    					 year2,
    					 year3,
    					 year4,
    					 year5,
    					 year6,
    					 year7,
    					 year8,
    					 year9;
    					 
  
    Button               pressedMonth, pressedYear;

    /**
     * Default no-arg constructor
     */
	public MonthYearWidgetUI() {
		initWidget(uiBinder.createAndBindUi(this));
		initialize();
	}
	

	/**
	 * Initialize widgets on the screen and set up handlers
	 */
	private void initialize(){
	    	    
	    /*
	     * Add ClickHandler to Previous Decade button to change the year buttons values down one decade.
	     */
        prevDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = ((year / 10 * 10) + 1900) - 10;
               setDecade(yr);
               toggleYear(-1);
            } 
        });
        prevDecade.setEnabled(true);
        
        /*
         * Add ClickHandler to the Next Decade button to change the year buttons values  up one decade.
         */
        nextDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = ((year / 10 * 10) + 1900) + 10;
               setDecade(yr);
               toggleYear(-1);
            } 
        });
        nextDecade.setEnabled(true);
        
	}
	
	/**
	 * Sets the year buttons to current decade based on the first year passed. 
	 */
	private void setDecade(int yr) {
	    for(int i = 0; i < 10; i++)
	    	switch(i) {
	    		case 0 :
	    			year0.setText(String.valueOf(yr+i));
	    			break;
	    		case 1 :
	    			year1.setText(String.valueOf(yr+i));
	    			break;
	    		case 2 :
	    			year2.setText(String.valueOf(yr+i));
	    			break;
	    		case 3 :
	    			year3.setText(String.valueOf(yr+i));
	    			break;
	    		case 4 :
	    			year4.setText(String.valueOf(yr+i));
	    			break;
	    		case 5 :
	    			year5.setText(String.valueOf(yr+i));
	    			break;
	    		case 6 :
	    			year6.setText(String.valueOf(yr+i));
	    			break;
	    		case 7 :
	    			year7.setText(String.valueOf(yr+i));
	    			break;
	    		case 8 :
	    			year8.setText(String.valueOf(yr+i));
	    			break;
	    		case 9 :
	    			year9.setText(String.valueOf(yr+i));
	    	}
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
	
	public void setMonth(int month) {
		this.month = month;
		toggleMonth(month);
	}
	
	/**
	 * Sets the selected month to the passed in month
	 * @param month
	 */
	@UiHandler({"month0","month1","month2","month3","month4","month5","month6","month7","month8","month9","month10","month11"})	
	public void clickMonth(ClickEvent event) {
	    month = Integer.parseInt(((Button)event.getSource()).getAction());
	    ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
	}

	/**
	 * Runs through year buttons to set the selected year to be the only 
	 * pressed year
	 * @param yr
	 */
	@UiHandler({"year0","year1","year2","year3","year4","year5","year6","year7","year8","year9"})
	protected void clickYear(ClickEvent event) {
		year = Integer.parseInt(((Button)event.getSource()).getAction());
		ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(),this);
	}
	
	protected void toggleYear(int yr) {
		if(pressedYear != null)
			pressedYear.setPressed(false);
		switch(yr) {
			case 0 :
				pressedYear = year0;
				break;
			case 1 :
				pressedYear = year1;
				break;
			case 2 :
				pressedYear = year2;
				break;
			case 3 : 
				pressedYear = year3;
				break;
			case 4 :
				pressedYear = year4;
				break;
			case 5 :
				pressedYear = year5;
				break;
			case 6 :
				pressedYear = year6;
				break;
			case 7 :
				pressedYear = year7;
				break;
			case 8 :
				pressedYear = year8;
				break;
			case 9 :
				pressedYear = year9;
		}
		
		pressedYear.setPressed(true);
		
	}

	/**
	 * Runs through month buttons set the selected month to be the only
	 * pressed month
	 * @param month
	 */
	private void toggleMonth(int month) {
		if(pressedMonth != null)
			pressedMonth.setPressed(false);
		switch(month) {
			case 0 : 
				pressedMonth = month0;
				break;
			case 1 : 
				pressedMonth = month1;
				break;
			case 2 : 
				pressedMonth = month2;
				break;
			case 3 : 
				pressedMonth = month3;
				break;
			case 4 : 
				pressedMonth = month4;
				break;
			case 5 : 
				pressedMonth = month5;
				break;
			case 6 : 
				pressedMonth = month6;
				break;
			case 7 : 
				pressedMonth = month7;
				break;
			case 8 : 
				pressedMonth = month8;
				break;
			case 9 : 
				pressedMonth = month9;
				break;
			case 10 : 
				pressedMonth = month10;
				break;
			case 111 : 
				pressedMonth = month11;
				break;
		}
		pressedMonth.setPressed(true);

	}


	@Override
	public HandlerRegistration addChangeHandler(ChangeHandler handler) {
		return addHandler(handler,ChangeEvent.getType());
	}

	
}
