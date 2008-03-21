package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DateField;
import org.openelis.gwt.widget.FormCalendarWidget;

import java.util.Date;
/**
 * ScreenCalendar wraps a FormCalendar to be displayed on a Screen
 * @author tschmidt
 *
 */
public class ScreenCalendar extends ScreenInputWidget {
    
    /**
     * Default Tag Name for XML Definition and WidgetMap
     */
    public static String TAG_NAME = "calendar";
    /**
     * Widget wrapped by this class
     */
    private FormCalendarWidget cal;
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
        byte begin = Byte.parseByte(node.getAttributes()
                                        .getNamedItem("begin")
                                        .getNodeValue());
        byte end = Byte.parseByte(node.getAttributes()
                                      .getNamedItem("end")
                                      .getNodeValue());
        if (node.getAttributes().getNamedItem("week") != null)
            cal = new FormCalendarWidget(begin,
                                         end,
                                         Boolean.valueOf(node.getAttributes()
                                                             .getNamedItem("week")
                                                             .getNodeValue())
                                                .booleanValue());
        else
            cal = new FormCalendarWidget(begin, end, false);
        cal.setForm(screen);
        if (node.getAttributes().getNamedItem("shortcut") != null)
            cal.setShortcutKey(node.getAttributes()
                                   .getNamedItem("shortcut")
                                   .getNodeValue()
                                   .charAt(0));
        if (node.getAttributes().getNamedItem("onChange") != null){
            String listener = node.getAttributes().getNamedItem("onChange").getNodeValue();
            if (listener.equals("this"))
                cal.addChangeListener(screen);
            else          
                cal.addChangeListener((ChangeListener)ScreenBase.getWidgetMap().get(listener));
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
        }
    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else{
            field.setValue(null);
            Date date = null;
            String entered = cal.getText();
            if (field instanceof DateField) {
                if (entered != null && !entered.equals("")) {
                    try {
                        date = new Date(entered.replaceAll("-", "/"));
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
        }
    }
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else{
            cal.setEnabled(enabled);
            if(enabled)
                cal.addFocusListener(this);
            else
                cal.removeFocusListener(this);
        }
    }
    
    public void destroy() {
        cal = null;
        super.destroy();
    }
    
    public void setFocus(boolean focus){
        cal.setFocus(focus);
    }

}
