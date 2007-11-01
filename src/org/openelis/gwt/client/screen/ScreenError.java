package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Node;
/**
 * ScreenError is a widget for displaying input errors on 
 * Screen 
 * @author tschmidt
 *
 */
public class ScreenError extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "error";
	/**
	 * Widget wrapped by this class
	 */
	private VerticalPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenError() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     *<br/><br/>
     * &lt;error id="string,string..."/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenError(Node node, ScreenBase screen) {
        super(node);
        panel = new VerticalPanel();
        panel.setVisible(false);
        String[] ids = node.getAttributes()
                           .getNamedItem("id")
                           .getNodeValue()
                           .split(",");
        for (int k = 0; k < ids.length; k++) {
            screen.errors.put(ids[k], panel);
        }
        initWidget(panel);
        panel.setStyleName("ScreenError");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenError(node, screen);
    }
    
    public void destroy() {
        panel = null;
        super.destroy();
    }

}
