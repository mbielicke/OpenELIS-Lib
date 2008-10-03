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

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
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
import org.openelis.gwt.widget.Dropdown;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableLabel;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

public class ScreenDropDownWidget extends ScreenInputWidget implements FocusListener {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "dropdown";
    public String fieldCase = "mixed";
    public boolean loadFromModel = false;
    private boolean multiSelect; 
	/**
	 * Widget wrapped by this class.
	 */
    private Dropdown auto;
    
    private String type = "";

	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenDropDownWidget() {
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
    public ScreenDropDownWidget(Node node, final ScreenBase screen) {
        super(node);
        
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


        
        ArrayList<TableColumnInt> columns = new ArrayList<TableColumnInt>(); 
        if(widthsNode != null) {
            String[] widths = widthsNode.getFirstChild()
            .getNodeValue()
            .split(",");
            for (String wid : widths) {
                TableColumn col = new TableColumn();
                col.setCurrentWidth(Integer.parseInt(wid));
                col.setColumnWidget(new TableLabel());
                columns.add(col);
            }
        }else{
            TableColumn col = new TableColumn();
            col.setCurrentWidth(Integer.parseInt(width.substring(0, width.indexOf("px"))));
            col.setColumnWidget(new TableLabel());
            columns.add(col);
        }
        boolean showHeader = false;
        if(headersNode != null){
            showHeader = true;
            String[] headerNames = headersNode.getFirstChild()
            .getNodeValue()
            .split(",");
            for(int i = 0; i < headerNames.length; i++){
                columns.get(i).setHeader(headerNames[i].trim());
            }                    
        }
      

        auto = new Dropdown(columns,10,popWidth,null,showHeader,VerticalScroll.NEEDED);
        auto.init(multiSelect, width);
        final ScreenDropDownWidget sa = this; 

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

        
        auto.setForm(screen);
        
        ((AppScreen)screen).addKeyboardListener(auto.keyboardHandler);
        ((AppScreen)screen).addClickListener(auto.mouseHandler);
        initWidget(auto);
        displayWidget = auto;
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenDropDownWidget(node, screen);
    }

    public void load(AbstractField field) {
    	if(queryMode){
    		queryWidget.load(field);
    	}else{
            auto.setSelections(((DropDownField)field).getSelections());
            super.load(field);
        }
    }

    public void submit(AbstractField field) {
        if(queryMode){
            queryWidget.submit(field);
        }else {
        	field.setValue(auto.getSelections());
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
     //           auto.removeFocusListener(this);
       //         auto.addFocusListener(this);
            }else{
                auto.textBox.setReadOnly(!enabled);
                //if(enabled)
         //           auto.addFocusListener(this);
             //   else
           //         auto.removeFocusListener(this);
            }
            super.enable(enabled);
        }
    }
    
    public void destroy() {
    	//auto.clear();
    	//auto.scrollList = null;
        //auto = null;
        //super.destroy();
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
		set.add(display);

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
    
    public ScreenDropDownWidget getQueryWidget(){
    	return (ScreenDropDownWidget)queryWidget;
    }
    
    public void onLostFocus(Widget sender) {
    	//auto.onLostFocus(sender);
    	if(key != null)
    		super.onLostFocus(sender);
    }
    
   public void onFocus(Widget sender) {
	   //auto.onFocus(sender);
	   super.onFocus(sender);
   }
   
   public void setForm(boolean mode) {
       if(queryWidget == null){
          // if(mode && auto.cat == null)
         //      auto.setMultiSelect(true);
           //else
           //    auto.setMultiSelect(multiSelect);
       }else
           super.setForm(mode);
   }
}
