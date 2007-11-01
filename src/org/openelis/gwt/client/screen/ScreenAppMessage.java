package org.openelis.gwt.client.screen;

import com.google.gwt.xml.client.Node;
import org.openelis.gwt.client.widget.AppMessage;

/**
 * ScreenAppMessage will wrap is used to wrap an AppMessage widget to display messages to users
 * on a screen.
 * @author tschmidt
 * 
 */
public class ScreenAppMessage extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "appmessage";

	/**
	 * Widget being wrapped by this widget
	 * 
	 */
	private AppMessage msg;

	public ScreenAppMessage() {

	}

	/**
	 * Constructor called from getInstance to return a specific instance of this
	 * class to be displayed on the screen. It uses the XML Node to create it's
	 * widget.
	 * <br/><br/>
	 *
	 * &lt;appmessage key="string"/&gt;
	 * 
	 * @param node
	 * @param screen
	 */
	public ScreenAppMessage(Node node, ScreenBase screen) {
		msg = new AppMessage();
		initWidget(msg);
		setDefaults(node, screen);
	}

	public ScreenWidget getInstance(Node node, ScreenBase screen) {
		return new ScreenAppMessage(node, screen);
	}
    
    public void destroy() {
        msg = null;
        super.destroy();
    }
}
