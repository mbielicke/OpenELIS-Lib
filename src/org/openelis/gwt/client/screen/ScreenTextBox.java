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
public class ScreenTextBox extends ScreenWidget implements ChangeListener{
	/**
	 * Default XML Tag Name used in XML Definition
	 */
	public static String TAG_NAME = "textbox";
	/**
	 * Widget wrapped by this class
	 */
    private TextBox textbox;
    private String fieldCase;
  
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
    public ScreenTextBox(Node node, final Screen screen) {
        super(node);
        textbox = new TextBox() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                    }
                } else if(DOM.eventGetType(event) == Event.ONKEYUP){
                    if(fieldCase.equals("upper"))
                        setText(getText().toUpperCase());
                    else
                        setText(getText().toLowerCase());   
                }else{
                    super.onBrowserEvent(event);
                }
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
        if (node.getAttributes().getNamedItem("case") != null){
            fieldCase = node.getAttributes().getNamedItem("case")
                                            .getNodeValue();
            if (!fieldCase.equals("mixed")){
                textbox.addChangeListener(this);
            }
        }
        if (node.getAttributes().getNamedItem("max") != null) {
            int length = Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue());
            textbox.setMaxLength(length);
        }
            
        initWidget(textbox);
        textbox.setStyleName("ScreenTextBox");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenTextBox(node, screen);
    }

    public void load(AbstractField field) {
        textbox.setText(field.toString().trim());

    }

    public void submit(AbstractField field) {
        field.setValue(textbox.getText());

    }

    public void onChange(Widget sender) {
        if(fieldCase.equals("upper"))
            textbox.setText(textbox.getText().toUpperCase());
        else
            textbox.setText(textbox.getText().toLowerCase());       
    }
    
    public void enable(boolean enabled){
        textbox.setReadOnly(!enabled);
    }
    
    public void setFocus(boolean focus){
        textbox.setFocus(focus);
    }
    
}
