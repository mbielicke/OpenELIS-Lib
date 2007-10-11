package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;
/**
 * ScreenConstant wraps a Label widget who's text is set from a resource bundle
 * @author tschmidt
 *
 */
public class ScreenConstant extends ScreenWidget {
	/**
	 * Default XML Tag Name in XML Definition and WidgetMap
	 */
	public static String TAG_NAME = "const";
	/**
	 * Widget that is wrapped by this class
	 */
	private Label label; 
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenConstant() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;const key="string" wordwrap="boolean"&gt;NAME OF CONSTANT&lt;/const&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenConstant(Node node, Screen screen) {
        super(node);
        label = null;
        if (node.hasChildNodes())
            label = new Label(screen.constants.getString(node.getFirstChild()
                                                             .getNodeValue()));
        else
            label = new Label("");
        if (node.getAttributes().getNamedItem("wordwrap") != null)
            label.setWordWrap(Boolean.valueOf(node.getAttributes()
                                                  .getNamedItem("wordwrap")
                                                  .getNodeValue())
                                     .booleanValue());
        else
            label.setWordWrap(false);
        initWidget(label);
        label.setStyleName("ScreenLabel");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        // TODO Auto-generated method stub
        return new ScreenConstant(node, screen);
    }
    
    public void destroy() {
        label = null;
        super.destroy();
    }
}
