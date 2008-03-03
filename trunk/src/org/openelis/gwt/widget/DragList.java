package org.openelis.gwt.widget;

import java.util.Iterator;

import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.screen.ScreenDragList;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.screen.ScreenWidget;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
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
    private ProxyListener listener = new ProxyListener();
    
    public DragList() {
        initWidget(scroll);
        scroll.setStyleName("DragContainer");
        scroll.setWidget(vp);
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
        label.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("ProxyListener"));
        label.sinkEvents(Event.MOUSEEVENTS);
        label.setDropTargets(((ScreenDragList)getParent()).getDropTargets());
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
                label.addMouseListener(listener);
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
            wid.removeMouseListener((MouseListener)ScreenBase.getWidgetMap().get("ProxyListener"));
            if(enabled){
                wid.addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("ProxyListener"));
            }
        }
    }
    
}
