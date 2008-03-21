package org.openelis.gwt.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.ScreenText;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.services.CalendarServiceInt;
import org.openelis.gwt.services.CalendarServiceIntAsync;
/**
 * Calendar Widget will display a calendar and text boxes with the current time in
 * a popup panel.  A user can then select a date and time that will be returned back
 * to a form.
 * @author tschmidt
 *
 */
public class CalendarWidget  extends AppScreen implements SourcesChangeEvents {
    
    protected CalendarServiceIntAsync screenService = (CalendarServiceIntAsync) GWT
    .create(CalendarServiceInt.class);
    protected ServiceDefTarget target = (ServiceDefTarget) screenService;
    
    protected ChangeListenerCollection changeListeners;
    
    public CalendarWidget() {
        String base = GWT.getModuleBaseURL();
        base += "CalendarServlet";        
        target.setServiceEntryPoint(base);
        service = screenService;
        getXML();
    }
    
    public void onClick(Widget sender){
        if(sender == getWidget("prevMonth")){
            int month = ((Integer)rpc.getFieldValue("month")).intValue() - 1;
            int year = ((Integer)rpc.getFieldValue("year")).intValue();
            if(month < 0) {
                month = 11;
                year -= 1;
            }
            screenService.getMonth(String.valueOf(month), String.valueOf(year), new AsyncCallback() {
                public void onSuccess(Object result) {
                    redrawScreen((String)result);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == getWidget("nextMonth")){
            int month = ((Integer)rpc.getFieldValue("month")).intValue() + 1;
            int year = ((Integer)rpc.getFieldValue("year")).intValue();
            if(month > 11) {
                month = 0;
                year += 1;
            }
            screenService.getMonth(String.valueOf(month), String.valueOf(year), new AsyncCallback() {
                public void onSuccess(Object result) {
                    redrawScreen((String)result);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == getWidget("monthSelect")){
            int month = ((Integer)rpc.getFieldValue("month")).intValue();
            int year = ((Integer)rpc.getFieldValue("year")).intValue();
            screenService.getMonthSelect(String.valueOf(month), String.valueOf(year), new AsyncCallback() {
                public void onSuccess(Object result) {
                    redrawScreen((String)result);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == getWidget("ok") || 
           sender == getWidget("cancel")){
            int month = ((Integer)rpc.getFieldValue("month")).intValue();
            int year = ((Integer)rpc.getFieldValue("year")).intValue();
            screenService.getMonth(String.valueOf(month), String.valueOf(year), new AsyncCallback() {
                public void onSuccess(Object result) {
                    redrawScreen((String)result);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == getWidget("prevDecade")){
            ((Widget) getWidget("year"+":"+ ((Integer)rpc.getFieldValue("year")).intValue()%10)).removeStyleName("Current");
            int year = ((Integer)rpc.getFieldValue("year")).intValue()/10*10;
            year += -10;
            for(int i = 0; i < 10; i++) 
                ((ScreenText)widgets.get("year:"+i+"Text")).text.setText(String.valueOf(year+i));
            rpc.setFieldValue("year",new Integer(year));
            ((Widget) getWidget("year:0")).addStyleName("Current");
            return;
        }
        if(sender == getWidget("nextDecade")){
            ((Widget) getWidget("year"+":"+ ((Integer)rpc.getFieldValue("year")).intValue()%10)).removeStyleName("Current");
            int year = ((Integer)rpc.getFieldValue("year")).intValue()/10*10;
            year += 10;
            for(int i = 0; i < 10; i++) 
                ((ScreenText)widgets.get("year:"+i+"Text")).text.setText(String.valueOf(year+i));
            rpc.setFieldValue("year",new Integer(year));
            ((Widget) getWidget("year:0")).addStyleName("Current");
            return;
        }
        if(((ScreenWidget)sender).getWidget().getStyleName().indexOf("MYCell") > -1){
            String[] value = ((String)((ScreenWidget)sender).getUserObject()).split(",");
            if(value[0].equals("month")){
                ((Widget) getWidget("month"+":"+ rpc.getFieldValue("month")) ).removeStyleName("Current");
                rpc.setFieldValue("month",value[1]);
                ((Widget) getWidget("month"+":"+ rpc.getFieldValue("month")) ).addStyleName("Current");
            }else{
                ((Widget) getWidget("year"+":"+ ((Integer)rpc.getFieldValue("year")).intValue()%10)).removeStyleName("Current");
                rpc.setFieldValue("year", new Integer(((Integer)rpc.getFieldValue("year")).intValue()+Integer.parseInt(value[1])));
                ((Widget) getWidget("year"+":"+ ((Integer)rpc.getFieldValue("year")).intValue()%10)).addStyleName("Current");
            }
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
