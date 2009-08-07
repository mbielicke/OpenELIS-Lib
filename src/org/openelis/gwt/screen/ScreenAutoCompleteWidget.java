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

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DropDownField;
import org.openelis.gwt.common.data.QueryStringField;
import org.openelis.gwt.screen.AppScreenForm.State;
import org.openelis.gwt.widget.AutoComplete;
import org.openelis.gwt.widget.AutoCompleteCall;
import org.openelis.gwt.widget.AutoCompleteCallInt;
import org.openelis.gwt.widget.TextBox;
import org.openelis.gwt.widget.table.TableCellWidget;
import org.openelis.gwt.widget.table.TableColumn;
import org.openelis.gwt.widget.table.TableColumnInt;
import org.openelis.gwt.widget.table.TableLabel;
import org.openelis.gwt.widget.table.TableViewInt.VerticalScroll;

import java.util.ArrayList;

@Deprecated
public class ScreenAutoCompleteWidget extends ScreenInputWidget implements FocusListener {
	/**
	 * Default XML Tag Name for XML definition and WidgetMap
	 */
	public static String TAG_NAME = "autoComplete";
    public boolean loadFromModel = false;
    private boolean multiSelect; 
	/**
	 * Widget wrapped by this class.
	 */
    private AutoComplete auto;
    
    private String type = "";

	/**
	 * Default no-arg constructor used to create reference in the WidgetMap class
	 */
    public ScreenAutoCompleteWidget() {
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
    public ScreenAutoCompleteWidget(Node node, final ScreenBase screen) {
        super(node);
        init(node,screen);
    }
    
    public void init(Node node, ScreenBase screen) {
        if(node.getAttributes().getNamedItem("key") != null && screen.wrappedWidgets.containsKey(node.getAttributes().getNamedItem("key").getNodeValue()))
            auto = (AutoComplete)screen.wrappedWidgets.get(node.getAttributes().getNamedItem("key").getNodeValue());
        else
            auto = new AutoComplete();
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
        else
            textBoxDefault = "";
        
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
            col.setCurrentWidth(Integer.parseInt(width.substring(0,width.indexOf("px"))));
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
      

        auto.setup(columns,10,popWidth,null,showHeader,VerticalScroll.NEEDED);
        auto.init(cat, multiSelect, textBoxDefault, width);
        final ScreenAutoCompleteWidget sa = this; 
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
        
        if(node.getAttributes().getNamedItem("case") != null){
            String textCase = node.getAttributes().getNamedItem("case").getNodeValue().toUpperCase();
            auto.lookUp.getTextBox().setCase(TextBox.Case.valueOf(textCase));
        }
        
        auto.setForm(screen);
        
        if(node.getAttributes().getNamedItem("autoCall") != null) {
            String autoCall = node.getAttributes().getNamedItem("autoCall").getNodeValue();
            if(autoCall.equals("this")){
                auto.setAutoCall((AutoCompleteCallInt)screen);
            }else{
                auto.setAutoCall((AutoCompleteCallInt)ClassFactory.forName(autoCall));
            }
        }else if(url != null) {
            auto.setAutoCall(new AutoCompleteCall(url));
        }
        
        ((AppScreen)screen).addKeyboardListener(auto.keyboardHandler);
        ((AppScreen)screen).addClickListener(auto.mouseHandler);
        initWidget(auto);
        auto.setStyleName("FormDropdown");
        displayWidget = auto;
        setDefaults(node, screen);
    }

    public ScreenWidget getInstance(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        return new ScreenAutoCompleteWidget(node, screen);
    }

    public void load(AbstractField field) {
    	if(queryMode) {
    	    if(queryWidget != null)
    	  		queryWidget.load(field);
    	    else
    	        auto.lookUp.setText((String)field.getValue());
    	}else{
            if(((DropDownField)field).getModel() != null && ((DropDownField)field).getModel().size() > 0){
                auto.activeRow = -1;
                auto.activeCell = -1;
                auto.model.load(((DropDownField)field).getModel());
            }
            auto.setSelections(((DropDownField)field).getKeyValues());
            super.load(field);
        }
    }

    public void submit(AbstractField field) {
        if(queryMode) {
            if(queryWidget != null)
                queryWidget.submit(field);
            else
                field.setValue(auto.lookUp.getText());
        }else {
        	field.setValue(auto.getSelections());
        }
    }

    public void setFocus(boolean focused) {
        // TODO Auto-generated method stub
        if(queryMode && queryWidget != null)
            queryWidget.setFocus(focused);
        else
            auto.lookUp.setFocus(focused);
    }
    
    public void enable(boolean enabled){
        if(queryMode && queryWidget != null)
            queryWidget.enable(enabled);
        else{
            if(alwaysEnabled){
                auto.removeFocusListener(this);
                auto.addFocusListener(this);
            }else{
                auto.lookUp.getTextBox().setReadOnly(!enabled);
                auto.enabled = enabled;
                if(enabled)
                    auto.addFocusListener(this);
                else
                    auto.removeFocusListener(this);
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
    /*
    private TableDataModel<TableDataRow<Integer>> getDropDownOptions(Node itemsNode){
		TableDataModel dataModel = new TableDataModel();		
		
    	NodeList items = ((Element)itemsNode).getElementsByTagName("item");
    	for (int i = 0; i < items.getLength(); i++) {
    	TableDataRow<Integer> set = new TableDataRow<Integer>();
        Node item = items.item(i);

		//display text
        StringField display = new StringField();
		display.setValue((item.getFirstChild() == null ? "" : item.getFirstChild().getNodeValue()));
		set.add(display);

        //id
        if(type.equals("integer")){
        	//NumberObject id = new NumberObject(new Integer(item.getAttributes().getNamedItem("value").getNodeValue()));
        	set.setKey(new Integer(item.getAttributes().getNamedItem("value").getNodeValue()));
        }else if(type.equals("string")){
        	//Stringt id = new StringObject(item.getAttributes().getNamedItem("value").getNodeValue());
        	set.setKey(item.getAttributes().getNamedItem("value").getNodeValue());
        }
        
		dataModel.add(set);
    	}
        return dataModel;
	}
    */
    public ScreenAutoCompleteWidget getQueryWidget(){
    	return (ScreenAutoCompleteWidget)queryWidget;
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
   
   public void setForm(State state) {
       if(queryWidget == null){
          if(state == State.QUERY){
              queryField = new QueryStringField(key);
              auto.queryMode = true;
          }else
              auto.queryMode = false;
       }
       super.setForm(state);
   }
}
