package org.openelis.gwt.widget;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class UMonthYearWidget extends Screen {
	
    protected int month = -1,year  = -1;
    
    protected AppButton ok,cancel;
    
	public UMonthYearWidget() {
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
	        button.enable(true);
	    }
	    
	    /*
	    final AppButton month0 = (AppButton)def.getWidget("month0");
	    month0.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	            setMonth(0);
	        }
	    });
	    month0.enable(true);
	    
	    final AppButton month1 = (AppButton)def.getWidget("month1");
	    month1.addClickHandler(new ClickHandler() {
	       public void onClick(ClickEvent event) {
	           setMonth(1);
	        } 
	    });
	    month1.enable(true);
	    
        final AppButton month2 = (AppButton)def.getWidget("month2");
        month2.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               setMonth(2);
            } 
        });
        month2.enable(true);
	    
        final AppButton month3 = (AppButton)def.getWidget("month3");
        month3.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               setMonth = 3;
            } 
        });
        month3.enable(true);
        
        final AppButton month4 = (AppButton)def.getWidget("month4");
        month4.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 4;
            } 
        });
        month4.enable(true);
        
        final AppButton month5 = (AppButton)def.getWidget("month5");
        month5.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 5;
            } 
        });
        month5.enable(true);
        
        final AppButton month6 = (AppButton)def.getWidget("month6");
        month6.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 6;
            } 
        });
        month6.enable(true);
        
        final AppButton month7 = (AppButton)def.getWidget("month7");
        month7.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 7;
            } 
        });
        month7.enable(true);
        
        final AppButton month8 = (AppButton)def.getWidget("month8");
        month8.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 8;
            } 
        });
        month8.enable(true);
        
        final AppButton month9 = (AppButton)def.getWidget("month9");
        month9.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 9;
            } 
        });
        month9.enable(true);
        
        final AppButton month10 = (AppButton)def.getWidget("month10");
        month10.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 10;
            } 
        });
        month10.enable(true);
        
        final AppButton month11 = (AppButton)def.getWidget("month11");
        month11.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 11;
            } 
        });
        month11.enable(true);
        */
	    
	    for(int i = 0; i < 10; i++) {
	        final int yearIndex = i;
	        button = (AppButton)def.getWidget("year"+i);
	        button.addClickHandler(new ClickHandler() {
	            public void onClick(ClickEvent event) {
	                setYear(Integer.parseInt(((Label)def.getWidget("year"+yearIndex+"Text")).getText())-1900);
	            }
	        });
	        button.enable(true);
	    }
	    
	    /*
        final AppButton year0 = (AppButton)def.getWidget("year0");
        year0.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               
            } 
        });
       
        year0.enable(true);
        
        final AppButton year1 = (AppButton)def.getWidget("year1");
        year1.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year1Text.getText())-1900;
            } 
        });
        year1.enable(true);
        
        final AppButton year2 = (AppButton)def.getWidget("year2");
        year2.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year2Text.getText())-1900;
            } 
        });
        year2.enable(true);
        
        final AppButton year3 = (AppButton)def.getWidget("year3");
        year3.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year3Text.getText())-1900;
            } 
        });
        year3.enable(true);        
        
        final AppButton year4 = (AppButton)def.getWidget("year4");
        year4.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year4Text.getText())-1900;
            } 
        });
        year4.enable(true);
        
        final AppButton year5 = (AppButton)def.getWidget("year5");
        year5.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year5Text.getText())-1900;
            } 
        });
        year5.enable(true);
        
        final AppButton year6 = (AppButton)def.getWidget("year6");
        year6.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year6Text.getText())-1900;
            } 
        });
        year6.enable(true);
        
        final AppButton year7 = (AppButton)def.getWidget("year7");
        year7.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year7Text.getText())-1900;
            } 
        });
        year7.enable(true);
        
        final AppButton year8 = (AppButton)def.getWidget("year8");
        year8.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year8Text.getText())-1900;
            } 
        });
        year8.enable(true);
        
        final AppButton year9 = (AppButton)def.getWidget("year9");
        year9.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year9Text.getText())-1900;
            } 
        });
        year9.enable(true);    
        
        */
	    
        final AppButton prevDecade = (AppButton)def.getWidget("prevDecade");
        prevDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = Integer.parseInt(((Label)def.getWidget("year0Text")).getText())-10;
               setDecade(yr);
               toggleYear(-1);
            } 
        });
        prevDecade.enable(true);
        
        final AppButton nextDecade = (AppButton)def.getWidget("nextDecade");
        nextDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = Integer.parseInt(((Label)def.getWidget("year0Text")).getText())+10;
               setDecade(yr);
               toggleYear(-1);
            } 
        });
        nextDecade.enable(true);
        
        ok = (AppButton)def.getWidget("ok");
        ok.enable(true);
        
        cancel = (AppButton)def.getWidget("cancel");
        cancel.enable(true);
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
	        ((AppButton)def.getWidget("year"+i)).setState(yr == i ? AppButton.ButtonState.PRESSED : AppButton.ButtonState.UNPRESSED);
	}
	
	private void toggleMonth(int month) {
	    for(int i = 0; i < 12; i++)
	        ((AppButton)def.getWidget("month"+i)).setState(month == i ? AppButton.ButtonState.PRESSED : AppButton.ButtonState.UNPRESSED);
	}

	
}
