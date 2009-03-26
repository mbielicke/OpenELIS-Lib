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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.common.CalendarForm;
import org.openelis.gwt.screen.AppScreen;
import org.openelis.gwt.screen.ScreenAbsolute;
import org.openelis.gwt.screen.ScreenLabel;
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
 public class CalendarWidget  extends AppScreen<CalendarForm> implements SourcesChangeEvents, ClickListener {
    
    protected CalendarServiceIntAsync screenService = (CalendarServiceIntAsync) GWT
    .create(CalendarServiceInt.class);
    protected ServiceDefTarget target = (ServiceDefTarget) screenService;
    
    protected ChangeListenerCollection changeListeners;
   
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
        CalendarForm form = new CalendarForm();
        form.date = date;
        getScreen(form);
    }
    
    public void afterDraw(boolean success) {
        super.afterDraw(success);
        //year = (NumberField)form.getFieldMap().get("year");
        //month = (NumberField)form.getFieldMap().get("month");
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
            form.month--;
            if(form.month < 0) {
                form.month = 11;
                form.year--;
            }
            final AppScreen scr = this;
            screenService.getMonth(form, new AsyncCallback<CalendarForm>() {
                public void onSuccess(CalendarForm result) {
                    redrawScreen(result.xml);
                    DOM.removeEventPreview(scr);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == nextMonth){
            form.month++;
            if(form.month > 11) {
                form.month = 0;
                form.year++;
            }
            final AppScreen scr = this;
            screenService.getMonth(form, new AsyncCallback<CalendarForm>() {
                public void onSuccess(CalendarForm result) {
                    redrawScreen(result.xml);
                    DOM.removeEventPreview(scr);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == monthSelect){
            final AppScreen scr = this;
            screenService.getMonthSelect(form, new AsyncCallback<CalendarForm>() {
                public void onSuccess(CalendarForm result) {
                    redrawScreen(result.xml);
                    DOM.removeEventPreview(scr);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == getWidget("ok") || 
           sender == getWidget("cancel")){
            final AppScreen scr = this;
            screenService.getMonth(form, new AsyncCallback<CalendarForm>() {
                public void onSuccess(CalendarForm result) {
                    redrawScreen(result.xml);
                    DOM.removeEventPreview(scr);
                }
                public void onFailure(Throwable caught){
                    
                }
            });
            return;
        }
        if(sender == prevDecade){
            years[form.year%10].label.removeStyleName("Current");
            form.year = form.year/10*10 -10;
            for(int i = 0; i < 10; i++) 
                years[i].label.setText(String.valueOf(form.year+i));
            years[0].label.addStyleName("Current");
            return;
        }
        if(sender == nextDecade){
            years[form.year%10].label.removeStyleName("Current");
            form.year = form.year/10*10 +10;
            for(int i = 0; i < 10; i++) 
                years[i].label.setText(String.valueOf(form.year+i));
            years[0].label.addStyleName("Current");
            return;
        }
        if(sender instanceof ScreenLabel && ((ScreenLabel)sender).key.startsWith("month:")){
            String[] value = ((String)((ScreenWidget)sender).getUserObject()).split(",");
            months[form.month].label.removeStyleName("Current");
            form.month = Integer.parseInt(value[1]);
            ((ScreenLabel)sender).label.addStyleName("Current");
            return;
        }
        if(sender instanceof ScreenLabel && ((ScreenLabel)sender).key.startsWith("year:")){
            String[] value = ((String)((ScreenWidget)sender).getUserObject()).split(",");
            years[form.year%10].label.removeStyleName("Current");
            form.year = form.year/10*10+Integer.parseInt(value[1]);
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
