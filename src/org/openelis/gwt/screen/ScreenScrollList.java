package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.widget.ScrollList;
/**
 * ScreenDragList wraps a DragList Widget to be displayed on a Screen
 * @author tschmidt
 *
 */
public class ScreenScrollList extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "scrolllist";
	/**
	 * Widget wrapped by this class
	 */
    private ScrollList list;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenScrollList() {
        
    }
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget.
     * <br/><br/>
     * &lt;draglist key="string" drag="string" drop="string" targets="string,string..."/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenScrollList(Node node, final ScreenBase screen){
        super(node);
        final ScreenScrollList sl = this;
        list = new ScrollList() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, sl);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        list.setMaxRows(Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue()));
        if(node.getAttributes().getNamedItem("cellHeight") != null){
            list.setCellHeight(Integer.parseInt(node.getAttributes().getNamedItem("cellHeight").getNodeValue()));
        }
        if(node.getAttributes().getNamedItem("targets") != null)
            list.drag = true;
        if(node.getAttributes().getNamedItem("drop") != null)
            list.drop = true;
        if(node.getAttributes().getNamedItem("multi") != null){
            if(node.getAttributes().getNamedItem("multi").getNodeValue().equals("true")){
                list.multi = true;
            }
        }
        Node widthsNode = ((Element)node).getElementsByTagName("widths").item(0);
        Node headersNode = ((Element)node).getElementsByTagName("headers").item(0);
        
        if(headersNode != null)
            list.setHeaders(getHeaders(headersNode));
        
        if(widthsNode !=  null)
            list.setCellWidths(getWidths(widthsNode));
        
        if(node.getAttributes().getNamedItem("maxHeight") != null){
            if(node.getAttributes().getNamedItem("maxHeight").getNodeValue().equals("true"))
                list.maxHeight = true;
        }
        
        initWidget(list);        
        
        list.setStyleName("ScreenDragList");
        setDefaults(node, screen);
        if(node.hasChildNodes()){
            createList((Element)node);
        }
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen){
        return new ScreenScrollList(node,screen);
    }
    /**
     * Loads DragList from an AbstractField returned from the server
     * in FormRPC
     */
    public void load(AbstractField field) {
        // TODO Auto-generated method stub
        if(field instanceof StringField){
            Document doc = XMLParser.parse((String)field.getValue());
            list.clear();
            createList(doc.getDocumentElement());
        }else{
            list.setDataModel((DataModel)field.getValue());
        }
            
    }

    /**
     * Loads the DragList from string representing XML.
     * @param xml
     */
    public void load(String xml) {
        Document doc = XMLParser.parse(xml);
        list.clear();
        createList(doc.getDocumentElement());
    }
    
    /**
     * Creates the list from XML
     * @param node
     */
    private void createList(Element node){
        NodeList items = node.getElementsByTagName("item");
        for(int i = 0; i < items.getLength(); i++){
            if(items.item(i).getNodeType() == Node.ELEMENT_NODE){
                StringObject so = new StringObject(items.item(i).getAttributes().getNamedItem("value").getNodeValue()); 
                list.addDropItem(items.item(i).getAttributes().getNamedItem("text").getNodeValue(),so);
            }
        }
    }
    
    public void enable(boolean enabled){
        list.enable(enabled);
    }
    
    public void destroy() {
        list = null;
        super.destroy();
    }
    
    public int[] getWidths(Node node){
     String[] widths = node.getFirstChild()
        .getNodeValue()
        .split(",");
    int[] width = new int[widths.length];
    for (int i = 0; i < widths.length; i++) {
        width[i] = Integer.parseInt(widths[i]);
    }
    return width;
   }
    
    public String[] getHeaders(Node node){
        return node.getFirstChild()
                .getNodeValue()
                .split(",");
    } 
}
