package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;
/**
 * ScreenTextBox wraps a GWT TextBox to be displayed on a Screen.
 * @author tschmidt
 *
 */
public class ScreenTextBox extends ScreenInputWidget implements ChangeListener{
	/**
	 * Default XML Tag Name used in XML Definition
	 */
	public static String TAG_NAME = "textbox";
	/**
	 * Widget wrapped by this class
	 */
    private TextBox textbox;
    private String fieldCase;
    private int length = 255;
  
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTextBox() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;textbox key="string" tab="string,string" shortcut="char" 
     *          case="mixed,upper,lower" max="int"/&gt; 
     * @param node
     * @param screen
     */	
    public ScreenTextBox(Node node, final ScreenBase screen) {
        super(node);
        textbox = new TextBox() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                        return;
                    }
                }
                super.onBrowserEvent(event);
            }
        };
        if (node.getAttributes().getNamedItem("tab") != null) {
            screen.addTab(textbox, node.getAttributes()
                                       .getNamedItem("tab")
                                       .getNodeValue()
                                       .split(","));
            textbox.sinkEvents(Event.KEYEVENTS);
        }
        if (node.getAttributes().getNamedItem("shortcut") != null)
            textbox.setAccessKey(node.getAttributes()
                                     .getNamedItem("shortcut")
                                     .getNodeValue()
                                     .charAt(0));
        textbox.setStyleName("ScreenTextBox");
        if (node.getAttributes().getNamedItem("case") != null){
            fieldCase = node.getAttributes().getNamedItem("case")
                                            .getNodeValue();
            if (fieldCase.equals("upper")){
                textbox.addStyleName("Upper");
            }
            if (fieldCase.equals("lower")){
                textbox.addStyleName("Lower");
            }
        }
        if (node.getAttributes().getNamedItem("max") != null) {
            length = Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue());
            textbox.setMaxLength(length);
        }
            
        initWidget(textbox);
        displayWidget = textbox;
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTextBox(node, screen);
    }

    public void load(AbstractField field) {
        if(!queryMode)
            textbox.setText(field.toString().trim());
        else
            queryWidget.load(field);
    }

    public void submit(AbstractField field) {
        if(!queryMode)
            field.setValue(textbox.getText());
        else
            queryWidget.submit(field);

    }

    public void onChange(Widget sender) {    
    }
    
    public void enable(boolean enabled){
        textbox.setReadOnly(!enabled);
    }
    
    public void setFocus(boolean focus){
        textbox.setFocus(focus);
    }
    
    public void destroy() {
        textbox = null;
        super.destroy();
    }
    
    public void setForm(boolean mode) {
        if(queryWidget == null){
            if(mode)
                textbox.setMaxLength(255);
            else
                textbox.setMaxLength(length);
        }else
            super.setForm(mode);
    }
    
}
