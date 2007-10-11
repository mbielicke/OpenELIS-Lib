package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.AutoCompleteTextBox;
import org.openelis.gwt.common.AbstractField;

/**
 * ScreenAuto wraps an AutoComplete widget to be displayed on a Screen
 * @author tschmidt
 *
 */
public class ScreenAuto extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "auto";

	/**
	 * Widget wrapped by this class.
	 */
    private AutoCompleteTextBox auto;

	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenAuto() {
    }

    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &;t;auto key="string" serviceUrl="string" cat="string" shortcut="char"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenAuto(Node node, final Screen screen) {
        super(node);
        String cat = node.getAttributes().getNamedItem("cat").getNodeValue();
        String url = node.getAttributes()
                         .getNamedItem("serviceUrl")
                         .getNodeValue();
        auto = new AutoCompleteTextBox(cat, url) {
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
            auto.setAccessKey(node.getAttributes()
                                  .getNamedItem("shortcut")
                                  .getNodeValue()
                                  .charAt(0));
        auto.setStyleName("ScreenAuto");
        initWidget(auto);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenAuto(node, screen);
    }

    public void load(AbstractField field) {
        auto.setValue((Integer)field.getValue());
    }

    public void submit(AbstractField field) {
        if (((String)field.getKey()).endsWith("Id"))
            field.setValue(auto.value);
        if ((((String)field.getKey()).endsWith("Text"))) {
            field.setValue(auto.getText());
        }
    }

    public void setFocus(boolean focused) {
        // TODO Auto-generated method stub
        auto.setFocus(focused);
    }

    public void enabled(boolean enabled){
        auto.setReadOnly(!enabled);
    }
    public void destroy() {
        auto = null;
        super.destroy();
    }
}
