package org.openelis.gwt.widget;

import org.openelis.gwt.screen.Screen;
import org.openelis.gwt.screen.ScreenDefInt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class UMonthYearWidget extends Screen {
	
    int month = -1;
    int year  = -1;
    
    Label year0Text,
          year1Text,
          year2Text,
          year3Text,
          year4Text,
          year5Text,
          year6Text,
          year7Text,
          year8Text,
          year9Text;
    
	public UMonthYearWidget() {
		super((ScreenDefInt)GWT.create(MonthYearDef.class));
		initialize();
	}
	
	private void initialize(){
	    final AppButton month0 = (AppButton)def.getWidget("month0");
	    month0.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	            month = 0;
	        }
	    });
	    month0.enable(true);
	    
	    final AppButton month1 = (AppButton)def.getWidget("month1");
	    month1.addClickHandler(new ClickHandler() {
	       public void onClick(ClickEvent event) {
	           month = 1;
	        } 
	    });
	    month1.enable(true);
	    
        final AppButton month2 = (AppButton)def.getWidget("month2");
        month2.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 2;
            } 
        });
        month2.enable(true);
	    
        final AppButton month3 = (AppButton)def.getWidget("month3");
        month3.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               month = 3;
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
        
        year0Text = (Label)def.getWidget("year0Text");
        year1Text = (Label)def.getWidget("year1Text");
        year2Text = (Label)def.getWidget("year2Text");
        year3Text = (Label)def.getWidget("year3Text");
        year4Text = (Label)def.getWidget("year4Text");
        year5Text = (Label)def.getWidget("year5Text");
        year6Text = (Label)def.getWidget("year6Text");
        year7Text = (Label)def.getWidget("year7Text");
        year8Text = (Label)def.getWidget("year8Text");
        year9Text = (Label)def.getWidget("year9Text");
        
        final AppButton year0 = (AppButton)def.getWidget("year0");
        year0.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year0Text.getText());
            } 
        });
        year0.enable(true);
        
        final AppButton year1 = (AppButton)def.getWidget("year1");
        year1.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year1Text.getText());
            } 
        });
        year1.enable(true);
        
        final AppButton year2 = (AppButton)def.getWidget("year2");
        year2.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year2Text.getText());
            } 
        });
        year2.enable(true);
        
        final AppButton year3 = (AppButton)def.getWidget("year3");
        year3.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year3Text.getText());
            } 
        });
        year3.enable(true);        
        
        final AppButton year4 = (AppButton)def.getWidget("year4");
        year4.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year4Text.getText());
            } 
        });
        year4.enable(true);
        
        final AppButton year5 = (AppButton)def.getWidget("year5");
        year5.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year5Text.getText());
            } 
        });
        year5.enable(true);
        
        final AppButton year6 = (AppButton)def.getWidget("year6");
        year6.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year6Text.getText());
            } 
        });
        year6.enable(true);
        
        final AppButton year7 = (AppButton)def.getWidget("year7");
        year7.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year7Text.getText());
            } 
        });
        year7.enable(true);
        
        final AppButton year8 = (AppButton)def.getWidget("year8");
        year8.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year8Text.getText());
            } 
        });
        year8.enable(true);
        
        final AppButton year9 = (AppButton)def.getWidget("year9");
        year9.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               year = Integer.parseInt(year9Text.getText());
            } 
        });
        year9.enable(true);    
        
        
        final AppButton prevDecade = (AppButton)def.getWidget("prevDecade");
        prevDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = Integer.parseInt(year0Text.getText())-10;
               setDecade(yr);
            } 
        });
        prevDecade.enable(true);
        
        final AppButton nextDecade = (AppButton)def.getWidget("nextDecade");
        nextDecade.addClickHandler(new ClickHandler() {
           public void onClick(ClickEvent event) {
               int yr = Integer.parseInt(year0Text.getText())+10;
               setDecade(yr);
            } 
        });
        nextDecade.enable(true);
        
        final AppButton ok = (AppButton)def.getWidget("ok");
	}
	
	private void setDecade(int yr) {
        year0Text.setText(String.valueOf(yr));
        year1Text.setText(String.valueOf(yr+1));
        year2Text.setText(String.valueOf(yr+2));
        year3Text.setText(String.valueOf(yr+3));
        year4Text.setText(String.valueOf(yr+4));
        year5Text.setText(String.valueOf(yr+5));
        year6Text.setText(String.valueOf(yr+6));
        year7Text.setText(String.valueOf(yr+7));
        year8Text.setText(String.valueOf(yr+8));
        year9Text.setText(String.valueOf(yr+9));
	}

	
}
