package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * ScreenVertical wraps a GWT VerticalPanel to display widgets on a 
 * Screen in a vertical Column.
 * @author tschmidt
 *
 */

public class ScreenVertical extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "panel-vertical";
	/**
	 * Widget wrapped by this class
	 */
    private VerticalPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenVertical() {
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;panel layout="vertical" key="string" spacing="int"*gt;
     *   &lt;widget&gt;
     *     ...
     *   &lt;/widget&gt;
     * &lt;/panel&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenVertical(Node node, Screen screen) {
        super(node);
        panel = new VerticalPanel();
        if (node.getAttributes().getNamedItem("spacing") != null){
            panel.setSpacing(Integer.parseInt(node.getAttributes()
                                                  .getNamedItem("spacing")
                                                  .getNodeValue()));
        }
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
                addWidget(widgets.item(k), wid);
            }
        }
        if(node.getAttributes().getNamedItem("overflow") != null){
        	AbsolutePanel ap = new AbsolutePanel();
        	ap.add(panel);
        	initWidget(ap);
        	DOM.setStyleAttribute(ap.getElement(),"overflow",node.getAttributes().getNamedItem("overflow").getNodeValue());
        }else
        	initWidget(panel);
        panel.setStyleName("ScreenPanel");
        setDefaults(node, screen);
    }

    public void addWidget(Node widget, Widget wid) {
        panel.add(wid);
        if (widget.getAttributes().getNamedItem("halign") != null) {
            String align = widget.getAttributes()
                                 .getNamedItem("halign")
                                 .getNodeValue();
            if (align.equals("right"))
                panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_RIGHT);
            if (align.equals("left"))
                panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_LEFT);
            if (align.equals("center"))
                panel.setCellHorizontalAlignment(wid, HasAlignment.ALIGN_CENTER);
        }
        if (widget.getAttributes().getNamedItem("valign") != null) {
            String align = widget.getAttributes()
                                 .getNamedItem("valign")
                                 .getNodeValue();
            if (align.equals("top"))
                panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_TOP);
            if (align.equals("middle"))
                panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_MIDDLE);
            if (align.equals("bottom"))
                panel.setCellVerticalAlignment(wid, HasAlignment.ALIGN_BOTTOM);
        }
        /*if (widget.getAttributes().getNamedItem("width") != null) {
            panel.setCellWidth(wid, widget.getAttributes().getNamedItem("width").getNodeValue());
        }
        if (widget.getAttributes().getNamedItem("height") != null) {
            panel.setCellWidth(wid, widget.getAttributes().getNamedItem("height").getNodeValue());
        }
        */
    }

    public ScreenWidget getInstance(Node node, Screen screen) {
        return new ScreenVertical(node, screen);
    }
    
    public void destroy() {
        panel = null;
    }

}
