package org.openelis.gwt.widget.calendar;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.resources.CalendarCSS;
import org.openelis.gwt.resources.CalendarUICSS;
import org.openelis.gwt.resources.OpenELISResources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.Widget;
/**
 * This class is composite widget that displays a Calendar month.  
 * @author tschmidt
 *
 */
public class CalendarTableUI extends Composite implements HasSelectionHandlers<Datetime> {
    
	@UiTemplate("calendartable.ui.xml")
	interface CalendarTableUiBinder extends UiBinder<Widget, CalendarTableUI>{};
	public final static CalendarTableUiBinder uiBinder = GWT.create(CalendarTableUiBinder.class);
	
    /*
     * Wrapped table for displaying the date cells
     */
	@UiField
    protected Grid table;
    /*
     * 2D array Holding dates for each cell in the calendar
     */
    protected Datetime[][] dates;
    
    /*
     * Currently selected cell in the calendar
     */
    protected int selectedRow, selectedCol;
    
    protected CalendarUICSS css;
    
    /**
     * Default no-arg constructor
     */
    public CalendarTableUI() {
    	css = OpenELISResources.INSTANCE.calendarui();
    	css.ensureInjected();
    	
        /*
         * Final reference to this widget used in anon handlers
         */
    	final CalendarTableUI source = this;
    	
        initWidget(uiBinder.createAndBindUi(this));
        
        /*
         * Register ClickHandler for selection of date.
         */
        table.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		Cell cell = ((Grid)event.getSource()).getCellForEvent(event);
        		SelectionEvent.fire(source, dates[cell.getRowIndex()-1][cell.getCellIndex()]);
        	}
        });
        
    }
    
    /**
     * Draws the calendar to the month year passed and will use the selected and current Datetimes
     * params for setting the correct style to those cells if present.
     * @param year
     * @param month
     * @param selected
     * @param current
     */
    public void setCalendar(int year, int month, Datetime selected, Datetime current) {
        byte begin, end;
        /*
         * Precision to be used for the Datetimes for each cell
         */
        begin = current.getStartCode();
        end   = current.getEndCode();
        
        CalendarImpl cal = CalendarImpl.getInstance();
        cal.set(year,month,1,0,0,0);

        /*
         * Calculate the Date to be used for cell[0][0]
         */
    	if(cal.get(CalendarImpl.DAY_OF_WEEK) > 0)
    	    cal.add(CalendarImpl.DATE, -cal.get(CalendarImpl.DAY_OF_WEEK));
        else
            cal.add(CalendarImpl.DATE, -7);
    	
    	dates = new Datetime[6][7];
        
    	/*
    	 * Reset selected cell.
    	 */
    	selectedRow = -1;
    	selectedCol = -1;
    	
    	/*
    	 * Loop through for each cell adding 1 day to the begin date and add the needed style 
    	 * for each cell based on the date of that cell.
    	 */
    	for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 7; j++) {
            	dates[i][j] = Datetime.getInstance(begin,end,cal.getTime());
                table.setHTML(i+1,j,"<span>"+String.valueOf(cal.get(CalendarImpl.DATE))+"</span>");
                
                if(cal.get(CalendarImpl.MONTH) != month)
                    table.getCellFormatter().addStyleName(i+1,j,css.OffMonth());
                else
                    table.getCellFormatter().removeStyleName(i+1,j,css.OffMonth());
                
                if(dates[i][j].equals(current))
                    table.getCellFormatter().addStyleName(i+1,j,css.Current());
                else
                    table.getCellFormatter().removeStyleName(i+1,j,css.Current());
                
                if(dates[i][j].equals(selected))
                    select(i+1,j);
                else 
                    table.getCellFormatter().removeStyleName(i+1,j,css.selected());
                
                cal.add(CalendarImpl.DATE,1);
            }
        }
    }
    
    /**
     * Returns the currently selected calendar row index
     */
    public int getSelectedRow() {
        return selectedRow;
    }
    
    /**
     * Returns the currently selected calendar col index. 
     */
    public int getSelectedCol() {
        return selectedCol;
    }
    
    /**
     * Set the selected calendar cell.
     */
    public Datetime select(int row, int col) {
        if(selectedRow > -1)
            table.getCellFormatter().removeStyleName(selectedRow,selectedCol,css.selected());
        
        table.getCellFormatter().addStyleName(row,col,css.selected());
        
        selectedRow = row;
        selectedCol = col;
        
        return dates[row -1][col];
    }

    /**
     * Adds a SelectionHandler<Datetime> to this widget.
     */
	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Datetime> handler) {
		return addHandler(handler,SelectionEvent.getType());
	}

}
