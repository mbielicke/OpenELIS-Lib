package org.openelis.gwt.widget.calendar;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;
import org.openelis.gwt.widget.AppButton;
import org.openelis.gwt.widget.Label;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class MonthYearWidget extends Screen {
	
    protected int month = -1,year  = -1;
    
    protected AppButton ok,cancel;
    
	public MonthYearWidget() {
		super((ScreenDefInt)GWT.create(MonthYearDef.class));
		initialize();
	}
	
	private void initialize(){
	    AppButton button;
	    
	    for(int i = 0; i < 12; i++) {
	        final int monthIndex = i;
	        button = (AppButton)def.getWidget("month"+i);
	        button.addClickHandler(new ClickHandler() {
	           public void onClick(ClickEvent event) {
	               setMonth(monthIndex);
	           } 
	        });
	        button.setEnabled(true);
	    }

	    for(int i = 0; i < 10; i++) {
	        final int yearIndex = i;
	        button = (AppButton)def.getWidget("year"+i);
	        button.addClickHandler(new ClickHandler() {
	            public void onClick(ClickEvent event) {
	                setYear(Integer.parseInt(((Label)def.getWidget("year"+yearIndex+"Text")).getText())-1900);
	            }
	        });
	        button.setEnabled(true);
	    }
	    
        final AppButton prevDecade = (AppButton)def.getWidget("prevDecade");
        prevDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = Integer.parseInt(((Label)def.getWidget("year0Text")).getText())-10;
               setDecade(yr);
               toggleYear(-1);
            } 
        });
        prevDecade.setEnabled(true);
        
        final AppButton nextDecade = (AppButton)def.getWidget("nextDecade");
        nextDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = Integer.parseInt(((Label)def.getWidget("year0Text")).getText())+10;
               setDecade(yr);
               toggleYear(-1);
            } 
        });
        nextDecade.setEnabled(true);
        
        ok = (AppButton)def.getWidget("ok");
        ok.setEnabled(true);
        
        cancel = (AppButton)def.getWidget("cancel");
        cancel.setEnabled(true);
	}
	
	private void setDecade(int yr) {
	    for(int i = 0; i < 10; i++)
	        ((Label)def.getWidget("year"+i+"Text")).setText(String.valueOf(yr+i));
	}
	
	public void addOKHandler(ClickHandler handler) {
	    ok.addClickHandler(handler);
	}
	
	public void addCancelHandler(ClickHandler handler) {
	    cancel.addClickHandler(handler);
	}
	
	public int getYear() {
	    return year;
	}
	
	public int getMonth() {
	    return month;
	}
	
	public void setYear(int year) {
	    this.year = year;
	    setDecade((year / 10 * 10) + 1900);
	    toggleYear(year % (year / 10 * 10));
	}
	
	public void setMonth(int month) {
	    this.month = month;
	    toggleMonth(month);
	}
	
	private void toggleYear(int yr) {
	    for(int i = 0; i < 10; i++) 
	        ((AppButton)def.getWidget("year"+i)).setPressed(yr == i);
	}
	
	private void toggleMonth(int month) {
	    for(int i = 0; i < 12; i++)
	        ((AppButton)def.getWidget("month"+i)).setPressed(month == i);
	}

	
}
