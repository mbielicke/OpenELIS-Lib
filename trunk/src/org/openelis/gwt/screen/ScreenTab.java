package org.openelis.gwt.screen;

import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenTab wraps a GWT TabPanel for displaying widgets 
 * on a Screen in Tab Layout.
 * @author tschmidt
 *
 */
public class ScreenTab extends ScreenWidget {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "panel-tab";
	/**
	 * Widget wrapped by this class
	 */
	private TabPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenTab() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;panel layout="tab" key="string"&gt;
     *   &lt;tab text="string"&gt;
     *    ....
     *   &lt;tab&gt;
     *   &lt;tab text="string"&gt;
     *    ....
     *   &lt;/tab&gt;
     * &lt;/panel&gt;
     *     
     * @param node
     * @param screen
     */	
    public ScreenTab(Node node, ScreenBase screen) {
        super(node);
        panel = new TabPanel();
        panel.setStyleName("ScreenTab");
        initWidget(panel);
        NodeList tabs = ((Element)node).getElementsByTagName("tab");
        for (int k = 0; k < tabs.getLength(); k++) {
            NodeList widgets = tabs.item(k).getChildNodes();
            for (int l = 0; l < widgets.getLength(); l++) {
                if (widgets.item(l).getNodeType() == Node.ELEMENT_NODE) {
                    Widget wid = ScreenWidget.loadWidget(widgets.item(l), screen);
                    ScrollPanel scroll = new ScrollPanel();
                    scroll.add(wid);
                    scroll.setWidth("100%");
                    //tabs can not have a constant or hard coded text
                    panel.add(scroll, tabs.item(k).getAttributes()
                                             .getNamedItem("text")
                                             .getNodeValue());
                }
            }
        }
        panel.selectTab(0);
        panel.addTabListener(screen);
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenTab(node, screen);
    }
    
    public void destroy(){
        panel = null;
        super.destroy();
    }

}
