package org.openelis.gwt.widget.calendar;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.HasDataChangeHandlers;
import org.openelis.gwt.widget.Label;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class CalendarTable extends Composite implements HasSelectionHandlers<Datetime>, HasDataChangeHandlers{
    
    protected FlexTable table;
    protected Datetime[][] dates;
    
    protected int selectedRow, selectedCol;
    
    public CalendarTable() {
    	final CalendarTable source = this;
    	
    	table = new FlexTable();
        table.insertRow(0);
        table.getRowFormatter().setStyleName(0,"DayBar");

        table.setWidget(0, 0, new Label("S"));
        table.getCellFormatter().setStyleName(0,0,"DayCell");
        table.getWidget(0, 0).setStyleName("DayText");

        table.setWidget(0, 1, new Label("M"));
        table.getCellFormatter().setStyleName(0,1,"DayCell");
        table.getWidget(0, 1).setStyleName("DayText");

        table.setWidget(0, 2, new Label("T"));
        table.getCellFormatter().setStyleName(0,2,"DayCell");
        table.getWidget(0, 2).setStyleName("DayText");
        
        table.setWidget(0, 3, new Label("W"));
        table.getCellFormatter().setStyleName(0,3,"DayCell");
        table.getWidget(0, 3).setStyleName("DayText");
        
        table.setWidget(0, 4, new Label("T"));
        table.getCellFormatter().setStyleName(0,4,"DayCell");
        table.getWidget(0, 4).setStyleName("DayText");
        
        table.setWidget(0, 5, new Label("F"));
        table.getCellFormatter().setStyleName(0,5,"DayCell");
        table.getWidget(0, 5).setStyleName("DayText");
        
        table.setWidget(0, 6, new Label("S"));
        table.getCellFormatter().setStyleName(0,6,"DayCell");
        table.getWidget(0, 6).setStyleName("DayText");
        
        for(int i = 1; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
                table.setWidget(i, j, new Label(""));
                table.getCellFormatter().setStyleName(i, j, "DateCell");
                table.getWidget(i, j).setStyleName("DateText");       
            }
        }
        
        initWidget(table);
        setStyleName("Calendar");
        setWidth("100%");
        table.setCellPadding(0);
        table.setCellSpacing(0);
        
        table.addClickHandler(new ClickHandler() {
        	public void onClick(ClickEvent event) {
        		Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);
        		SelectionEvent.fire(source, dates[cell.getRowIndex()-1][cell.getCellIndex()]);
        	}
        });
        
    }
    
    public void setCalendar(int year, int month, Datetime selected, Datetime current) {
        byte begin, end;
        
        begin = current.getStartCode();
        end   = current.getEndCode();
        
        CalendarImpl cal = CalendarImpl.getInstance();
        cal.set(year,month,1,0,0,0);

    	if(cal.get(CalendarImpl.DAY_OF_WEEK) > 0)
    	    cal.add(CalendarImpl.DATE, -cal.get(CalendarImpl.DAY_OF_WEEK));
        else
            cal.add(CalendarImpl.DATE, -7);
    	
    	dates = new Datetime[6][7];
        
    	selectedRow = -1;
    	selectedCol = -1;
    	for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 7; j++) {
            	dates[i][j] = Datetime.getInstance(begin,end,cal.getTime());
                com.google.gwt.user.client.ui.Label cell = ((com.google.gwt.user.client.ui.Label)table.getWidget(i+1,j));
                cell.setText(String.valueOf(cal.get(CalendarImpl.DATE)));
                
                if(cal.get(CalendarImpl.MONTH) != month)
                    cell.addStyleName("offMonth");
                else
                    cell.removeStyleName("offMonth");
                
                if(dates[i][j].equals(current))
                    cell.addStyleName("current");
                else
                    cell.removeStyleName("current");
                
                if(dates[i][j].equals(selected))
                    select(i+1,j);
                else 
                    cell.removeStyleName("selected");
                
                cal.add(CalendarImpl.DATE,1);
            }
        }
    }
    
    public int getSelectedRow() {
        return selectedRow;
    }
    
    public int getSelectedCol() {
        return selectedCol;
    }
    
    public Datetime select(int row, int col) {
        if(selectedRow > -1)
            table.getWidget(selectedRow, selectedCol).removeStyleName("selected");
        
        table.getWidget(row, col).addStyleName("selected");
        
        selectedRow = row;
        selectedCol = col;
        
        return dates[row -1][col];
    }

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Datetime> handler) {
		return addHandler(handler,SelectionEvent.getType());
	}

	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		return addHandler(handler,DataChangeEvent.getType());
	}
}
