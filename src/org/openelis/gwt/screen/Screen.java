package org.openelis.gwt.screen;

import java.util.HashMap;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.table.TableCellWidget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class Screen extends Composite {
	
	public final AbsolutePanel panel = new AbsolutePanel();
    public HashMap<String,ScreenWidget> widgets = new HashMap<String,ScreenWidget>();
    protected Document xml;
    protected HashMap<ScreenWidget,String> tabOrder = new HashMap<ScreenWidget,String>();
    protected HashMap<ScreenWidget,String> tabBack = new HashMap<ScreenWidget,String>();
    public HashMap<String,Widget> shortcut = new HashMap<String,Widget>();
    public String name;
    public enum State {DEFAULT,DISPLAY,UPDATE,ADD,QUERY,BROWSE,DELETE};
    public enum Action {NEW_MODEL,REFRESH_PAGE,NEW_PAGE,LOAD,UNLOAD,SUBMIT_QUERY};
    public State state = State.DEFAULT;
    
    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public Screen() {
        initWidget(panel);
    }
    
    public Widget getWidget(String name) {
        if(widgets.containsKey(name))
            return widgets.get(name).getWidget();
        return null;
    }
    
    protected void draw() {
        Node screen = xml.getElementsByTagName("screen").item(0);
        if(screen.getAttributes().getNamedItem("name") != null){
            name = screen.getAttributes().getNamedItem("name").getNodeValue();
        }
        Node display = xml.getElementsByTagName("display").item(0);
        NodeList widgets = display.getChildNodes();
        for (int i = 0; i < widgets.getLength(); i++) {
            if (widgets.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Widget wid = createWidget(widgets.item(i), this);
                panel.add(wid);
                break;
            }
        }
        Node rpc = xml.getElementsByTagName("rpc").item(0);
        NodeList fields = rpc.getChildNodes(); 
        for(int i = 0; i < fields.getLength(); i++){
            if(fields.item(i).getNodeType() == Node.ELEMENT_NODE){
            	AbstractField field = Screen.createField(fields.item(i));
            	if(this.widgets.containsKey(field.key))
                    ((ScreenInputWidget)this.widgets.get(field.key)).setField(field);
            }
        }
        panel.setStyleName("Screen");
    }
    
    public static Widget createWidget(Node node, Screen screen) {
        String widName = node.getNodeName();
        if(node.getAttributes().getNamedItem("key") != null && screen.widgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue())){
            ScreenWidget sw = (ScreenWidget)screen.widgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
            sw.init(node, null);
            return sw;
        }
        return (ScreenWidget)ClassFactory.forName(widName,new Object[] {node,screen});
    }

    public static TableCellWidget createCellWidget(Node node, ScreenBase screen) {
        String widName = "table-" + node.getNodeName();
        return (TableCellWidget)ClassFactory.forName(widName,new Object[] {node, screen});
    }
    
    public static AbstractField createField(Node node) {
        String fName = "rpc-" + node.getNodeName();
        if(node.getAttributes().getNamedItem("class") != null)
            fName = node.getAttributes().getNamedItem("class").getNodeValue();
        return (AbstractField)ClassFactory.forName(fName,new Object[] {node});
    }
    
    protected boolean validate() {
        return true;
    }
    
    protected void strikeThru(boolean enabled) {
        for(String key : widgets.keySet()) {
            //if (!form.getFields().contains(key) && !form.getFieldList().contains(key+"Id")) {
              //  continue;
           // }
            if(enabled)
                widgets.get(key).addStyleName("strike");
            else
                widgets.get(key).removeStyleName("strike");
      }
   }
    
    /**
     * This method is used to control the Tab order of input fields defined in
     * the form XML.  It is called when tab is pressed on widgets that have been 
     * defined to do so by overriding it's onBrowser(Event event) method during
     * declaration.  
     * 
     * @param event
     * @param wid
     */
    
    public void doTab(Event event, ScreenWidget wid) {
        doTab(DOM.eventGetShiftKey(event),wid);
        DOM.eventCancelBubble(event, true);
        DOM.eventPreventDefault(event);
    }
    
    public void doTab(boolean shift, ScreenWidget wid) {
        
        ScreenWidget obj = null;
        if (shift)
            obj = widgets.get(tabBack.get(wid));
        else
            obj = widgets.get(tabOrder.get(wid));
        if (obj != null) {
            boolean tabbed = false;
            while (!tabbed) {
                if (obj.isVisible() && obj.isEnabled()) {
                    tabbed = true;
                    obj.setFocus(true);
                } else {
                    if (shift)
                        obj = widgets.get(tabBack.get(obj));
                    else
                        obj = widgets.get(tabOrder.get(obj));
                }
            }
        }
    }
    
    /**
     * This method adds the input field to the form tab order.
     * 
     * @param on
     * @param to
     */
    public void addTab(ScreenWidget on, String[] to) {
        tabOrder.put(on, to[0]);
        if (to.length > 1)
            tabBack.put(on, to[1]);
    }
}
