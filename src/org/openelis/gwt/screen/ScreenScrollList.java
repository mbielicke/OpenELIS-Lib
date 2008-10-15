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
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableLabel;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;
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
        int maxRows = (Integer.parseInt(node.getAttributes().getNamedItem("maxRows").getNodeValue()));
        int cellHeight = 18;
        if(node.getAttributes().getNamedItem("cellHeight") != null){
            cellHeight = (Integer.parseInt(node.getAttributes().getNamedItem("cellHeight").getNodeValue()));
        }
        boolean drag = false;
        if(node.getAttributes().getNamedItem("targets") != null)
            drag = true;
        boolean drop = false;
        if(node.getAttributes().getNamedItem("drop") != null)
            drop = true;
        boolean multi = false;
        if(node.getAttributes().getNamedItem("multi") != null){
            if(node.getAttributes().getNamedItem("multi").getNodeValue().equals("true")){
                multi = true;
            }
        }
        VerticalScroll showScroll = VerticalScroll.NEEDED;
        if(node.getAttributes().getNamedItem("showScroll") != null){
            showScroll = VerticalScroll.valueOf((node.getAttributes().getNamedItem("showScroll").getNodeValue()));
        }
        Node widthsNode = ((Element)node).getElementsByTagName("widths").item(0);
        Node headersNode = ((Element)node).getElementsByTagName("headers").item(0);
        
        ArrayList<TableColumnInt> columns = new ArrayList<TableColumnInt>(); 
        if(widthsNode != null) {
            String[] widths = widthsNode.getFirstChild()
            .getNodeValue()
            .split(",");
            for (String wid : widths) {
                TableColumn col = new TableColumn();
                col.setCurrentWidth(Integer.parseInt(wid));
                col.setColumnWidget(new TableLabel());
                columns.add(col);
            }
        }
        boolean showHeader = false;
        if(headersNode != null){
            showHeader = true;
            String[] headerNames = headersNode.getFirstChild()
            .getNodeValue()
            .split(",");
            for(int i = 0; i < headerNames.length; i++){
                columns.get(i).setHeader(headerNames[i].trim());
            }                    
        }
        

        
        list = new ScrollList(columns,maxRows,"auto",null,showHeader,showScroll); 
        
        if(node.getAttributes().getNamedItem("maxHeight") != null){
            if(node.getAttributes().getNamedItem("maxHeight").getNodeValue().equals("true"))
                list.maxHeight = true;
        }
        
        list.setStyleName("ScreenDragList");
        setWidget(list);
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
            list.model.load((DataModel)field.getValue());
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
