package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenDisclosure wraps a GWT Disclosure panel for displaying widgets on a screen.
 * @author tschmidt
 *
 */
public class ScreenDisclosure extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "DisclosurePanel";
	/**
	 * Widget that is wrapped by this class
	 */
    private DisclosurePanel dp;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenDisclosure() {

    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="disclosure"&gt;
     *   &lt;widget&gt;
     *     First widget is Header
     *   &lt;/widget&gt;
     *   &lt;widget&gt;
     *     Second widget is Content
     *   &lt;/widget&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenDisclosure(Node node, ScreenBase screen) {
        super(node);
        dp = new DisclosurePanel();
        dp.setStyleName("ScreenDisclosure");
        initWidget(dp);
        Element header = (Element) ((Element)node).getElementsByTagName("header").item(0);
        NodeList widgets = header.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                dp.setHeader(wid);
            }
        }
        Element content = (Element) ((Element)node).getElementsByTagName("content").item(0);
        widgets = content.getChildNodes();
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
                Widget wid = ScreenBase.createWidget(input, screen);
                dp.setContent(wid);
            }
        }
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenDisclosure(node, screen);
    }
    
    public void destroy(){
        dp = null;
        super.destroy();
    }
}
