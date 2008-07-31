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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.table.TableCellWidget;

import java.util.HashMap;
import java.util.Iterator;
/**
 * The Screen class is the base class for displaying a screen 
 * drawn on the client machine.  It can also validate input and
 * and return data to the servlet.  This class is compiled into 
 * javascript and run on the client.
 * 
 * @author tschmidt
 *
 */
public class ScreenBase extends Composite implements FocusListener{
	/**
	 * All drawn widgets will be held in this panel.
	 */
    protected VerticalPanel panel = new VerticalPanel();
    public FormRPC rpc;
    /** 
     * All widgets drawn on screen are referenced in this
     * HashMap
     */
    public HashMap<String,ScreenWidget> widgets = new HashMap<String,ScreenWidget>();
    protected Document xml;
    protected HashMap<ScreenWidget,String> tabOrder = new HashMap<ScreenWidget,String>();
    protected HashMap<ScreenWidget,String> tabBack = new HashMap<ScreenWidget,String>();
    public boolean keep;
    public String name;
    public HashMap<String,Widget> shortcut = new HashMap<String,Widget>();
   
    /**
     * No arg constructor will initiate a blank panel and new FormRPC 
     */
    public ScreenBase() {
        initWidget(panel);
        rpc = new FormRPC();
    }

    /**
     * This method will return the widget being wrapped by the ScreenWidget
     * wth given name parameter
     * @param name
     * @return
     */
    public Widget getWidget(String name) {
        if(widgets.containsKey(name))
            return widgets.get(name).getWidget();
        return null;
    }

    /**
     * This method will put together the screen from the xml definition.
     * It will call the afterSubmit method when done with a method of "draw"
     * 
     */
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
                }
            }
            panel.setStyleName("Screen"); 
    }
    

    /**
     * This method will load the form with data provided by the callService
     * method and extract it from the FormRPC.
     * 
     */
    protected void load() {
        load(rpc);
    }
    
    protected void load(FormRPC rpc){
        for(AbstractField field : rpc.getFieldMap().values()) {
            if(field instanceof FormRPC){
                load((FormRPC)field);
            }else if(widgets.containsKey(field.getKey())){
                ScreenWidget inputField = widgets.get((String)field.getKey());
                inputField.load(field);
            }
        }
        
        
    }

    /**
     * This method will draw any validation errors the form may have
     * 
     */
    protected void drawErrors() {
        clearErrors();
        for (ScreenWidget wid : widgets.values()) {
            if(wid instanceof ScreenInputWidget){
            	String key = ((ScreenInputWidget)wid).key;
                AbstractField field = rpc.getField(key);
                if(field != null && !field.isValid())
                    ((ScreenInputWidget)wid).drawError();
            }
        }
    }
    
    /**
     * Returns a new Widget created by calling getInstance(node, screen) for the widget 
     * who's tag in the passed node maps. 
     * @param node
     * @param screen
     * @return
     */
    public static Widget createWidget(Node node, ScreenBase screen) {
        String widName = node.getNodeName();
        if (widName.equals("panel"))
            widName += "-" + node.getAttributes()
                                 .getNamedItem("layout")
                                 .getNodeValue();
        return (ScreenWidget)ClassFactory.forName(widName,new Object[] {node,screen});
    }
    
    /**
     * Returns a TableCellWidget from the passed in node's tag name.
     * @param node
     * @return
     */
    public static TableCellWidget createCellWidget(Node node, ScreenBase screen) {
        String widName = "table-" + node.getNodeName();
        return (TableCellWidget)ClassFactory.forName(widName,new Object[] {node, screen});
    }

    /** 
     * Returns the AbstractField from the passed in node's tag name.
     * @param node
     * @return
     */
    public static AbstractField createField(Node node) {
        String fName = "rpc-" + node.getNodeName();
        return (AbstractField)ClassFactory.forName(fName,new Object[] {node});
    }
    

    /**
     * This method can be overridden by an extending class to provide more
     * specific validation logic if necessary.  
     * 
     * @return
     */
    protected boolean validate() {
        return true;
    }

    /**
     * This method will pull information inputed by the user and prepare it to
     * be sent back to the server for processing
     * 
     */
    protected void doSubmit() {
        doSubmit(rpc);
    }
    
    protected void doSubmit(FormRPC rpc) {
        rpc.reset();
        for (AbstractField field : rpc.getFieldMap().values()){
            if(field instanceof FormRPC)
                doSubmit((FormRPC)field);
            else if(widgets.containsKey(field.getKey())){
                ScreenWidget inputField = widgets.get(field.getKey());
                inputField.submit(field);
            }
        }
    }

    /**
     * This method will enable or disable all input widgets on the screen depending on
     * the value of the parameter enabled
     * 
     * @param enabled
     */
    protected void enable(boolean enabled) {
        Iterator wids = widgets.values().iterator();
        while (wids.hasNext()) {
            Widget wid = (Widget)wids.next();
            if(wid instanceof ScreenWidget)
                ((ScreenWidget)wid).enable(enabled);
        }
    }
    
    /**
     * This method will enable or disable all input widgets on the screen depending on
     * the value of the parameter enabled
     * 
     * @param enabled
     */
    protected void strikeThru(boolean enabled) {
        for(String key : widgets.keySet()) {
            if (!rpc.getFieldMap().containsKey(key) && !rpc.getFieldMap().containsKey(key+"Id")) {
            	continue;
            }
            if(enabled)
            	widgets.get(key).addStyleName("strike");
            else
            	widgets.get(key).removeStyleName("strike");
      }
   }

    /**
     * Implementation of the onFocus method from FocusListener. Any widget that adds
     * the Screen as a FocusListener will call this method when focused. May be overridden
     * by the extending class to change the default behavior.
     */
    public void onFocus(Widget sender) {
        sender.addStyleName("focused");
    }

    /**
     * Implementation of the onFocus method from FocusListener. Any widget that adds
     * the Screen as a FocusListener will call this method when focus is lost. May be overridden
     * by the extending class to change the default behavior.
     */
    public void onLostFocus(Widget sender) {
        sender.removeStyleName("focused");
    }

    /**
     * This method will reset all input fields to default null value.
     * 
     */
    protected void resetRPC() {
        resetRPC(rpc);
    }
    
    protected void resetRPC(FormRPC rpc) {
        for (AbstractField field: rpc.getFieldMap().values()){
            if(field instanceof FormRPC){
                resetRPC((FormRPC)field);
                ((FormRPC)field).load = false;
            }else if(field.allowsReset()){
                field.setValue(null);
                field.reset();
            }
        }
    }

    /**
     * This method will clear all displayed errors in the form.
     * 
     */
    protected void clearErrors() {
        for(ScreenWidget wid : widgets.values()){
            if(wid instanceof ScreenInputWidget){
                ((ScreenInputWidget)wid).clearError();
            }
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
        ScreenWidget obj = null;
        if (event != null && DOM.eventGetShiftKey(event))
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
                    if (event != null && DOM.eventGetShiftKey(event))
                        obj = widgets.get(tabBack.get(obj));
                    else
                        obj = widgets.get(tabOrder.get(obj));
                }
            }
            if(event != null){
                DOM.eventCancelBubble(event, true);
                DOM.eventPreventDefault(event);
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
    
    protected void onDetach() {
        // TODO Auto-generated method stub
        if(!keep){
            for(ScreenWidget wid : widgets.values()) {
                if(wid instanceof ScreenWidget)
                    wid.destroy();
            }
            widgets.clear();
            widgets = null;
            xml = null;
            tabOrder.clear();
            tabOrder = null;
            tabBack.clear();
            tabBack = null;
            rpc = null;
        }
        super.onDetach();
    }
}
