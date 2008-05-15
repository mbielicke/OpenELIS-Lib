package org.openelis.gwt.screen;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.widget.MenuItem;

public class ScreenMenuItem extends ScreenWidget implements MouseListener, ClickListener, PopupListener {
    
    private MenuItem item;
    private Node popupNode;
    private Widget wid;
    public PopupPanel pop;
    private boolean popClosed;
    public ScreenMenuPanel menuBar;
    private boolean cursorOn;
    private ScreenMenuItem parent;
    public ScreenMenuPanel child;
    public static final String TAG_NAME = "menuItem";
    public String label;
    public String objClass;
    public DataObject[] args;
    
    public ScreenMenuItem() {
        super();
    }
    
    public ScreenMenuItem(Node node, ScreenBase screen){
        super(node);
        wid = null;
        if(((Element)node).getElementsByTagName("menuDisplay").getLength() > 0){
            NodeList displayList = ((Element)node).getElementsByTagName("menuDisplay").item(0).getChildNodes();
            int i = 0; 
            while(displayList.item(i).getNodeType() != Node.ELEMENT_NODE)
                i++;
            wid = ScreenWidget.loadWidget(displayList.item(i), screen);
        }else{
            wid = MenuItem.createDefault(node.getAttributes().getNamedItem("icon").getNodeValue(), 
                                         node.getAttributes().getNamedItem("label").getNodeValue(), 
                                         node.getAttributes().getNamedItem("description").getNodeValue());
            label = node.getAttributes().getNamedItem("label").getNodeValue();
        }
        if (node.getAttributes().getNamedItem("onClick") != null){
            String[] listeners = node.getAttributes().getNamedItem("onClick").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this"))
                    addClickListener((ClickListener)screen);
                else
                    addClickListener((ClickListener)ClassFactory.forName(listeners[i]));
            }
        }
        if (node.getAttributes().getNamedItem("class") != null){
            objClass = node.getAttributes().getNamedItem("class").getNodeValue();
        }
        if (node.getAttributes().getNamedItem("args") != null){
            String[] argStrings = node.getAttributes().getNamedItem("args").getNodeValue().split(",");
            args = new DataObject[argStrings.length];
            for(int i = 0; i < argStrings.length; i++){
                args[i] = new StringObject(argStrings[i]);
            }
        }
        item = new MenuItem(wid);
        popupNode = ((Element)node).getElementsByTagName("menuPanel").item(0);
        if(node.getAttributes().getNamedItem("enabled") != null){
            if(node.getAttributes().getNamedItem("enabled").getNodeValue().equals("true"))
                enable(true);
            else
                enable(false);
        }else
            enable(true);

        initWidget(item);
        setDefaults(node,screen);
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        menuBar.itemEnter(this);
        cursorOn = true;
    }

    public void onMouseLeave(Widget sender) {
        menuBar.itemLeave(this);
        cursorOn = false;
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onClick(Widget sender) {
        //Window.alert("click");
        if(popupNode != null){
            if(popClosed)
                popClosed = false;
            else
                createPopup();
            mouseListeners.fireMouseEnter(sender);
            return;
        }
        if(menuBar.getParent() instanceof PopupPanel)
            ((PopupPanel)menuBar.getParent()).hide();
        mouseListeners.fireMouseLeave(sender);
    }

    public void onPopupClosed(PopupPanel sender, boolean autoClosed) {
        if(cursorOn)
            popClosed = true;
        if(DOM.getElementProperty(sender.getElement(), "closeAll").equals("true")){
                if(menuBar.getParent() instanceof PopupPanel)
                    ((PopupPanel)menuBar.getParent()).hide();
                
        }
        menuBar.activeItem = null;
        menuBar.active = false;
        removeStyleName("Selected");
    }
    
    public void createPopup(){
        menuBar.activeItem = this;
        if(popupNode == null)
            return;
        if(pop == null) {
            pop = new PopupPanel(true,false);
            ScreenMenuPanel mp = (ScreenMenuPanel)ScreenWidget.loadWidget(popupNode, screen);
            pop.setWidget(mp);
        
            pop.addPopupListener(this);
 
        }
        
        //to be able to use the widget on a screen we always need to set the position
        if(popupNode.getAttributes().getNamedItem("position").getNodeValue().equals("below"))
            pop.setPopupPosition(wid.getAbsoluteLeft()+8, 
                                 wid.getAbsoluteTop()+wid.getOffsetHeight());
        else
            pop.setPopupPosition(getAbsoluteLeft()+getOffsetWidth(),
                                 getAbsoluteTop());
        
        pop.show();
        child = ((ScreenMenuPanel)pop.getWidget());
        DOM.setElementProperty(pop.getElement(),"closeAll", "true");
        ((ScreenMenuPanel)pop.getWidget()).active = true;
        DeferredCommand.addCommand(new Command() {
            public void execute(){
                ((ScreenMenuPanel)pop.getWidget()).setSize(pop.getPopupTop());
            }
        });
        addStyleName("Selected");
    }
    
    public void closePopup() {
        if(pop != null){
            DOM.setElementProperty(pop.getElement(),"closeAll", "false");
            pop.hide();
        }
    }
    
    public void enable(boolean enabled){
        if(enabled){
            removeClickListener(this);
            addClickListener(this);
            sinkEvents(Event.ONCLICK);
            removeMouseListener(this);
            addMouseListener(this);
            sinkEvents(Event.MOUSEEVENTS);
            removeStyleName("disabled");
        }else{
            removeClickListener(this);
            removeMouseListener(this);
            unsinkEvents(Event.ONCLICK);
            unsinkEvents(Event.MOUSEEVENTS);
            addStyleName("disabled");
        }
    }
    
    public ScreenWidget getInstance() {
        ScreenLabel labelProxy = new ScreenLabel(label,null);
        labelProxy.sinkEvents(Event.MOUSEEVENTS);
        return labelProxy;
        
    }

}
