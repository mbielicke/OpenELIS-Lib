package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.client.widget.DragList;
import org.openelis.gwt.common.AbstractField;
/**
 * ScreenDragList wraps a DragList Widget to be displayed on a Screen
 * @author tschmidt
 *
 */
public class ScreenDragList extends ScreenWidget {
    /**
     * Default XML Tag Name for XML Definition and WidgetMap
     */
	public static String TAG_NAME = "draglist";
	/**
	 * Widget wrapped by this class
	 */
    private DragList list;
	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenDragList() {
        
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
    public ScreenDragList(Node node, final Screen screen){
        super(node);
        list = new DragList() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        screen.doTab(event, this);
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };
        initWidget(list);        
        list.setStyleName("ScreenDragList");
        setDefaults(node, screen);
        if(node.hasChildNodes()){
            createList((Element)node);
        }
    }
    
    public ScreenWidget getInstance(Node node, Screen screen){
        return new ScreenDragList(node,screen);
    }
    /**
     * Loads DragList from an AbstractField returned from the server
     * in FormRPC
     */
    public void load(AbstractField field) {
        // TODO Auto-generated method stub
        Document doc = XMLParser.parse((String)field.getValue());
        list.clear();
        createList(doc.getDocumentElement());
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
                ScreenLabel label = new ScreenLabel(items.item(i),screen);
                label.addMouseListener((MouseListener)Screen.getWidgetMap().get("ProxyListener"));
                label.sinkEvents(Event.MOUSEEVENTS);
                label.setDropTargets(getDropTargets());
                list.addItem(label);
            }
        }
    }
    
    public void enable(boolean enabled){
        list.enable(enabled);
    }
    
}
