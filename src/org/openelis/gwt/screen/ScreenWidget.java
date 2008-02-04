package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DragListenerCollection;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.dnd.DropListenerCollection;
import com.google.gwt.user.client.dnd.SourcesDragEvents;
import com.google.gwt.user.client.dnd.SourcesDropEvents;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;

import java.util.Vector;
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
                                             SourcesDropEvents,
                                             SourcesMouseEvents,
                                             SourcesDragEvents {

    private DropListenerCollection dropListeners;
    private DragListenerCollection dragListeners;
    private MouseListenerCollection mouseListeners;
    /** 
     * userObject can be used to attach specific application
     * data to a screen widget.
     */
    private Object userObject;
    /**
     * A list of available drop targets for a widget if it uses 
     * Drag and Drop
     */
    private Vector dropTargets = new Vector();
    /** 
     * Reference back to the Screen this widget is displayed in
     */
    protected ScreenBase screen;
    public String hoverStyle = "Hover";
    public String key;
   
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
            node.getAttributes().getNamedItem("drag") != null  ||
            node.getAttributes().getNamedItem("drop") != null ||
            node.getAttributes().getNamedItem("hover") != null) {
            sinkEvents(Event.MOUSEEVENTS);
        }
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
     * This method will add DropListeners to be fired if any Drop events are detected on the Widget.
     */
    public void addDropListener(DropListener listener) {
        if (dropListeners == null) {
            dropListeners = new DropListenerCollection();
        }
        dropListeners.add(listener, this);
    }

    /** 
     * This method will DropListeners from this widget.
     */
    public void removeDropListener(DropListener listener) {
        if (dropListeners != null) {
            dropListeners.remove(listener);
        }
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
        if (node.getAttributes().getNamedItem("style") != null)
            getWidget().addStyleName(node.getAttributes()
                                         .getNamedItem("style")
                                         .getNodeValue());
        if (node.getAttributes().getNamedItem("width") != null)
            getWidget().setWidth(node.getAttributes()
                                     .getNamedItem("width")
                                     .getNodeValue());
        if (node.getAttributes().getNamedItem("height") != null)
            getWidget().setHeight(node.getAttributes()
                                      .getNamedItem("height")
                                      .getNodeValue());
        if (node.getAttributes().getNamedItem("tab") != null)
            screen.addTab(getWidget(), node.getAttributes()
                                           .getNamedItem("tab")
                                           .getNodeValue()
                                           .split(","));
        if (node.getAttributes().getNamedItem("value") != null)
            setUserObject(node.getAttributes()
                              .getNamedItem("value")
                              .getNodeValue());
        if (node.getAttributes().getNamedItem("drop") != null) {
            String listener = node.getAttributes()
                                  .getNamedItem("drop")
                                  .getNodeValue();
            if (listener.equals("this"))
                addDropListener(screen);
            else {
                addDropListener((DropListener)ScreenBase.getWidgetMap()
                                                    .get(listener));
            }
        }
        if (node.getAttributes().getNamedItem("drag") != null) {
            String listener = node.getAttributes()
                                  .getNamedItem("drag")
                                  .getNodeValue();
            if (listener.equals("this"))
                addDragListener(screen);
            else {
                addDragListener((DragListener)ScreenBase.getWidgetMap()
                                                    .get(listener));
            }
        }
        if (node.getAttributes().getNamedItem("mouse") != null) {
            String listener = node.getAttributes()
                                  .getNamedItem("mouse")
                                  .getNodeValue();
            if (listener.equals("this"))
                addMouseListener(screen);
            else {
                addMouseListener((MouseListener)ScreenBase.getWidgetMap()
                                                      .get(listener));
            }
        }
        if (node.getAttributes().getNamedItem("targets") != null) {
            String targets[] = node.getAttributes()
                                  .getNamedItem("targets")
                                  .getNodeValue().split(",");
            for(int i = 0; i < targets.length; i++){
                    dropTargets.add(targets[i]);
            }
        }
        if (node.getAttributes().getNamedItem("hover") != null){
            hoverStyle = node.getAttributes().getNamedItem("hover").getNodeValue();
            addMouseListener((MouseListener)ScreenBase.getWidgetMap().get("HoverListener"));
        }
        if (node.getAttributes().getNamedItem("tip") != null){
            setTitle(node.getAttributes().getNamedItem("tip").getNodeValue());
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

    /** 
     * This method will add DragListeners to this widget to be fired if any Drag events 
     * are detected on this widget.
     */
    public void addDragListener(DragListener listener) {
        if (dragListeners == null) {
            dragListeners = new DragListenerCollection();
        }
        dragListeners.add(listener, this);
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

    /**
     * This method will remove DragListeners that have been added to the Widget.
     */
    public void removeDragListener(DragListener listener) {
        if (dragListeners != null) {
            dragListeners.remove(listener);
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
     * Makes the DropTargets vector available publicly
     * @return
     */
    public Vector getDropTargets() {
        return dropTargets;
    }
    
    /**
     * Returns a Vector of widgets pulled from the Screen using the Keys stored in the 
     * DropTargets vector.
     * @return
     */
    public Vector getDropMap(){
        Vector dropMap = new Vector();
        for(int i = 0; i < dropTargets.size(); i++){
            String target = (String)dropTargets.get(i);
            DropListenerCollection dropColl = ((ScreenWidget)screen.widgets.get(target)).getDropListeners();
            dropMap.add(dropColl);
        }
        return dropMap;
    }
   
    /**
     * Set the Vector of DropTargets representing the keys to the widgets that this
     * widget is allowed to drop on.
     */
    public void setDropTargets(Vector targets){
        dropTargets = targets;
    }
    
    /**
     * Getter for the DropListener Collection
     * @return
     */
    public DropListenerCollection getDropListeners(){
        return dropListeners;
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
        dropListeners = null;
        dragListeners = null;
        mouseListeners = null;
        userObject = null;
        dropTargets = null;
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
        Widget wid = ScreenBase.getWidgetMap().getWidget(input, screen);
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
                Widget query = ScreenBase.getWidgetMap().getWidget(input, screen);
                ((ScreenInputWidget)wid).setQueryWidget((ScreenInputWidget)query);
            }
        }
        return wid;
    }
    
    
}
