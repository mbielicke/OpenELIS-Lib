/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseWheelListener;
import com.google.gwt.user.client.ui.MouseWheelListenerCollection;
import com.google.gwt.user.client.ui.MouseWheelVelocity;
import com.google.gwt.user.client.ui.SourcesMouseWheelEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.MenuPanel;

import java.util.ArrayList;

/**
 * ScreenMenuPanel will create either a vertical or horizontal list of widgets
 * from an XML string and is Styled as a ScreenMenuList.
 * @author tschmidt
 *
 */
public class ScreenMenuPanel extends ScreenWidget implements MouseListener, MouseWheelListener{
	/**
	 * Default XML Tag Name in XML Definition
	 */
	public static String TAG_NAME = "menuPanel";
	/**
	 * Widget wrapped by this class
	 */
    public MenuPanel panel;
    private class MenuVP extends VerticalPanel implements SourcesMouseWheelEvents {

        private MouseWheelListenerCollection listeners;
        
        public MenuVP() {
            sinkEvents(Event.ONMOUSEWHEEL);
        }
        
        public void onBrowserEvent(Event event) {
            // TODO Auto-generated method stub
            if(DOM.eventGetType(event) == event.ONMOUSEWHEEL){
                listeners.fireMouseWheelEvent(this, event);
            }
            super.onBrowserEvent(event);
        }
        
        public void addMouseWheelListener(MouseWheelListener listener) {
            if(listeners == null){
                listeners = new MouseWheelListenerCollection();
            }
            listeners.add(listener);
        }

        public void removeMouseWheelListener(MouseWheelListener listener) {
            if(listeners != null){
                listeners.remove(listener);
            }
        }
        
    }
    private MenuVP vp;
    public AbsolutePanel ap;
    public ScreenMenuItem activeItem;
    public boolean active;
    public ArrayList menuItems = new ArrayList();
    public FocusPanel up = new FocusPanel();
    public FocusPanel down = new FocusPanel();
    Timer timer;
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
        String layout = node.getAttributes().getNamedItem("layout").getNodeValue();
        panel = new MenuPanel(node.getAttributes().getNamedItem("layout").getNodeValue());
        createPanel(node);
        if(layout.equals("vertical")){
            vp = new MenuVP();
            vp.addMouseWheelListener(this);
            up.addMouseListener(this);
            up.setStyleName("MenuUp");
            up.addStyleName("MenuDisabled");
            up.setVisible(false);
            vp.add(up);
            ap = new AbsolutePanel();
            DOM.setStyleAttribute(ap.getElement(),"overflow","hidden");
            ap.add(panel);
            vp.add(ap);
            down.addMouseListener(this);
            down.setStyleName("MenuDown");
            down.setVisible(false);
            vp.add(down);
            initWidget(vp);
        }else{
            initWidget(panel);
        }
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
                Widget wid = ScreenWidget.loadWidget(items.item(i), screen);
                if(wid instanceof ScreenMenuItem){
                    ((ScreenMenuItem)wid).menuBar = this;
                    menuItems.add(wid);
                }
                panel.add(wid);
            }
        }
    }
    
    /**
     * This method loads the Widget from an AbstractField from the FormRPC
     */
    public void load(AbstractField field) {
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
    
    public void itemEnter(ScreenMenuItem item) {
        if(activeItem != null && activeItem != item){
            boolean lowest = false;
            ArrayList<ScreenMenuItem> closeList = new ArrayList<ScreenMenuItem>();
            closeList.add(activeItem);
            ScreenMenuItem close = activeItem;
            while(!lowest){
                if(close.child == null || close.child.activeItem == null || close.child.activeItem.pop == null)
                    lowest = true;
                else{
                    closeList.add(close.child.activeItem);
                    close = close.child.activeItem;
                }
            }
            for(int i = closeList.size() -1; i > -1; i--){
                closeList.get(i).closePopup();
            }
            item.createPopup();
        }else if(active)
            item.createPopup();
    }
    
    public void itemLeave(ScreenMenuItem item) {
        
    }
    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    public void onMouseEnter(final Widget sender) {
        timer = new Timer() {
            public void run() {
                if(sender == down){
                    if(ap.getWidgetTop(panel) <= ap.getOffsetHeight() - panel.getOffsetHeight()){
                        down.addStyleName("MenuDisabled");
                        cancel();
                    }else{
                        ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)-10);
                        up.removeStyleName("MenuDisabled");
                    }
                }
                if(sender == up){
                    if(ap.getWidgetTop(panel) >= 0){
                        up.addStyleName("MenuDisabled");
                        cancel();
                    }else{
                        ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)+10);
                        down.removeStyleName("MenuDisabled");
                    }
                }
            }
        };
        timer.scheduleRepeating(50);
    }
    
    public void onMouseLeave(Widget sender) {
        if(timer != null)
            timer.cancel();
    }
    
    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
    public void setSize(int top) {
        if(panel.getOffsetHeight() > Window.getClientHeight()- top){
            ap.setHeight((Window.getClientHeight()-top - 50)+"px");
            ap.setWidth(ap.getOffsetWidth()+"px");
            up.setVisible(true);
            down.setVisible(true);
        }else{
            ap.setHeight(panel.getOffsetHeight()+"px");
            ap.setWidth(panel.getOffsetWidth()+"px");
            ap.setWidgetPosition(panel, 0, 0);
            up.setVisible(false);
            down.setVisible(false);
        }
    }
    public void onMouseWheel(Widget sender, MouseWheelVelocity velocity) {
        if(velocity.isSouth() && down.getStyleName().indexOf("MenuDisabled") == -1){
            if(ap.getWidgetTop(panel) <= ap.getOffsetHeight() - panel.getOffsetHeight()){
                down.addStyleName("MenuDisabled");
            }else{
                ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)-10);
                up.removeStyleName("MenuDisabled");
            }
        }
        if(velocity.isNorth() && up.getStyleName().indexOf("MenuDisabled") == -1){
            if(ap.getWidgetTop(panel) >= 0){
                up.addStyleName("MenuDisabled");
            }else{
                ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)+10);
                down.removeStyleName("MenuDisabled");
            }
        }
        
    }

}
