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

import java.util.ArrayList;
import java.util.Date;

import org.openelis.gwt.common.CalendarRPC;
import org.openelis.ui.common.Datetime;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.services.CalendarService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
/**
 * Calendar Widget will display a calendar and text boxes with the current time in
 * a popup panel.  A user can then select a date and time that will be returned back
 * to a form.
 * @author tschmidt
 *
 */
 public class CalendarWidget extends Screen implements HasValue<Datetime>, ClickHandler {
   
    protected AppButton prevMonth;
    protected AppButton nextMonth;
    protected AppButton monthSelect;
    protected AppButton ok;
    protected AppButton cancel;
    protected AppButton prevDecade;
    protected AppButton nextDecade;
    protected AppButton today;
    protected ArrayList<AppButton> months;
    protected ArrayList<AppButton> years; 
    protected CalendarRPC form;
    protected TextBox<Datetime> time;
    protected ScreenDefInt calendarDef;
    protected ScreenDefInt monthYearDef;
    
    public CalendarWidget(Datetime date,byte begin, byte end) throws Exception {
    	super((ScreenDefInt)GWT.create(CalendarDef.class));
    	calendarDef = def;
        form = new CalendarRPC();
        form.date = date;
        form.begin = begin;
        form.end = end;
        form = CalendarService.get().getMonth(form);
        //ScreenDef def = new ScreenDef();
        //def.setXMLString(form.xml);
		//drawScreen(def);
		setHandlers();
    }
    
    public void setHandlers() {
    	if(def.getName().equals("Calendar")) {
    		((Label)def.getWidget("MonthDisplay")).setText(form.monthDisplay);
    		boolean displayMonth = false; 
    		Date currDate = new Date();
    		if(form.month == currDate.getMonth() && (form.year -1900) == currDate.getYear())
    			displayMonth = true;
    		for(int i = 0; i < 6; i++) {
    			for(int j = 0; j < 7; j++) {
    				Label date = (Label)def.getWidget("cell:"+i+":"+j);
    				date.setStyleName("DateText");
    				if(i == 0 && form.cells[i][j] > 7) {
    					date.addStyleName("offMonth");
    					((IconContainer)date.getParent()).enable(false);
    				}
    				else if(i >= 4 && form.cells[i][j] < 14){
    					date.addStyleName("offMonth");
    					((IconContainer)date.getParent()).enable(false);
    				}else{
    					date.removeStyleName("offMonth");
    					((IconContainer)date.getParent()).enable(true);
    					if(displayMonth && form.cells[i][j] == form.date.get(Datetime.DAY))
        					date.addStyleName("Current");
    					else
    						date.removeStyleName("Current");
    				}
    				date.setText(String.valueOf(form.cells[i][j]));
    				if(prevMonth == null)
    					date.addClickHandler(this);
    			}
    		}
       		if(prevMonth == null){
    			prevMonth = (org.openelis.gwt.widget.AppButton)def.getWidget("prevMonth");
    			prevMonth.addClickHandler(this);
    			prevMonth.enable(true);
    		}
    		if(nextMonth == null){
    			nextMonth = (AppButton)def.getWidget("nextMonth");
    			nextMonth.addClickHandler(this);
    			nextMonth.enable(true);
    		}
    		if(monthSelect == null){
    			monthSelect = (AppButton)def.getWidget("monthSelect");
    			monthSelect.addClickHandler(this);
    			monthSelect.enable(true);
    		}
    		if(today == null) {
    			today = (AppButton)def.getWidget("today");
    			today.addClickHandler(this);
    			today.enable(true);
    		}
    		if(form.date.getEndCode() > Datetime.DAY){
    			if(time == null) {
    				time = (TextBox<Datetime>)def.getWidget("time");
    			}
    			time.setFieldValue(Datetime.getInstance(Datetime.HOUR,Datetime.MINUTE,form.date.getDate()));
    			def.getWidget("TimeBar").setVisible(true);
    		}else
    			def.getWidget("TimeBar").setVisible(false);
    	}else{
        	if(prevDecade == null){
        		prevDecade = (AppButton)def.getWidget("prevDecade");
            	prevDecade.addClickHandler(this);
            	prevDecade.enable(true);
        	}
        	if(nextDecade == null) {
        		nextDecade = (AppButton)def.getWidget("nextDecade");
        		nextDecade.addClickHandler(this);
        		nextDecade.enable(true);
        	}
        	if(ok == null){
        		ok = (AppButton)def.getWidget("ok");
    			ok.addClickHandler(this);
    			ok.enable(true);
        	}
        	if(cancel == null) {
        		cancel = (AppButton)def.getWidget("cancel");
        		cancel.addClickHandler(this);
        		cancel.enable(true);
        	}
        	if(months == null) {
        		months = new ArrayList<AppButton>();
        		for(int i = 0; i < 12; i++) {
        			months.add((AppButton)def.getWidget("month"+i));
        			((AppButton)def.getWidget("month"+i)).addClickHandler(this);
        		}
        	}
        	if(years == null) {
        		years = new ArrayList<AppButton>();
        		for(int i = 0; i < 10; i++) {
        			years.add((AppButton)def.getWidget("year"+i));
        			years.get(i).addClickHandler(this);
        			
        		}
        	}
        	int yr = form.year/10*10;
        	for(int i = 0; i < 10; i++) {
        		Label year = (Label)def.getWidget("year"+i+"Text");
        		year.setText(String.valueOf(yr+i));
        		if(form.year == yr+i){
        			((Widget)def.getWidget("year"+i)).addStyleName("Current");
        		}
        	}
        	((Widget)def.getWidget("month"+form.month)).addStyleName("Current");
        	
        }
    }
    
    public void onClick(ClickEvent event) {
        
        if(event.getSource() == prevMonth){
            form.month--;
            if(form.month < 0) {
                form.month = 11;
                form.year--;
            }
            try {
            	form = CalendarService.get().getMonth(form);
            	setHandlers();
            }catch(Exception e) {
            	e.printStackTrace();
            	Window.alert(e.getMessage());
            }
            return;
        }
        if(event.getSource() == nextMonth){
            form.month++;
            if(form.month > 11) {
                form.month = 0;
                form.year++;
            }
            try {
            	form = CalendarService.get().getMonth(form);
            	setHandlers();
            }catch(Exception e) {
            	e.printStackTrace();
            	Window.alert(e.getMessage());
            }
            return;
        }
        if(event.getSource() == monthSelect){
        	try {
        		//form = service.call("getMonthSelect",form);
        		//ScreenDef newDef = new ScreenDef();
        		//newDef.setXMLString(form.xml);
        		if(monthYearDef == null)
        			monthYearDef = (ScreenDefInt)GWT.create(MonthYearDef.class);
        		drawScreen(monthYearDef);
        		setHandlers();
        	}catch(Exception e) {
        		e.printStackTrace();
        		Window.alert(e.getMessage());
        	}
        	return;
        }
        if(event.getSource() == ok || 
           event.getSource() == cancel){
        	try {
        		form = CalendarService.get().getMonth(form);
        		drawScreen(calendarDef);
        		setHandlers();
        	}catch(Exception e) {
        		e.printStackTrace();
        		Window.alert(e.getMessage());
        	}
            return;
        }
        if(event.getSource() == prevDecade){
            years.get(form.year%10).removeStyleName("Current");
            form.year = form.year/10*10 -10;
            for(int i = 0; i < 10; i++) 
                ((Label)def.getWidget("year"+i+"Text")).setText(String.valueOf(form.year+i));
            years.get(0).addStyleName("Current");
            return;
        }
        if(event.getSource() == nextDecade){
            years.get(form.year%10).removeStyleName("Current");
            form.year = form.year/10*10 +10;
            for(int i = 0; i < 10; i++) 
                ((Label)def.getWidget("year"+i+"Text")).setText(String.valueOf(form.year+i));
            years.get(0).addStyleName("Current");
            return;
        }
        if(event.getSource() instanceof AppButton && months != null && months.contains(event.getSource()) ){
            for(int i = 0; i < 11; i++) {
            	months.get(i).removeStyleName("Current");
            }
            form.month = months.indexOf(event.getSource());
            ((AppButton)event.getSource()).addStyleName("Current");
            return;
        }
        if(event.getSource() == today) {
        	try {
        		setValue(CalendarService.get().getCurrentDatetime(form.begin,form.end),true);
        		return;
        	}catch(Exception e) {
        		e.printStackTrace();
        		return;
        	}
        }
        if(event.getSource() instanceof AppButton && years != null && years.contains(event.getSource()) ) {
            years.get(form.year%10).removeStyleName("Current");
            form.year = (form.year/10*10) + years.indexOf(event.getSource());
            ((AppButton)event.getSource()).addStyleName("Current");
            return;
        }
        if(((Label)event.getSource()).getStyleName().indexOf("offMonth") < 0){
        	String date =  ((Label)event.getSource()).getText();
        	if(form.end > Datetime.DAY)
        		setValue(Datetime.getInstance(form.begin,form.end,new Date(form.year-1900,form.month,Integer.parseInt(date),time.getFieldValue().get(Datetime.HOUR),time.getFieldValue().get(Datetime.MINUTE))),true);
        	else
        		setValue(Datetime.getInstance(form.begin,form.end,new Date(form.year-1900,form.month,Integer.parseInt(date))),true);
        }
    }

	public Datetime getValue() {
		return form.date;
	}

	public void setValue(Datetime value) {
		setValue(value,false);
	}

	public void setValue(Datetime value, boolean fireEvents) {
		Datetime old = form.date;
		form.date = value;
		if(fireEvents)
			ValueChangeEvent.fire(this, getValue());
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Datetime> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}
}
