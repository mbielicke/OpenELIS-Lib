package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;

/**
 * ScreenPassword wraps a GWT Password widget for Display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenPassword extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "password";
	/**
	 * Widget wrapped by this class
	 */
    private PasswordTextBox textbox;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenPassword() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;password key="string" shortcut="char"/&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenPassword(Node node, final Screen screen) {
        super(node);
        textbox = new PasswordTextBox() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        if (node.getAttributes().getNamedItem("shortcut") != null)
            textbox.setAccessKey(node.getAttributes()
                                     .getNamedItem("shortcut")
                                     .getNodeValue()
                                     .charAt(0));
        initWidget(textbox);
        textbox.setStyleName("ScreenPassword");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenPassword(node, screen);
    }

    public void load(AbstractField field) {
        textbox.setText(field.toString());

    }

    public void submit(AbstractField field) {
        field.setValue(textbox.getText());
    }
    
    public void enable(boolean enabled){
        textbox.setReadOnly(!enabled);
    }
    
    public void setFocus(boolean focus){
        textbox.setFocus(focus);
    }
}
