package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.FormCalendarWidget;
import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.DateField;
import org.openelis.gwt.common.DatetimeRPC;

import java.util.Date;
/**
 * ScreenCalendar wraps a FormCalendar to be displayed on a Screen
 * @author tschmidt
 *
 */
public class ScreenCalendar extends ScreenWidget {
	
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
        setDefaults(node, screen);
        
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenCalendar(node, screen);
    }

    public void load(AbstractField field) {
        if (field instanceof DateField) {
            if (field.getValue() != null)
                cal.setDate((DatetimeRPC)field.getValue());
            else
                cal.setText("");
        }
    }

    public void submit(AbstractField field) {
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
    
    public void enable(boolean enabled){
        cal.setEnabled(enabled);
    }
    
    public void destroy() {
        cal = null;
        super.destroy();
    }

}
