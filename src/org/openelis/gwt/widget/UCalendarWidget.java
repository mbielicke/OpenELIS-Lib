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

import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.event.ActionHandler;
import org.openelis.gwt.event.DataChangeEvent;
import org.openelis.gwt.event.DataChangeHandler;
import org.openelis.gwt.event.HasActionHandlers;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.screen.ScreenEventHandler;
import org.openelis.gwt.services.ScreenService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Calendar Widget will display a calendar and text boxes with the current time in
 * a popup panel.  A user can then select a date and time that will be returned back
 * to a form.
 * @author tschmidt
 *
 */
 public class UCalendarWidget extends Screen implements HasValueChangeHandlers<Datetime> {
   
    private Datetime data;
    private byte     begin;
    private byte     end;
    
    protected AppButton prevMonth, nextMonth, monthSelect, today;
   
    protected TextBox<Datetime> time;
    protected Label monthDisplay;
    
    protected UCalendarTable table;
    
    public UCalendarWidget(byte begin, byte end) throws Exception {
    	super((ScreenDefInt)GWT.create(CalendarDef.class));
 
    	service = new ScreenService("controller?service=org.openelis.gwt.server.CalendarService");
 
    	this.begin = begin;
    	this.end   = end;
    
    	initialize();    	
    }
    
    private void initialize() throws Exception {
        
        final UCalendarWidget source = this;
        
        table = new UCalendarTable();
        ((VerticalPanel)def.getWidget("calContainer")).add(table);
        
        addScreenHandler(table, new ScreenEventHandler<Object>() {
        	public void onDataChange(DataChangeEvent event) {
        		table.setCalendar(data);
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
                monthDisplay.setText(((Label)def.getWidget("month"+data.get(Datetime.MONTH))).getText()+" "+(data.get(Datetime.YEAR)+1900));
            }
        });
        
        monthSelect = (AppButton)def.getWidget("monthSelect");
        monthSelect.enable(true);
        
        prevMonth = (AppButton)def.getWidget("prevMonth");
        prevMonth.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
            	Date cal = data.getDate();
            	cal.setMonth(data.get(Datetime.MONTH)-1);
                data = Datetime.getInstance(data.startCode, data.endCode, cal);
                table.setCalendar(data);
                DataChangeEvent.fire(source);
            }
        });
        prevMonth.enable(true);
        
        nextMonth = (AppButton)def.getWidget("nextMonth");
        nextMonth.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
               	Date cal = data.getDate();
            	cal.setMonth(data.get(Datetime.MONTH)+1);
                data = Datetime.getInstance(data.startCode, data.endCode, cal);
                table.setCalendar(data);
                DataChangeEvent.fire(source);
            }
        });
        nextMonth.enable(true);
        
        today = (AppButton)def.getWidget("today");
        today.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    ValueChangeEvent.fire(source,service.callDatetime("getCurrentDatetime",data.startCode,data.endCode));
                }catch(Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
        
        if(end > Datetime.DAY){
            time = (TextBox<Datetime>)def.getWidget("time");
            addScreenHandler(time, new ScreenEventHandler<Datetime>() {
                public void onDataChange(DataChangeEvent event) {
                    time.setValue(data);
                } 
            });
            def.getWidget("TimeBar").setVisible(true);
        }else
            def.getWidget("TimeBar").setVisible(false);
        
    }

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Datetime> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}
	
	public void setDate(Datetime date) {
		if(date != null){
			data = date;
		}else{
			try {
				data = service.callDatetime("getCurrentDatetime", begin, end);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		DataChangeEvent.fire(this);
	}
	
	public void addMonthSelectHandler(ClickHandler handler) {
		monthSelect.addClickHandler(handler);
	}
}