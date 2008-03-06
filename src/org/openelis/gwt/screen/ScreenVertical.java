package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

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
    
    public ScreenVertical(ScreenBase screen, String key) {
    	panel = new VerticalPanel();
    	initWidget(panel);
    	screen.widgets.put(key, this);
    	
    }
    public ScreenVertical(Node node, ScreenBase screen) {
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
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                addWidget(widgets.item(k), wid);
            }
        }
        if(node.getAttributes().getNamedItem("overflow") != null ||
           node.getAttributes().getNamedItem("overflowX") != null || 
           node.getAttributes().getNamedItem("overflowY") != null){
        	AbsolutePanel ap = new AbsolutePanel();
        	ap.add(panel);
        	initWidget(ap);
            if(node.getAttributes().getNamedItem("overflow") != null)
                DOM.setStyleAttribute(ap.getElement(),"overflow",node.getAttributes().getNamedItem("overflow").getNodeValue());
            if(node.getAttributes().getNamedItem("overflowX") != null)
                DOM.setStyleAttribute(ap.getElement(),"overflow-x",node.getAttributes().getNamedItem("overflowX").getNodeValue());
            if(node.getAttributes().getNamedItem("overflowY") != null)
                DOM.setStyleAttribute(ap.getElement(),"overflow-y",node.getAttributes().getNamedItem("overflowY").getNodeValue());
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
    
    /**
     * createPanel creates the vertical panel from an XML node 
     * @param node
     */
    public void createPanel(Node node){
        NodeList widgets = node.getChildNodes();
        for (int k = 0; k < widgets.getLength(); k++) {
            if (widgets.item(k).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenWidget.loadWidget(widgets.item(k), screen);
                addWidget(widgets.item(k), wid);
            }
        }
    }

    /**
     * This method loads the Widget from a String
     * @param xml
     */
    public void load(String xml) {
        Document doc = XMLParser.parse(xml);
        panel.clear();
        createPanel(doc.getDocumentElement());
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return new ScreenVertical(node, screen);
    }
    
    public void clear(){
    	panel.clear();
    }
    
    public void destroy() {
        panel = null;
    }

}
