package org.openelis.gwt.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.data.NumberField;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.ScreenAbsolute;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.services.CalendarServiceInt;
import org.openelis.gwt.services.CalendarServiceIntAsync;

import java.util.Arrays;
import java.util.HashMap;
/**
 * Calendar Widget will display a calendar and text boxes with the current time in
 * a popup panel.  A user can then select a date and time that will be returned back
 * to a form.
 * @author tschmidt
 *
 */
public class CalendarWidget  extends AppScreen implements SourcesChangeEvents, ClickListener {
    
    protected CalendarServiceIntAsync screenService = (CalendarServiceIntAsync) GWT
    .create(CalendarServiceInt.class);
    protected ServiceDefTarget target = (ServiceDefTarget) screenService;
    
    protected ChangeListenerCollection changeListeners;
    
    protected NumberField year;
    protected NumberField month;
    protected AppButton prevMonth;
    protected AppButton nextMonth;
    protected AppButton monthSelect;
    protected AppButton ok;
    protected AppButton cancel;
    protected ScreenAbsolute prevDecade;
    protected ScreenAbsolute nextDecade;
    protected AppButton today;
    protected ScreenLabel[] months = new ScreenLabel[12];
    protected ScreenLabel[] years = new ScreenLabel[10];
    
    public CalendarWidget(String date) {
        super();
        String base = GWT.getModuleBaseURL();
        base += "CalendarServlet";        
        target.setServiceEntryPoint(base);
        service = screenService;
        HashMap map = new HashMap();
        map.put("date",new StringObject(date));
        getXMLData(map);
    }
    
    public void afterDraw(boolean success) {
        super.afterDraw(success);
        year = (NumberField)rpc.getField("year");
        month = (NumberField)rpc.getField("month");
        prevMonth = (AppButton)getWidget("prevMonth");
        nextMonth = (AppButton)getWidget("nextMonth");
        monthSelect = (AppButton)getWidget("monthSelect");
        prevDecade = (ScreenAbsolute)widgets.get("prevDecade");
        nextDecade = (ScreenAbsolute)widgets.get("nextDecade");
        ok = (AppButton)getWidget("ok");
        cancel = (AppButton)getWidget("cancel");
        for(int i = 0; i < 12; i++) {
            months[i] = (ScreenLabel)widgets.get("month:"+i+"Text"); 
        }
        for(int i = 0; i < 10; i++) {
            years[i] = (ScreenLabel)widgets.get("year:"+i+"Text");
         }
         DOM.removeEventPreview(this);
    }
    
    public void onClick(Widget sender){
        if(sender == prevMonth){
            month.setValue(new Integer(((Integer)month.getValue()).intValue() - 1));
            if(((Integer)month.getValue()).intValue() < 0) {
                month.setValue(new Integer(11));
                year.setValue(new Integer(((Integer)year.getValue()).intValue() - 1));
            }
            final AppScreen scr = this;
            screenService.getMonth(String.valueOf(month), String.valueOf(year), (String)rpc.getFieldValue("date"), new AsyncCallback() {
                public void onSuccess(Object result) {
                    redrawScreen((String)result);
                    DOM.removeEventPreview(scr);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == nextMonth){
            month.setValue(new Integer(((Integer)month.getValue()).intValue() + 1));
            if(((Integer)month.getValue()).intValue() > 11) {
                month.setValue(new Integer(0));
                year.setValue(new Integer(((Integer)year.getValue()).intValue() + 1));
            }
            final AppScreen scr = this;
            screenService.getMonth(String.valueOf(month), String.valueOf(year), (String)rpc.getFieldValue("date"), new AsyncCallback() {
                public void onSuccess(Object result) {
                    redrawScreen((String)result);
                    DOM.removeEventPreview(scr);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == monthSelect){
            final AppScreen scr = this;
            screenService.getMonthSelect(String.valueOf(month), String.valueOf(year), new AsyncCallback() {
                public void onSuccess(Object result) {
                    redrawScreen((String)result);
                    DOM.removeEventPreview(scr);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == ok || 
           sender == cancel){
            final AppScreen scr = this;
            screenService.getMonth(month.getValue().toString(), year.getValue().toString(), (String)rpc.getFieldValue("date"), new AsyncCallback() {
                public void onSuccess(Object result) {
                    redrawScreen((String)result);
                    DOM.removeEventPreview(scr);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == prevDecade){
            years[((Integer)year.getValue()).intValue()%10].label.removeStyleName("Current");
            year.setValue(new Integer(((Integer)year.getValue()).intValue()/10*10 -10));
            for(int i = 0; i < 10; i++) 
                years[i].label.setText(String.valueOf(((Integer)year.getValue()).intValue()+i));
            years[0].label.addStyleName("Current");
            return;
        }
        if(sender == nextDecade){
            years[((Integer)year.getValue()).intValue()%10].label.removeStyleName("Current");
            year.setValue(new Integer(((Integer)year.getValue()).intValue()/10*10 +10));
            for(int i = 0; i < 10; i++) 
                years[i].label.setText(String.valueOf(((Integer)year.getValue()).intValue()+i));
            years[0].label.addStyleName("Current");
            return;
        }
        if(Arrays.asList(months).contains(sender)){
            String[] value = ((String)((ScreenWidget)sender).getUserObject()).split(",");
            months[((Integer)month.getValue()).intValue()].label.removeStyleName("Current");
            month.setValue(value[1]);
            ((ScreenLabel)sender).label.addStyleName("Current");
            return;
        }
        if(Arrays.asList(years).contains(sender)){
            String[] value = ((String)((ScreenWidget)sender).getUserObject()).split(",");
            years[((Integer)year.getValue()).intValue()%10].label.removeStyleName("Current");
            year.setValue(new Integer(((Integer)year.getValue()).intValue()/10*10+Integer.parseInt(value[1])));
            ((ScreenLabel)sender).label.addStyleName("Current");
            return;
        }
        changeListeners.fireChange(sender);
    }

    public void addChangeListener(ChangeListener listener) {
        if(changeListeners == null){
            changeListeners = new ChangeListenerCollection();
        }
        changeListeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        if(changeListeners != null){
            changeListeners.remove(listener);
        }
    }
}
