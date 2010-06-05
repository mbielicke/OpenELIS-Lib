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
package org.openelis.gwt.widget;

import org.openelis.gwt.common.CalendarRPC;
import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.screen.ScreenEventHandler;
import org.openelis.gwt.services.ScreenService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
/**
 * Calendar Widget will display a calendar and text boxes with the current time in
 * a popup panel.  A user can then select a date and time that will be returned back
 * to a form.
 * @author tschmidt
 *
 */
 public class UCalendarWidget extends Screen implements HasValueChangeHandlers<Datetime> {
   
    private CalendarRPC data;
    
    protected AppButton prevMonth, nextMonth, monthSelect, today;
   
    protected TextBox<Datetime> time;
    protected Label monthDisplay;
    
    protected UCalendarTable table;
    
    public UCalendarWidget(Datetime date) throws Exception {
    	super((ScreenDefInt)GWT.create(CalendarDef.class));
    	service = new ScreenService("controller?service=org.openelis.gwt.server.CalendarService");
    	initialize(date);
    }
    
    private void initialize(Datetime date) throws Exception {
        
        final UCalendarWidget source = this;
        
        table = new UCalendarTable();
        ((VerticalPanel)def.getWidget("calContainer")).add(table);
        table.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                Cell cell = ((FlexTable)event.getSource()).getCellForEvent(event);
                ValueChangeEvent.fire(source, data.cells[cell.getRowIndex()-1][cell.getCellIndex()]);
            }
        });
        
        monthDisplay = (Label)def.getWidget("MonthDisplay");
        addScreenHandler(monthDisplay, new ScreenEventHandler<String>() {
            public void onDataChange(DataChangeEvent event) {
                monthDisplay.setText(data.monthDisplay);
            }
        });
        
        prevMonth = (AppButton)def.getWidget("prevMonth");
        prevMonth.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                data.month--;
                if(data.month < 0) {
                    data.month = 11;
                    data.year--;
                }
                try {
                    data = service.call("getMonth",data);
                    table.setCalendar(data);
                    DataChangeEvent.fire(source);
                }catch(Exception e) {
                    e.printStackTrace();
                    Window.alert(e.getMessage());
                }
                return;
                
            }
        });
        
        nextMonth = (AppButton)def.getWidget("nextMonth");
        nextMonth.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                data.month++;
                if(data.month > 11) {
                    data.month = 0;
                    data.year++;
                }
                try {
                    data = service.call("getMonth", data);
                    table.setCalendar(data);
                    DataChangeEvent.fire(source);
                }catch(Exception e) {
                    e.printStackTrace();
                    Window.alert(e.getMessage());
                }
                
            }
        });
        
        today = (AppButton)def.getWidget("today");
        today.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    ValueChangeEvent.fire(source,service.callDatetime("getCurrentDatetime",data.begin,data.end));
                }catch(Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        
        if(date.endCode > Datetime.DAY){
            time = (TextBox<Datetime>)def.getWidget("time");
            addScreenHandler(time, new ScreenEventHandler<Datetime>() {
                public void onDataChange(DataChangeEvent event) {
                    time.setValue(data.date);
                } 
            });
            def.getWidget("TimeBar").setVisible(true);
        }else
            def.getWidget("TimeBar").setVisible(false);
        
        data = new CalendarRPC();
        data.date = date;
        data.begin = date.startCode;
        data.end = date.endCode;
        data = service.call("getMonth",data);
        
        table.setCalendar(data);
        
        DataChangeEvent.fire(this);
        
    }

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Datetime> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}
}
