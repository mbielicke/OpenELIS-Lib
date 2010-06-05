package org.openelis.gwt.widget;

import org.openelis.gwt.common.CalendarRPC;
import org.openelis.gwt.common.Datetime;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

public class UCalendarTable extends Composite {
    
    protected FlexTable table;
    
    public UCalendarTable() {
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
    }
    
    public void setCalendar(CalendarRPC rpc) {
        for(int i = 0; i < 6; i++) {
            for(int j = 0; j < 7; j++) {
                Label cell = ((Label)table.getWidget(i+1,j));
                cell.setText(String.valueOf(rpc.cells[i][j].get(Datetime.DAY)));
                if(rpc.cells[i][j].get(Datetime.MONTH) != rpc.date.get(Datetime.MONTH))
                    cell.addStyleName("offMonth");
                else
                    cell.removeStyleName("offMonth");
                if(rpc.cells[i][j].equals(rpc.date))
                    cell.addStyleName("current");
                else
                    cell.removeStyleName("current");
            }
        }
    }
    
    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return table.addClickHandler(handler);
    }
}
