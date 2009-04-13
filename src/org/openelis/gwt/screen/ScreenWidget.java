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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesClickEvents;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.AppButton;
/**
 * ScreenWidget wraps a widget so that it can be displayed on a 
 * screen using the XML definition and provides base functionality for all
 * screen widgets such as Drag and Drop and Hover.  The base class adds
 * the following attributes to all extending classes.
 * 
 * key="string"
 * drag="string"
 * drop="string"
 * hover="string"
 * mouse="string"
 * targets="string,string..."
 * width="string"
 * height="string"
 * style="string"
 * value="string"
 * 
 * @author tschmidt
 *
 */
public class ScreenWidget extends SimplePanel implements
                                             SourcesMouseEvents,
                                             SourcesClickEvents {

    protected MouseListenerCollection mouseListeners;
    protected ClickListenerCollection clickListeners;
    /** 
     * userObject can be used to attach specific application
     * data to a screen widget.
     */
    private Object userObject;
    /** 
     * Reference back to the Screen this widget is displayed in
     */
    public ScreenBase screen;
    public String hoverStyle = "Hover";
    public String key;
    public boolean alwaysEnabled;
    public boolean alwaysDisabled;
    public boolean enabled;
   
    public ScreenWidget() {
    }

    /** 
     * Constructor which takes the xml node description of the widget. 
     * Should be called by the extending class to see if MouseEvents need to sunk
     * by the widget.
     * @param node
     */
    public ScreenWidget(Node node) {
        if (node.getAttributes().getNamedItem("mouse") != null || 
            node.getAttributes().getNamedItem("hover") != null) {
            sinkEvents(Event.MOUSEEVENTS);
        }
        if (node.getAttributes().getNamedItem("onPanelClick") != null)
            sinkEvents(Event.ONCLICK);
    }
    
    public void init(Node node, ScreenBase screen) {
        
    }
    
    /**
     * This stub should be overridden by the extending class.  It will be called
     * when the widget needs to be drawn by the screen.
     * @param node
     * @param screen
     * @return ScreenWidget
     */
    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        return this;
    }
    
    /**
     * This method is called to get a new instance of this widget to
     * be used by the Drag method when dragging by proxy.
     * @return ScreenWidget
     */
    public ScreenWidget getInstance() {
        ScreenWidget proxy = new ScreenWidget();
        proxy.sinkEvents(Event.MOUSEEVENTS);
        DOM.setInnerHTML(proxy.getElement(),
                         DOM.getInnerHTML(getWidget().getElement()));
        proxy.setStyleName(getStyleName());
        return proxy;
    }
    
    /**
     * This stub should be overridden by the extending class if it is an
     * input widget and needs to display data from the server.
     * @param field
     */
    public void load(AbstractField field) {
    }

    /**
     * This stub should be overridden by the extending class if it is an
     * input widget and needs to return data to the server. 
     * @param field
     */
    public void submit(AbstractField field) {
    }

    /**
     * This stub should be overridden by the extending class if it can
     * be disabled
     * @param enabled
     */
    public void enable(boolean enabled) {
        this.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * This stub should be overridden by the extending class if it can
     * accept focus
     * @param focused
     */
    public void setFocus(boolean focused) {
    }

    /**
     * This method needs to be called by the extending class and will set the widget that
     * is wrapped by this class. It must be set before setDefaults method is called.
     * @param widget
     */
    public void initWidget(Widget widget) {
        setWidget(widget);
    }

    /**
     * This method is called by the extending class to inspect the
     * node for any common attributes that can be applied to any 
     * ScreenWidget.
     * @param node
     * @param screen
     */
    public void setDefaults(Node node, ScreenBase screen) {
        this.screen = screen;
        if(node.getAttributes().getNamedItem("key") != null){
            key = node.getAttributes().getNamedItem("key").getNodeValue();
            screen.widgets.put(key, this);
        }
        if (node.getAttributes().getNamedItem("style") != null){
            String[] styles = node.getAttributes().getNamedItem("style").getNodeValue().split(",");
            getWidget().setStyleName(styles[0]);
            for(int i = 1; i < styles.length; i++){
                getWidget().addStyleName(styles[i]);
            }
        }
        if (node.getAttributes().getNamedItem("width") != null)
            getWidget().setWidth(node.getAttributes()
                                     .getNamedItem("width")
                                     .getNodeValue());
        if (node.getAttributes().getNamedItem("panelWidth") != null)
            setWidth(node.getAttributes()
                                     .getNamedItem("panelWidth")
                                     .getNodeValue());
        if (node.getAttributes().getNamedItem("height") != null)
            getWidget().setHeight(node.getAttributes()
                                      .getNamedItem("height")
                                      .getNodeValue());
        if (node.getAttributes().getNamedItem("tab") != null){
            screen.addTab(this, node.getAttributes()
                                           .getNamedItem("tab")
                                           .getNodeValue()
                                           .split(","));
            sinkEvents(Event.KEYEVENTS);
        }
        if (node.getAttributes().getNamedItem("value") != null){
            setUserObject(node.getAttributes()
                              .getNamedItem("value")
                              .getNodeValue());
            if(getWidget() instanceof AppButton){
                Window.alert(node.getAttributes()
                             .getNamedItem("value")
                             .getNodeValue());
            }
        }
        
        if(node.getAttributes().getNamedItem("mouse") != null){
            String[] listeners = node.getAttributes().getNamedItem("mouse").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    addMouseListener((MouseListener)screen);
                else
                    addMouseListener((MouseListener)ClassFactory.forName(listeners[i]));
            }
        }
        if (node.getAttributes().getNamedItem("hover") != null){
            hoverStyle = node.getAttributes().getNamedItem("hover").getNodeValue();
            addMouseListener((MouseListener)ClassFactory.forName("HoverListener"));
        }
        if (node.getAttributes().getNamedItem("tip") != null){
            setTitle(node.getAttributes().getNamedItem("tip").getNodeValue());
        }
        if (node.getAttributes().getNamedItem("alwaysEnabled") != null){
            if (node.getAttributes().getNamedItem("alwaysEnabled").getNodeValue().equals("true"))
                alwaysEnabled = true;
        }
        if (node.getAttributes().getNamedItem("alwaysDisabled") != null){
            if (node.getAttributes().getNamedItem("alwaysDisabled").getNodeValue().equals("true"))
                alwaysDisabled = true;
        }
        if (node.getAttributes().getNamedItem("onPanelClick") != null){
            String[] listeners = node.getAttributes().getNamedItem("onPanelClick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    addClickListener((ClickListener)screen);
                else
                    addClickListener((ClickListener)ClassFactory.forName(listeners[i]));
            }
        }
        if (node.getAttributes().getNamedItem("visible") != null){
            if(node.getAttributes().getNamedItem("visible").getNodeValue().equals("false"))
                getWidget().setVisible(false);
        }
        if (node.getAttributes().getNamedItem("shortcut") != null){
            String key = node.getAttributes().getNamedItem("shortcut").getNodeValue();
            screen.shortcut.put(key, getWidget());
        }
    }

    /**  
     * This method will add MouseListeners to this widget to be fired if any
     * MouseEvents are detected by this widget 
     */
    public void addMouseListener(MouseListener listener) {
        if (mouseListeners == null) {
            mouseListeners = new MouseListenerCollection();
        }
        mouseListeners.add(listener);
    }

    public void addClickListener(ClickListener listener) {
        if (clickListeners == null) {
            clickListeners = new ClickListenerCollection();
        }
        clickListeners.add(listener);
    }


    /**
     * This is an override of the Widget onBrowserEvent to make the SimplePanel 
     * respond to MouseEvents.
     */
    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEUP:
            case Event.ONMOUSEMOVE:
            case Event.ONMOUSEOVER:
            case Event.ONMOUSEOUT:
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
                break;
            case Event.ONCLICK:
                if (clickListeners != null) {
                    clickListeners.fireClick(this);
                }
                break;
        }
    }

    /**
     * This method will remove MouseListeners that have been added to the widget 
     */
    public void removeMouseListener(MouseListener listener) {
        if (mouseListeners != null) {
            mouseListeners.remove(listener);
        }
    }
    
    public void removeClickListener(ClickListener listener) {
        if (clickListeners != null) {
            clickListeners.remove(listener);
        }
    }

    /**
     * This method will set the Application data for this widget in the userObject field
     * @param obj
     */
    public void setUserObject(Object obj) {
        userObject = obj;
    }

    /** 
     * Getter for userObject field
     * @return
     */
    public Object getUserObject() {
        return userObject;
    }
    
    /**
     * Getter for the Screen that contains this Widget
     * @return
     */
    public ScreenBase getScreen(){
        return screen;
    }
    
    /** 
     * Setter for the Screen that contains this widget.
     * @param screen
     */
    public void setScreen(ScreenBase screen){
        this.screen = screen; 
    }
    
    public void destroy() {
        mouseListeners = null;
        userObject = null;
        screen = null;
        clear();
    }
    
    public static Widget loadWidget(Node node, ScreenBase screen){
        Node input = null;
        if (node.getNodeName().equals("widget")) {
            NodeList inputList = node.getChildNodes();
            for (int m = 0; m < inputList.getLength(); m++) {
                if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                    input = inputList.item(m);
                    m = 100;
                }
            }
        } else
            input = node;
        Widget wid = ScreenBase.createWidget(input, screen);
        if(node.getNodeName().equals("widget")){
            NodeList queryList = ((Element)node).getElementsByTagName("query");
            if(queryList.getLength() > 0){
                NodeList inputList = queryList.item(0).getChildNodes();
                for (int m = 0; m < inputList.getLength(); m++) {
                    if (inputList.item(m).getNodeType() == Node.ELEMENT_NODE) {
                        input = inputList.item(m);
                        m = 100;
                    }
                }
                Widget query = ScreenBase.createWidget(input, screen);
                ((ScreenInputWidget)wid).setQueryWidget((ScreenInputWidget)query);
            }
        }
        return wid;
    }
    
    public boolean equals(Object obj) {
        if(getWidget() == null)
            return false;
        return getWidget().equals(obj);
    }
    
}
