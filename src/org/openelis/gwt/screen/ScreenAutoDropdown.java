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
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.screen;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.DropDownField;
import org.openelis.gwt.common.data.NumberObject;
import org.openelis.gwt.common.data.StringObject;
import org.openelis.gwt.widget.AutoCompleteDropdown;
import org.openelis.gwt.widget.AutoCompleteParamsInt;
import org.openelis.gwt.widget.table.TableCellWidget;

import java.util.ArrayList;

public class ScreenAutoDropdown extends ScreenInputWidget implements FocusListener {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "autoDropdown";
    public String fieldCase = "mixed";
    public boolean loadFromModel = false;
    private boolean multiSelect; 
	/**
	 * Widget wrapped by this class.
	 */
    private AutoCompleteDropdown auto;
    
    private String type = "";

	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenAutoDropdown() {
    }
    
    /**
     * Constructor called from getInstance to return a specific instance of this class
     * to be displayed on the screen.  It uses the XML Node to create it's widget
     * <br/><br/>
     * &;t;auto key="string" serviceUrl="string" cat="string" shortcut="char"/&gt;
     * 
     * @param node
     * @param screen
     */
    public ScreenAutoDropdown(Node node, final ScreenBase screen) {
        super(node);
        String cat = null;
        if(node.getAttributes().getNamedItem("cat") != null)
            cat = node.getAttributes().getNamedItem("cat").getNodeValue();
        String url = null;
        if(node.getAttributes().getNamedItem("serviceUrl") != null)
            url = node.getAttributes()
            .getNamedItem("serviceUrl")
                         .getNodeValue();
        
        multiSelect = false;
        String textBoxDefault = null;
        String width = null;
        String popWidth = "auto";
                
        if (node.getAttributes().getNamedItem("multiSelect") != null && node.getAttributes().getNamedItem("multiSelect").getNodeValue().equals("true"))
        	multiSelect = true;
        
        if (node.getAttributes().getNamedItem("text") != null)
        	textBoxDefault = node.getAttributes()
                                  .getNamedItem("text")
                                  .getNodeValue();
        
        if (node.getAttributes().getNamedItem("width") != null)
        	width = node.getAttributes()
                                  .getNamedItem("width")
                                  .getNodeValue();
        
        if (node.getAttributes().getNamedItem("popWidth") != null)
            popWidth = node.getAttributes()
                                  .getNamedItem("popWidth")
                                  .getNodeValue();
        
        Node widthsNode = ((Element)node).getElementsByTagName("widths").item(0);
        Node headersNode = ((Element)node).getElementsByTagName("headers").item(0);

        auto = new AutoCompleteDropdown();
        final ScreenAutoDropdown sa = this; 
        auto.textBox = new TextBox() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {       
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB){
                        screen.doTab(event, sa);                      
                    }
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };

        auto.focusPanel = new FocusPanel() {
            public void onBrowserEvent(Event event) {
                if (DOM.eventGetType(event) == Event.ONKEYDOWN) {
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB)
                        onLostFocus(auto.textBox);

                    if (multiSelect) {
                        auto.visible = false;
                        auto.choicesPopup.hide();
                    }
                    screen.doTab(event, sa);
                } else {
                    super.onBrowserEvent(event);
                }
            }
        };

        auto.init(cat, url, multiSelect, textBoxDefault, width, popWidth);
        auto.setForm(screen);
        
        if(node.getAttributes().getNamedItem("autoParams") != null){
            auto.setAutoParams((AutoCompleteParamsInt)ClassFactory.forName(node.getAttributes().getNamedItem("autoParams").getNodeValue()));
        }
        
        if(node.getAttributes().getNamedItem("search") != null){
            if(node.getAttributes().getNamedItem("search").getNodeValue().equals("linear"))
                auto.setLinear(true);
        }
        
        if(headersNode != null) 
        	auto.setHeaders(getHeaders(headersNode));
        
        if(widthsNode != null) 
            auto.setWidths(getWidths(widthsNode));
        
        if (node.getAttributes().getNamedItem("case") != null){
            fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
            if(fieldCase.equals("upper"))
                auto.textBox.addStyleName("Upper");
            else if(fieldCase.equals("lower"))
                auto.textBox.addStyleName("Lower");
            auto.setCase(fieldCase);
        }
               
        if (node.getAttributes().getNamedItem("rows") != null)
        	auto.setRows(Integer.parseInt(node.getAttributes().getNamedItem("rows").getNodeValue()));
      
        if (node.getAttributes().getNamedItem("onchange") != null){
            String[] listeners = node.getAttributes().getNamedItem("onchange").getNodeValue().split(",");
            for(int i = 0; i < listeners.length; i++){
                if(listeners[i].equals("this")){
                    auto.addChangeListener((ChangeListener)screen);
                }else{
                    auto.addChangeListener((ChangeListener)ClassFactory.forName(listeners[i]));
                }
            }
        }
        auto.scrollList.view.initTable(auto.scrollList);
        ((AppScreen)screen).addKeyboardListener(auto.scrollList);
        ((AppScreen)screen).addClickListener(auto.scrollList);
        initWidget(auto);
        displayWidget = auto;
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenAutoDropdown(node, screen);
    }

    public void load(AbstractField field) {
    	if(queryMode){
    		queryWidget.load(field);
    	}else{
            auto.setSelected(((DropDownField)field).getSelections());
        }
    }

    public void submit(AbstractField field) {
        if(queryMode){
            queryWidget.submit(field);
        }else {
        	field.setValue(auto.getSelected());
        }
    }

    public void setFocus(boolean focused) {
        // TODO Auto-generated method stub
        if(queryMode)
            queryWidget.setFocus(focused);
        else
            auto.textBox.setFocus(focused);
    }
    
    public void enable(boolean enabled){
        if(queryMode)
            queryWidget.enable(enabled);
        else{
            if(alwaysEnabled){
                auto.removeFocusListener(this);
                auto.addFocusListener(this);
            }else{
                auto.textBox.setReadOnly(!enabled);
                if(enabled)
                    auto.addFocusListener(this);
                else
                    auto.removeFocusListener(this);
            }
            super.enable(enabled);
        }
    }
    
    public void destroy() {
    	auto.clear();
    	auto.scrollList = null;
        auto = null;
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
    
    public AbstractField[] getFields(Node node) {
        NodeList fieldList = node.getChildNodes();
        ArrayList<AbstractField> list = new ArrayList<AbstractField>();
        for (int i = 0; i < fieldList.getLength(); i++) {
            if (fieldList.item(i).getNodeType() == Node.ELEMENT_NODE) {
                list.add(ScreenBase.createField(fieldList.item(i)));
            }
        }
        return (AbstractField[])list.toArray();
    }
    
    public TableCellWidget[] getEditors(Node node) {
        NodeList editors = node.getChildNodes();
        ArrayList<TableCellWidget> list = new ArrayList<TableCellWidget>();
        for (int i = 0; i < editors.getLength(); i++) {
            if (editors.item(i).getNodeType() == Node.ELEMENT_NODE) {
                list.add(ScreenBase.createCellWidget(editors.item(i),screen));
            }
        }
        return (TableCellWidget[])list.toArray();
    }
    
    public String[] getHeaders(Node node){
    	return node.getFirstChild()
                .getNodeValue()
                .split(",");
    }
    
    private DataModel getDropDownOptions(Node itemsNode){
		DataModel dataModel = new DataModel();		
		
    	NodeList items = ((Element)itemsNode).getElementsByTagName("item");
    	for (int i = 0; i < items.getLength(); i++) {
    	DataSet set = new DataSet();
        Node item = items.item(i);

		//display text
        StringObject display = new StringObject();
		display.setValue((item.getFirstChild() == null ? "" : item.getFirstChild().getNodeValue()));
		set.addObject(display);

        //id
        if(type.equals("integer")){
        	NumberObject id = new NumberObject(new Integer(item.getAttributes().getNamedItem("value").getNodeValue()));
        	set.setKey(id);
        }else if(type.equals("string")){
        	StringObject id = new StringObject(item.getAttributes().getNamedItem("value").getNodeValue());
        	set.setKey(id);
        }
        
		dataModel.add(set);
    	}
        return dataModel;
	}
    
    public ScreenAutoDropdown getQueryWidget(){
    	return (ScreenAutoDropdown)queryWidget;
    }
    
    public void onLostFocus(Widget sender) {
    	auto.onLostFocus(sender);
    	if(key != null)
    		super.onLostFocus(sender);
    }
    
   public void onFocus(Widget sender) {
	   auto.onFocus(sender);
	   super.onFocus(sender);
   }
   
   public void setForm(boolean mode) {
       if(queryWidget == null){
           if(mode)
               auto.setMultiSelect(true);
           else
               auto.setMultiSelect(multiSelect);
       }else
           super.setForm(mode);
   }
}
