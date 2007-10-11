package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * ScreenAbsolute is used to layout widgets on the Screen using absolute X and Y coordinates
 * it Wraps GWT's AbsolutePanel.
 * @author tschmidt
 *
 */
public class ScreenAbsolute extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "panel-absolute";
	/** 
	 * Widget wrapped by this class
	 */
	private AbsolutePanel panel;
	
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenAbsolute() {
    }

    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="absolute" key="string"&gt;
     *   &lt;widget x="int" y="int"&gt;
     *   ...
     *   &lt;/widget&gt;
     * &lt;/panel&gt;
     *
     * @param node
     * @param screen
     */
    public ScreenAbsolute(Node node, Screen screen) {
        super(node);
        panel = new AbsolutePanel();
        panel.setStyleName("ScreenAbsolute");
        initWidget(panel);
        NodeList widgets = node.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Node input = null;
                if (widgets.item(k).getNodeName().equals("widget")) {
                    NodeList inputList = widgets.item(k).getChildNodes();
                    for (int m = 0; m < inputList.getLength(); m++) {
                        if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                            input = inputList.item(m);
                            m = 100;
                        }
                    }
                } else
                    input = widgets.item(k);
                Widget wid = Screen.getWidgetMap().getWidget(input, screen);
                int x = -1;
                if (widgets.item(k).getAttributes().getNamedItem("x") != null)
                    x = Integer.parseInt(widgets.item(k)
                                                .getAttributes()
                                                .getNamedItem("x")
                                                .getNodeValue());
                int y = -1;
                if (widgets.item(k).getAttributes().getNamedItem("y") != null)
                    y = Integer.parseInt(widgets.item(k)
                                                .getAttributes()
                                                .getNamedItem("y")
                                                .getNodeValue());
                panel.add(wid, x, y);
            }
        }
        setDefaults(node, screen);
    }
    
    /**
     * Method called to return a specific instance of this widget defined in the 
     * XML node.
     */
    public ScreenWidget getInstance(Node node, Screen screen) {
        return new ScreenAbsolute(node, screen);
    }
    
    public void destroy() {
        panel = null;
        super.destroy();
    }

}
