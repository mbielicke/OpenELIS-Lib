package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.xml.client.Node;

/**
 * ScreenText wraps a GWT Label for displaying static text on
 * a Screen defined from the XML Document.
 * @author tschmidt
 *
 */
public class ScreenText extends ScreenWidget {
	/**
	 * Default XML Tag Name in XML Definition
	 */
	public static String TAG_NAME = "text";
	/**
	 * Widget wrapped by this class
	 */
    public Label text;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenText() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;text&gt;PUT TEXT HERE&lt;/text&gt;
     * 
     * @param node
     * @param screen
     */	
    public ScreenText(Node node, ScreenBase screen) {
        super(node);
        if (node.hasChildNodes())
            text = new Label(node.getFirstChild().getNodeValue());
        else
            text = new Label("");
        if (node.getAttributes().getNamedItem("wordwrap") != null)
            text.setWordWrap(Boolean.valueOf(node.getAttributes()
                                                 .getNamedItem("wordwrap")
                                                 .getNodeValue())
                                    .booleanValue());
        else
            text.setWordWrap(false);
        if (node.getAttributes().getNamedItem("mouse") != null) {
            String mouse = node.getAttributes()
                               .getNamedItem("mouse")
                               .getNodeValue();
            text.addMouseListener((MouseListener)ClassFactory.forName(mouse));
        }
        initWidget(text);
        text.setStyleName("ScreenLabel");
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenText(node, screen);
    }
    
    public void destroy() {
        text = null;
        super.destroy();
    }

}
