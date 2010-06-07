package org.openelis.gwt.widget;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.HasDataChangeHandlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class UCalendarTable extends Composite implements HasSelectionHandlers<Datetime>, HasDataChangeHandlers{
    
    protected FlexTable table;
    protected Datetime[][] dates;
    
    public UCalendarTable() {
    	final UCalendarTable source = this;
    	
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
    
    public void setCalendar(Datetime date) {
    	Datetime counter = Datetime.getInstance(date.getStartCode(),date.getEndCode(),date.getDate());
    	counter = counter.add(-(counter.get(Datetime.DAY)-1));

    	if(counter.getDate().getDay() > 0)
            counter = counter.add(-(counter.getDate().getDay()));
        else
            counter = counter.add(-7);
    	dates = new Datetime[6][7];
        
    	for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 7; j++) {
            	dates[i][j] = counter;
                com.google.gwt.user.client.ui.Label cell = ((com.google.gwt.user.client.ui.Label)table.getWidget(i+1,j));
                cell.setText(String.valueOf(counter.get(Datetime.DAY)));
                if(counter.get(Datetime.MONTH) != date.get(Datetime.MONTH))
                    cell.addStyleName("offMonth");
                else
                    cell.removeStyleName("offMonth");
                if(counter.equals(date))
                    cell.addStyleName("current");
                else
                    cell.removeStyleName("current");
                counter = counter.add(1);
            }
        }
    }

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Datetime> handler) {
		return addHandler(handler,SelectionEvent.getType());
	}

	public HandlerRegistration addDataChangeHandler(DataChangeHandler handler) {
		return addHandler(handler,DataChangeEvent.getType());
	}
}
