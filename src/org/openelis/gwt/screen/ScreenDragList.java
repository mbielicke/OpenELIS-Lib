/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.DragList;
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
    public ScreenDragList(Node node, final ScreenBase screen){
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        final ScreenDragList sd = this;
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            list = (DragList)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            list = new DragList();
        initWidget(list);        
        list.setStyleName("ScreenDragList");
        setDefaults(node, screen);
        if(node.hasChildNodes()){
            createList((Element)node);
        }
    }
    
    public ScreenWidget getInstance(Node node, ScreenBase screen){
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
                label.addMouseListener((MouseListener)ClassFactory.forName("ProxyListener"));
                label.sinkEvents(Event.MOUSEEVENTS);
                label.setDropTargets(getDropTargets());
                list.addItem(label);
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
    
}
