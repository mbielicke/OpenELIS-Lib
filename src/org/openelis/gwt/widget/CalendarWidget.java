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
import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDef;
import org.openelis.gwt.services.ScreenService;
import org.openelis.gwt.widget.deprecated.IconContainer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
/**
 * Calendar Widget will display a calendar and text boxes with the current time in
 * a popup panel.  A user can then select a date and time that will be returned back
 * to a form.
 * @author tschmidt
 *
 */
 public class CalendarWidget extends Screen implements HasValue<Datetime>, ClickHandler {
   
    protected org.openelis.gwt.widget.AppButton prevMonth;
    protected org.openelis.gwt.widget.AppButton nextMonth;
    protected org.openelis.gwt.widget.AppButton monthSelect;
    protected org.openelis.gwt.widget.AppButton ok;
    protected org.openelis.gwt.widget.AppButton cancel;
    protected IconContainer prevDecade;
    protected IconContainer nextDecade;
    protected org.openelis.gwt.widget.AppButton today;
    protected ArrayList<Label> months = new ArrayList<Label>(12);
    protected ArrayList<Label> years = new ArrayList<Label>(10);
    protected ScreenService service; 
    protected CalendarRPC form;
    
    public CalendarWidget(Datetime date) throws Exception {
    	service = new ScreenService("controller?service=org.openelis.gwt.server.CalendarService");
        form = new CalendarRPC();
        form.date = date;
        form = service.call("getScreen",form);
        ScreenDef def = new ScreenDef();
        def.setXMLString(form.xml);
		drawScreen(def);
		setHandlers();
    }
    
    public void setHandlers() {
    	if(def.getName().equals("Calendar")) {
    		prevMonth = (org.openelis.gwt.widget.AppButton)def.getWidget("prevMonth");
    		prevMonth.addClickHandler(this);
    		nextMonth = (AppButton)def.getWidget("nextMonth");
    		nextMonth.addClickHandler(this);
    		monthSelect = (AppButton)def.getWidget("monthSelect");
    		monthSelect.addClickHandler(this);
    		for(int i = 1; i <= 42; i++) {
    			Label date = (Label)def.getWidget("Cell"+i);
    			if(date.getText() != null && !date.getText().equals(""))
    				((Label)def.getWidget("Cell"+i)).addClickHandler(this);
    		}
    	}else{
        	months = new ArrayList<Label>();
        	years = new ArrayList<Label>();
            prevDecade = (IconContainer)def.getWidget("prevDecade");
            prevDecade.addClickHandler(this);
            nextDecade = (IconContainer)def.getWidget("nextDecade");
            nextDecade.addClickHandler(this);
    		ok = (AppButton)def.getWidget("ok");
    		ok.addClickHandler(this);
    		cancel = (AppButton)def.getWidget("cancel");
    		cancel.addClickHandler(this);
            for(int i = 0; i < 12; i++) {
            	months.add((Label)def.getWidget("month:"+i+"Text")); 
            	months.get(i).addClickHandler(this);
            }
            for(int i = 0; i < 10; i++) {
            	years.add((Label)def.getWidget("year:"+i+"Text"));
            	years.get(i).addClickHandler(this);
            }
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
            	form = service.call("getMonth",form);
            	ScreenDef newDef = new ScreenDef();
            	newDef.setXMLString(form.xml);
            	drawScreen(newDef);
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
            	form = service.call("getMonth", form);
            	ScreenDef newDef = new ScreenDef();
            	newDef.setXMLString(form.xml);
            	drawScreen(newDef);
            	setHandlers();
            }catch(Exception e) {
            	e.printStackTrace();
            	Window.alert(e.getMessage());
            }
            return;
        }
        if(event.getSource() == monthSelect){
        	try {
        		form = service.call("getMonthSelect",form);
        		ScreenDef newDef = new ScreenDef();
        		newDef.setXMLString(form.xml);
        		drawScreen(newDef);
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
        		form = service.call("getMonth", form);
        		ScreenDef newDef = new ScreenDef();
        		newDef.setXMLString(form.xml);
        		drawScreen(newDef);
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
                years.get(i).setText(String.valueOf(form.year+i));
            years.get(0).addStyleName("Current");
            return;
        }
        if(event.getSource() == nextDecade){
            years.get(form.year%10).removeStyleName("Current");
            form.year = form.year/10*10 +10;
            for(int i = 0; i < 10; i++) 
                years.get(i).setText(String.valueOf(form.year+i));
            years.get(0).addStyleName("Current");
            return;
        }
        if(event.getSource() instanceof Label && months.contains(event.getSource()) ){
            String value = ((Label)event.getSource()).getText();
            for(int i = 0; i < 11; i++) {
            	months.get(i).removeStyleName("Current");
            }
            if(value.equals("Jan"))
            	form.month = 0;
            else if (value.equals("Feb"))
            	form.month = 1;
            else if (value.equals("Mar"))
            	form.month = 2;
            else if (value.equals("Apr"))
            	form.month = 3;
            else if (value.equals("May"))
            	form.month = 4;
            else if (value.equals("Jun"))
            	form.month = 5;
            else if (value.equals("Jul"))
            	form.month = 6;
            else if (value.equals("Aug"))
            	form.month = 7;
            else if (value.equals("Sep"))
            	form.month = 8;
            else if (value.equals("Oct"))
            	form.month = 9;
            else if (value.equals("Nov"))
            	form.month = 10;
            else if (value.equals("Dec"))
            	form.month = 12;
            ((Label)event.getSource()).addStyleName("Current");
            return;
        }
        if(event.getSource() instanceof Label && years.contains(event.getSource()) ) {
            String value = ((Label)event.getSource()).getText();
            years.get(form.year%10).removeStyleName("Current");
            form.year = Integer.parseInt(value);
            ((Label)event.getSource()).addStyleName("Current");
            return;
        }
        String date = ((Label)event.getSource()).getText();
        setValue(Datetime.getInstance(Datetime.YEAR,Datetime.DAY,new Date(form.year-1900,form.month,Integer.parseInt(date))),true);
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
			ValueChangeEvent.fireIfNotEqual(this, old, getValue());
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Datetime> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}
}
