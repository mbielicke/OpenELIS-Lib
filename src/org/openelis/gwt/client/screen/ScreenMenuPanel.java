package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.AbstractField;

/**
 * ScreenMenuPanel will create either a vertical or horizontal list of widgets
 * from an XML string and is Styled as a ScreenMenuList.
 * @author tschmidt
 *
 */
public class ScreenMenuPanel extends ScreenWidget {
	/**
	 * Default XML Tag Name in XML Definition
	 */
	public static String TAG_NAME = "menu";
	/**
	 * Widget wrapped by this class
	 */
    private CellPanel panel;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenMenuPanel() {
        
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &lt;menu vetical="boolean" key="string"&gt;
     *    &lt;widget/&gt;
     *    ......
     * &lt;menu/&gt;
     *   
     * @param node
     * @param screen
     */	
    public ScreenMenuPanel(Node node, ScreenBase screen){
        super(node);
        this.screen = screen;
        if(node.getAttributes().getNamedItem("vertical") != null)
            panel = new VerticalPanel();
        else
            panel = new HorizontalPanel();
        createPanel(node);
        initWidget(panel);
        panel.setStyleName("ScreenMenuList");
        setDefaults(node, screen);
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen){
        return new ScreenMenuPanel(node,screen);
    }
    
    /**
     * createPanel creates the MenuPanel from an XML node 
     * @param node
     */
    public void createPanel(Node node){
        NodeList items = node.getChildNodes();
        for (int i = 0; i < items.getLength(); i++) {
            if (items.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = ScreenBase.getWidgetMap().getWidget(items.item(i), screen);
                panel.add(wid);
            }
        }
    }
    
    /**
     * This method loads the Widget from an AbstractField from the FormRPC
     */
    public void load(AbstractField field) {
        // TODO Auto-generated method stub
        Document doc = XMLParser.parse((String)field.getValue());
        panel.clear();
        createPanel(doc.getDocumentElement());
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
    
    public void destroy() {
        panel = null;
        super.destroy();
    }

}
