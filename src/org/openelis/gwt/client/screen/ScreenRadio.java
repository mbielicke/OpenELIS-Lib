package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.CheckField;

/**
 * ScreenRadio wraps a GWT RadioButton widget for Display on a Screen.
 * @author tschmidt
 *
 */
public class ScreenRadio extends ScreenInputWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "radio";
	/**
	 * Widget wrapped by this class
	 */
    private RadioButton radio;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenRadio() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;radio key="string" group="string" shortcut="char"&lt;TEXT&lt;/radio&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenRadio(Node node, final ScreenBase screen) {
        super(node);
        radio = new RadioButton(node.getAttributes()
                                    .getNamedItem("group")
                                    .getNodeValue()) {
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
        if (node.getFirstChild() != null)
            radio.setText(node.getFirstChild().getNodeValue());
        if (node.getAttributes().getNamedItem("shortcut") != null)
            radio.setAccessKey(node.getAttributes()
                                   .getNamedItem("shortcut")
                                   .getNodeValue()
                                   .charAt(0));
        initWidget(radio);
        displayWidget = radio;
        radio.setStyleName("ScreenRadio");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenRadio(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else
            radio.setChecked(((CheckField)field).isChecked());
    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else
            field.setValue(new Boolean(radio.isChecked()));
    }
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else
            radio.setEnabled(enabled);
    }
    
    public void setFocus(boolean focus){
        if(queryMode)
            queryWidget.setFocus(focus);
        else
            radio.setFocus(focus);
    }
    
    public void destroy() {
        radio = null;
        super.destroy();
    }

}
