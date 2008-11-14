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

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.screen.ScreenWidget;
import org.openelis.gwt.widget.MenuItem;
import org.openelis.gwt.widget.MenuPanel;

public class ScreenMenuItem extends ScreenWidget {
    
    private MenuItem item;
    private Node popupNode;
    private Widget wid;
    public PopupPanel pop;
    private boolean popClosed;
    public ScreenMenuPanel menuBar;
    private boolean cursorOn;
    private ScreenMenuItem parent;
    public ScreenMenuPanel child;
    public ScreenMenuPanel menuItemsPanel;
    public static final String TAG_NAME = "menuItem";
    public String label;

    
    public ScreenMenuItem() {
        super();
    }
    
    public ScreenMenuItem(String icon, Widget wid, String description) {
        item = new MenuItem(icon,wid,description);
        initWidget(item);
        sinkEvents(Event.MOUSEEVENTS);
        addMouseListener((MouseListener)ClassFactory.forName("HoverListener"));
    }
    
    public ScreenMenuItem(String label) {
        wid = MenuItem.createTableHeader("", new Label(label));
        item = new MenuItem(wid);
        initWidget(item);
        sinkEvents(Event.MOUSEEVENTS);
    }
    
    public ScreenMenuItem(Node node, ScreenBase screen){
        super(node);
        wid = null;
        if(((Element)node).getElementsByTagName("menuDisplay").getLength() > 0 &&  ((Element)node).getElementsByTagName("menuDisplay").item(0).getParentNode().equals(node)){
            NodeList displayList = ((Element)node).getElementsByTagName("menuDisplay").item(0).getChildNodes();
            int i = 0; 
            while(displayList.item(i).getNodeType() != Node.ELEMENT_NODE)
                i++;
            wid = ScreenWidget.loadWidget(displayList.item(i), screen);
            item = new MenuItem(wid);
        }else if(node.getAttributes().getNamedItem("header") != null){
            wid = MenuItem.createTableHeader("", new Label(node.getAttributes().getNamedItem("label").getNodeValue()));
            item = new MenuItem(wid);
        }else{
            item = new MenuItem(node.getAttributes().getNamedItem("icon").getNodeValue(), 
                                         node.getAttributes().getNamedItem("label").getNodeValue(), 
                                         node.getAttributes().getNamedItem("description").getNodeValue());
            label = node.getAttributes().getNamedItem("label").getNodeValue();
        }

        if (node.getAttributes().getNamedItem("class") != null){
            item.objClass = node.getAttributes().getNamedItem("class").getNodeValue();
        }
        if (node.getAttributes().getNamedItem("args") != null && !"".equals(node.getAttributes().getNamedItem("args").getNodeValue())){
            String[] argStrings = node.getAttributes().getNamedItem("args").getNodeValue().split(",");
            item.args = new Object[argStrings.length];
            for(int i = 0; i < argStrings.length; i++){
                item.args[i] = new StringObject(argStrings[i]);
            }
        }
        
        if (node.getAttributes().getNamedItem("onClick") != null){
            String[] listeners = node.getAttributes().getNamedItem("onClick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    item.addClickListener((ClickListener)screen);
                else
                    item.addClickListener((ClickListener)ClassFactory.forName(listeners[i]));
            }
        }
        popupNode = ((Element)node).getElementsByTagName("menuPanel").item(0);
        if(node.getAttributes().getNamedItem("enabled") != null){
            if(node.getAttributes().getNamedItem("enabled").getNodeValue().equals("true"))
                enable(true);
            else
                enable(false);
        }else
            enable(true);
        
        if(popupNode != null){
            item.menuItemsPanel = (MenuPanel)((ScreenMenuPanel)ScreenWidget.loadWidget(popupNode, screen)).getWidget();
            if(popupNode.getAttributes().getNamedItem("position") != null)
                item.popPosition = MenuItem.PopPosition.valueOf(popupNode.getAttributes().getNamedItem("position").getNodeValue().toUpperCase());
        }

        if(node.getAttributes().getNamedItem("key") != null){
            item.key = node.getAttributes().getNamedItem("key").getNodeValue();
        }
        
        initWidget(item);
        setDefaults(node,screen);
        sinkEvents(Event.MOUSEEVENTS);
    }
    
    public void enable(boolean enabled){
        item.enable(enabled);
    }
    
    public ScreenWidget getInstance() {
        ScreenLabel labelProxy = new ScreenLabel(label,null);
        labelProxy.sinkEvents(Event.MOUSEEVENTS);
        return labelProxy;
        
    }

}
