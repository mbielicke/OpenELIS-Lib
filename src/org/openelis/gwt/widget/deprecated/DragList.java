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
package org.openelis.gwt.widget.deprecated;

import java.util.Iterator;
import java.util.Vector;

import org.openelis.gwt.screen.deprecated.ClassFactory;
import org.openelis.gwt.screen.deprecated.ScreenDragList;
import org.openelis.gwt.screen.deprecated.ScreenLabel;
import org.openelis.gwt.screen.deprecated.ScreenWidget;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.IndexedDropController;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * DragList is a widget that displays widgets in a vertical list
 * that can be dragged to other widgets on a screen.
 * 
 * @author tschmidt
 *
 */
public class DragList extends Composite {

    private VerticalPanel vp = new VerticalPanel();
    private ScrollPanel scroll = new ScrollPanel();
    public PickupDragController dragController = new PickupDragController(RootPanel.get(),false);
    public IndexedDropController dropController = new IndexedDropController(vp){
        @Override
        public void onDrop(DragContext context) {
            // TODO Auto-generated method stub
            Window.alert("dropping");
            super.onDrop(context);
        }
    };
    public Vector<String> targets = new Vector<String>();
    
    public DragList() {
        initWidget(scroll);
        scroll.setStyleName("DragContainer");
        scroll.setWidget(vp);
        dragController.setBehaviorDragProxy(true);
        dragController.registerDropController(dropController);
    }
    /**
     * Method used to add a widget to the list
     * @param wid
     */
    public void addItem(Widget wid){
        vp.add(wid);
        if(vp.getWidgetCount() % 2 == 1){
        	wid.addStyleName("AltTableRow");
        }else{
        	wid.addStyleName("TableRow");
        }
        vp.setCellWidth(wid, scroll.getOffsetWidth()+"px");
        	
    }
    /**
     * Handles the dropping of widget on to this widget
     * @param text
     * @param value
     */
    public void addDropItem(String text, Object value){
        ScreenLabel label = new ScreenLabel(text,value);
        //label.addMouseListener((MouseListener)ClassFactory.forName("ProxyListener"));
        label.sinkEvents(Event.MOUSEEVENTS);
        //label.setDropTargets(((ScreenDragList)getParent()).getDropTargets());
        label.setScreen(((ScreenDragList)getParent()).getScreen());
        addItem(label);
    }
    
    /**
     * This method takes an xml string that set the list of widgets
     * If using as part of ScreenDragList use its load(String xml) instead.
     * @param xml
     */
    public void setList(String xml){
        Document doc = XMLParser.parse(xml);
        Element root = doc.getDocumentElement();
        NodeList items = root.getElementsByTagName("item");
        for(int i = 0; i < items.getLength(); i++){
            if(items.item(i).getNodeType() == Node.ELEMENT_NODE){
                String text = items.item(i).getAttributes().getNamedItem("text").getNodeValue();
                String value = "";
                if(items.item(i).getAttributes().getNamedItem("value") != null)
                    value = items.item(i).getAttributes().getNamedItem("value").getNodeValue();
                ScreenLabel label = new ScreenLabel();
                ((Label)label.getWidget()).setText(text);
                label.setUserObject(value);
                //label.addMouseListener(listener);
                addItem(label);
            }
        }
    }
    
    public void setHeight(String height){
        scroll.setHeight(height);
    }
    
    public void setWidth(String width){
        scroll.setWidth(width); 
    }
    
    public void clear(){
        vp.clear();
    }
    
    public Iterator getIterator(){
        return vp.iterator();
    }
    
    public void removeItem(ScreenWidget wid){
        vp.remove(wid);
        
    }
    
    /**
     * Sets the list to be draggable or not
     * @param enabled
     */
    public void enable(boolean enabled){
        Iterator it = vp.iterator();
        while(it.hasNext()){
            ScreenWidget wid = (ScreenWidget)it.next();
            if(enabled)
                dragController.makeDraggable(wid);
            else
                dragController.makeNotDraggable(wid);
        }
    }
    
}