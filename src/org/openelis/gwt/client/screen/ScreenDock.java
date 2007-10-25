package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * ScreenDock wraps a GWT DockPanel to display widgets on the Screen
 * with Dock Layout.
 * @author tschmidt
 *
 */
public class ScreenDock extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "panel-dock";
	/**
	 * Widget that is wrapped by this class
	 */
	private DockPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenDock() {

    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * <&lt;panel layout="dock" spacing="int"&gt;>
     *   &lt;widget dir="north,south,east,west,center"&gt;
     *      ....
     *   &lt;/widget&gt;
     * &lt;/panel&gt;
     *      
     * @param node
     * @param screen
     */
    public ScreenDock(Node node, Screen screen) {
        super(node);
        panel = new DockPanel();
        panel.addStyleName("ScreenDock");
        initWidget(panel);
        if (node.getAttributes().getNamedItem("spacing") != null)
            panel.setSpacing(Integer.parseInt(node.getAttributes()
                                                  .getNamedItem("spacing")
                                                  .getNodeValue()));
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
                String dir = widgets.item(k)
                                    .getAttributes()
                                    .getNamedItem("dir")
                                    .getNodeValue();
                if (dir.equals("north"))
                    panel.add(wid, DockPanel.NORTH);
                else if (dir.equals("south"))
                    panel.add(wid, DockPanel.SOUTH);
                else if (dir.equals("east"))
                    panel.add(wid, DockPanel.EAST);
                else if (dir.equals("west"))
                    panel.add(wid, DockPanel.WEST);
                else if (dir.equals("center"))
                    panel.add(wid, DockPanel.CENTER);
                if (widgets.item(k).getAttributes().getNamedItem("halign") != null) {
                    String align = widgets.item(k)
                                          .getAttributes()
                                          .getNamedItem("align")
                                          .getNodeValue();
                    if (align.equals("right"))
                        panel.setCellHorizontalAlignment(wid,
                                                         HasAlignment.ALIGN_RIGHT);
                    if (align.equals("left"))
                        panel.setCellHorizontalAlignment(wid,
                                                         HasAlignment.ALIGN_LEFT);
                    if (align.equals("center"))
                        panel.setCellHorizontalAlignment(wid,
                                                         HasAlignment.ALIGN_CENTER);
                }
                if (widgets.item(k).getAttributes().getNamedItem("valign") != null) {
                    String align = widgets.item(k)
                                          .getAttributes()
                                          .getNamedItem("valign")
                                          .getNodeValue();
                    if (align.equals("top"))
                        panel.setCellVerticalAlignment(wid,
                                                       HasAlignment.ALIGN_TOP);
                    if (align.equals("middle"))
                        panel.setCellVerticalAlignment(wid,
                                                       HasAlignment.ALIGN_MIDDLE);
                    if (align.equals("bottom"))
                        panel.setCellVerticalAlignment(wid,
                                                       HasAlignment.ALIGN_BOTTOM);
                }
               
            }
        }
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        return new ScreenDock(node, screen);
    }
    
    public void destroy() {
        panel = null;
        super.destroy();
    }

}
