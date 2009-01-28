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
package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DateField;
import org.openelis.gwt.common.data.QueryDateField;
import org.openelis.gwt.widget.CalendarLookUp;
import org.openelis.gwt.widget.FormCalendarWidget;

import java.util.Date;
/**
 * ScreenCalendar wraps a FormCalendar to be displayed on a Screen
 * @author tschmidt
 *
 */
public class ScreenCalendar extends ScreenInputWidget implements FocusListener{
    
    /**
     * Default Tag Name for XML Definition and WidgetMap
     */
    public static String TAG_NAME = "calendar";
    /**
     * Widget wrapped by this class
     */
    private CalendarLookUp cal;
    /**
     * Default no-arg constructor used to create reference in the WidgetMap class
     */
    public ScreenCalendar() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;calendar begin="int" end="int" week="boolean" shortcut="char" onChange="string"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenCalendar(Node node, final ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        byte begin = Byte.parseByte(node.getAttributes()
                                        .getNamedItem("begin")
                                        .getNodeValue());
        byte end = Byte.parseByte(node.getAttributes()
                                      .getNamedItem("end")
                                      .getNodeValue());
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            cal = (CalendarLookUp)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            cal = new CalendarLookUp();
        if (node.getAttributes().getNamedItem("week") != null)
            cal.init(begin,
                     end,
                     Boolean.valueOf(node.getAttributes()
                                          .getNamedItem("week")
                                          .getNodeValue())
                                          .booleanValue());
        else
            cal.init(begin, end, false);
        //l.init();
        /*if (node.getAttributes().getNamedItem("shortcut") != null)
            cal.setShortcutKey(node.getAttributes()
                                   .getNamedItem("shortcut")
                                   .getNodeValue()
                                   .charAt(0));
        */
        if (node.getAttributes().getNamedItem("onChange") != null){
            String listener = node.getAttributes().getNamedItem("onChange").getNodeValue();
            if (listener.equals("this"))
                cal.addChangeListener((ChangeListener)screen);
            else          
                cal.addChangeListener((ChangeListener)ClassFactory.forName(listener));
        }
        cal.setStyleName("ScreenCalendar");
        initWidget(cal);
        displayWidget = cal;
        setDefaults(node, screen);
        
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenCalendar(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else{
            if (field instanceof DateField) {
                if (field.getValue() != null)
                    cal.setDate((DatetimeRPC)field.getValue());
                else
                    cal.setText("");
            }
            super.load(field);
        }
    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else{
            field.setValue(null);
            Date date = null;
            String entered = cal.getText();
            entered = entered.replaceAll("-", "/");
            if (field instanceof DateField) {
                if (entered != null && !entered.equals("")) {
                    try {
                        date = new Date(entered);
                    } catch (Exception e) {
                        field.addError("Not a Valid Date");
                    }
                }
                if (date != null) {
                    field.setValue(DatetimeRPC.getInstance(((DateField)field).getBegin(),
                                                           ((DateField)field).getEnd(),
                                                           date));
                }
            }
            if(field instanceof QueryDateField && !entered.equals("")) {
                field.setValue(entered);
            }
            
        }
    }
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else{
            cal.enable(enabled);
            if(enabled)
                cal.addFocusListener(this);
            else
                cal.removeFocusListener(this);
            super.enable(enabled);
        }
    }
    
    public void destroy() {
        cal = null;
        super.destroy();
    }
    
    public void setFocus(boolean focus){
        if(queryMode)
            queryWidget.setFocus(focus);
        else
            cal.setFocus(focus);
    }
    
    public void onLostFocus(Widget sender) {
        cal.onLostFocus(sender);
        if(key != null)
            super.onLostFocus(sender);
    }
    
   public void onFocus(Widget sender) {
       cal.onFocus(sender);
       super.onFocus(sender);
   }

}
