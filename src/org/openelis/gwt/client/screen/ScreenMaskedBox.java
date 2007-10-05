package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.MaskedTextBox;
import org.openelis.gwt.common.AbstractField;

/**
 * ScreenMaskedBox wraps a MaskedTextBox widget for display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenMaskedBox extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "maskedbox";
	/**
	 * Widget wrapped by this class
	 */
    private MaskedTextBox maskbox;
    private boolean enabled;
    private String next;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenMaskedBox() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;maskedbox key="string" shortcut="char" mask="string" next="string"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenMaskedBox(Node node, final Screen screen) {
        super(node);
        maskbox = new MaskedTextBox() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
            
            public void complete() {
                screen.doTab(null,this);
            }
        };
        if (node.getAttributes().getNamedItem("shortcut") != null)
            maskbox.setAccessKey(node.getAttributes()
                                     .getNamedItem("shortcut")
                                     .getNodeValue()
                                     .charAt(0));
        String mask = node.getAttributes().getNamedItem("mask").getNodeValue();
        if(node.getAttributes().getNamedItem("next") != null){
            next = node.getAttributes().getNamedItem("next").getNodeValue();
        }
        maskbox.setMask(mask);
        initWidget(maskbox);
        maskbox.setStyleName("ScreenMaskedBox");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenMaskedBox(node, screen);
    }

    public void load(AbstractField field) {
        maskbox.setText(field.toString());

    }

    public void submit(AbstractField field) {
        field.setValue(maskbox.getText());

    }
    
    public void enable(boolean enabled){
        maskbox.setReadOnly(!enabled);
    }
    
    public void setFocus(boolean focus){
        maskbox.setFocus(focus);
    }
    
}
