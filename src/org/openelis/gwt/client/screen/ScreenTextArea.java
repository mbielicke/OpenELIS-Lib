package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;

/** 
 * ScreenTextArea wraps a GWT TextArea widget to be displayed on 
 * a Screen.
 * @author tschmidt
 *
 */
public class ScreenTextArea extends ScreenInputWidget implements FocusListener{
	/**
	 * Default XML Tag Name in XML Definition
	 */
	public static String TAG_NAME = "textarea";
	/**
	 * Widget wrapped by this class
	 */
    private TextArea textarea;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTextArea() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;textarea key="string" shortcut="char" tab="string,string"/&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenTextArea(Node node, final ScreenBase screen) {
        super(node);
        textarea = new TextArea() {
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
            textarea.setAccessKey(node.getAttributes()
                                      .getNamedItem("shortcut")
                                      .getNodeValue()
                                      .charAt(0));
        if (node.getAttributes().getNamedItem("tab") != null) {
            screen.addTab(textarea, node.getAttributes()
                                       .getNamedItem("tab")
                                       .getNodeValue()
                                       .split(","));
            textarea.sinkEvents(Event.KEYEVENTS);
        }
        
        textarea.addFocusListener(this);
        initWidget(textarea);
        displayWidget = textarea;
        textarea.setStyleName("ScreenTextArea");
        textarea.addStyleName("NoFocus");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTextArea(node, screen);
    }

    public void load(AbstractField field) {
        if(queryMode)
            queryWidget.load(field);
        else
            textarea.setText(field.toString());

    }

    public void submit(AbstractField field) {
        if(queryMode)
            queryWidget.submit(field);
        else
            field.setValue(textarea.getText());
    }
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else
            textarea.setReadOnly(!enabled);
    }
    
    public void setFocus(boolean focus){
        if(queryMode)
            queryWidget.setFocus(focus);
        else
            textarea.setFocus(focus);
    }
    
    public void destroy() {
        textarea = null;
        super.destroy();
    }

    public void onFocus(Widget sender) {
		if(!textarea.isReadOnly()){
			if(sender == textarea){
				textarea.removeStyleName("NoFocus");
				textarea.addStyleName("Focus");
			}
		}		
	}
	public void onLostFocus(Widget sender) {
		if(!textarea.isReadOnly()){
			if(sender == textarea){
				textarea.removeStyleName("Focus");
				textarea.addStyleName("NoFocus");
			}
		}
	}    
}
