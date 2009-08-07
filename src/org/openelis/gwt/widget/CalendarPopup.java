package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.ScreenAppButton;
import org.openelis.gwt.widget.table.PopupTable;
@Deprecated
public class CalendarPopup extends PopupTable implements ClickListener {
    
    private VerticalPanel vp = new VerticalPanel();
    private AppButton prevMonth;
    private Label month;
    private AppButton monthSelect;
    private AppButton nextMonth;
    
    public CalendarPopup() {
        vp.setStyleName("CalendarWidget");
        vp.setWidth("100%");
        HorizontalPanel monthBar = new HorizontalPanel();
        monthBar.setStyleName("MonthBar");
        
        prevMonth = new AppButton();
        AbsolutePanel ap = new AbsolutePanel();
        ap.setStyleName("PreviousMonth");
        prevMonth.setWidget(ap);
        prevMonth.addClickListener(this);
        monthBar.add(prevMonth);
        
        month = new Label();
        month.setStyleName("MonthDisplay");
        monthBar.add(month);
        
        monthSelect = new AppButton();
        ap = new AbsolutePanel();
        ap.setStyleName("MonthSelect");
        monthSelect.setWidget(ap);
        monthSelect.addClickListener(this);
        monthBar.add(monthSelect);
        
        nextMonth = new AppButton();
        ap = new AbsolutePanel();
        ap.setStyleName("NextMonth");
        nextMonth.setWidget(ap);
        nextMonth.addClickListener(this);
        monthBar.add(nextMonth);
        
        vp.add(monthBar);
        
        
        
         
        
        
        
        
    }

    public void onClick(Widget sender) {
        // TODO Auto-generated method stub
        
    }

}
