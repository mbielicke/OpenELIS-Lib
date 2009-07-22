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
import org.openelis.gwt.screen.rewrite.Screen;
import org.openelis.gwt.screen.rewrite.UIUtil;
import org.openelis.gwt.services.CalendarServiceInt;
import org.openelis.gwt.services.CalendarServiceIntAsync;
import org.openelis.gwt.widget.rewrite.AppButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.HasValue;
/**
 * Calendar Widget will display a calendar and text boxes with the current time in
 * a popup panel.  A user can then select a date and time that will be returned back
 * to a form.
 * @author tschmidt
 *
 */
 public class CalendarWidget extends Screen implements HasValue<Date>, ClickHandler {
    
    protected CalendarServiceIntAsync screenService = (CalendarServiceIntAsync) GWT
    .create(CalendarServiceInt.class);
    protected ServiceDefTarget target = (ServiceDefTarget) screenService;
   
    protected org.openelis.gwt.widget.rewrite.AppButton prevMonth;
    protected org.openelis.gwt.widget.rewrite.AppButton nextMonth;
    protected org.openelis.gwt.widget.rewrite.AppButton monthSelect;
    protected org.openelis.gwt.widget.rewrite.AppButton ok;
    protected org.openelis.gwt.widget.rewrite.AppButton cancel;
    protected IconContainer prevDecade;
    protected IconContainer nextDecade;
    protected org.openelis.gwt.widget.rewrite.AppButton today;
    protected ArrayList<Label> months = new ArrayList<Label>(12);
    protected ArrayList<Label> years = new ArrayList<Label>(10);
    
    protected CalendarRPC form;
    
    public CalendarWidget(Date date) {
    	super();
        String base = GWT.getModuleBaseURL();
        base += "CalendarServlet";        
        target.setServiceEntryPoint(base);
        form = new CalendarRPC();
        form.date = date;
        screenService.getScreen(form, new AsyncCallback<CalendarRPC>() {
        	public void onSuccess(CalendarRPC result) {
        		form = result;
        		try {
        			setDef(UIUtil.createWidgets(form.xml));
        		}catch(Exception e) {
        			
        		}
        	}
        	public void onFailure(Throwable caught) {
        		Window.alert(caught.getMessage());
        	}
        });
    }
    
    public void afterDraw() {
    	if(def.name.equals("Calendar")) {
    		prevMonth = (org.openelis.gwt.widget.rewrite.AppButton)def.getWidget("prevMonth");
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
    
    public void onClick(ClickEvent event){
        
        if(event.getSource() == prevMonth){
            form.month--;
            if(form.month < 0) {
                form.month = 11;
                form.year--;
            }
            screenService.getMonth(form, new AsyncCallback<CalendarRPC>() {
                public void onSuccess(CalendarRPC result) {
                	form = result;
                	try {
                		setDef(UIUtil.createWidgets(form.xml));
                	}catch(Exception e) {
                		
                	}
                }
                public void onFailure(Throwable caught){
                    Window.alert(caught.getMessage());
                }
            });
            return;
        }
        if(event.getSource() == nextMonth){
            form.month++;
            if(form.month > 11) {
                form.month = 0;
                form.year++;
            }
            screenService.getMonth(form, new AsyncCallback<CalendarRPC>() {
                public void onSuccess(CalendarRPC result) {
                	form = result;
                	try {
                		setDef(UIUtil.createWidgets(form.xml));
                	}catch(Exception e) {
                		
                	}
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(event.getSource() == monthSelect){
            screenService.getMonthSelect(form, new AsyncCallback<CalendarRPC>() {
                public void onSuccess(CalendarRPC result) {
                	form = result;
                	try {
                		setDef(UIUtil.createWidgets(form.xml));
                	}catch(Exception e) {
                		
                	}
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(event.getSource() == ok || 
           event.getSource() == cancel){
            screenService.getMonth(form, new AsyncCallback<CalendarRPC>() {
                public void onSuccess(CalendarRPC result) {
                	form = result;
                	try {
                		setDef(UIUtil.createWidgets(form.xml));
                	}catch(Exception e) {
                		
                	}
                }
                public void onFailure(Throwable caught){
                    
                }
            });
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
            form.year = form.year/10*10+Integer.parseInt(value);
            ((Label)event.getSource()).addStyleName("Current");
            return;
        }
        String date = ((Label)event.getSource()).getText();
        setValue(new Date(form.year,form.month,Integer.parseInt(date)),true);
    }

	public Date getValue() {
		return form.date;
	}

	public void setValue(Date value) {
		setValue(value,false);
	}

	public void setValue(Date value, boolean fireEvents) {
		Date old = form.date;
		form.date = value;
		if(fireEvents)
			ValueChangeEvent.fireIfNotEqual(this, old, getValue());
		
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Date> handler) {
		return addHandler(handler,ValueChangeEvent.getType());
	}
}
