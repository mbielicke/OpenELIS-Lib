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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataObject;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.DropDownField;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.screen.ClassFactory;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.AutoCompleteDropdown;
import org.openelis.gwt.widget.AutoCompleteParamsInt;

import java.util.ArrayList;

public class TableAutoDropdown extends TableCellInputWidget implements EventPreview {

	public AutoCompleteDropdown editor;
	private Label display;
	
	//this should be the id of the selected element
	private StringField textValue = new StringField();
    private ChangeListener listener;
	
    public boolean loadFromModel;
    public String loadFromHidden = null;
    private boolean multi;
    private int visible = 1;
    private String fieldCase = "mixed";
    private String type = "";
    private int width;
    public static final String TAG_NAME = "table-autoDropdown";
    
    
    public TableAutoDropdown(){
    	sinkEvents(Event.KEYEVENTS);
    }
    
    public TableCellWidget getNewInstance() {
    	TableAutoDropdown ta = new TableAutoDropdown();
        ta.multi = multi;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listener = listener;
        ta.type = type;
        ta.screen = screen;
        return ta;
    }
    
    public TableAutoDropdown getNewTableAuto() {
    	TableAutoDropdown ta = new TableAutoDropdown();
        ta.multi = multi;
        ta.visible = visible;
        ta.loadFromHidden = loadFromHidden;
        ta.loadFromModel = loadFromModel;
        ta.editor = editor;
        ta.listener = listener;
        ta.type = type;
        ta.screen = screen;
        return ta;
    }
    
	public TableAutoDropdown(Node node, ScreenBase screen) {
		AutoCompleteDropdown auto;
        this.screen = screen;
        String cat = null;
        if(node.getAttributes().getNamedItem("cat") != null)
            cat = node.getAttributes().getNamedItem("cat").getNodeValue();
        String url = null;
        if(node.getAttributes().getNamedItem("serviceUrl") != null)
            url = node.getAttributes()
            .getNamedItem("serviceUrl")
                         .getNodeValue();
        
        boolean multiSelect = false;
        String textBoxDefault = null;
        String width = null;
        String popWidth = "auto";
        
        if (node.getAttributes().getNamedItem("multiSelect") != null)
        	multiSelect = true;
        
        if (node.getAttributes().getNamedItem("text") != null)
        	textBoxDefault = node.getAttributes()
                                  .getNamedItem("text")
                                  .getNodeValue();
        
        if (node.getAttributes().getNamedItem("width") != null)
        	width = node.getAttributes()
                                  .getNamedItem("width")
                                  .getNodeValue();
        
        if(node.getAttributes().getNamedItem("popWidth") != null){
            popWidth = node.getAttributes().getNamedItem("popWidth").getNodeValue();
        }
        Node widthsNode = ((Element)node).getElementsByTagName("widths").item(0);
        Node headersNode = ((Element)node).getElementsByTagName("headers").item(0);

        auto = new AutoCompleteDropdown(cat, url, multiSelect, textBoxDefault, width,popWidth);
        auto.mainHP.removeStyleName("AutoDropdown");
        auto.mainHP.addStyleName("TableAutoDropdown");
        auto.focusPanel.removeStyleName("AutoDropdownButton");
        auto.focusPanel.addStyleName("TableAutoDropdownButton");
        auto.setInsideTable(true);
        
        if(node.getAttributes().getNamedItem("autoParams") != null){
            auto.setAutoParams((AutoCompleteParamsInt)ClassFactory.forName(node.getAttributes().getNamedItem("autoParams").getNodeValue()));
        }
        
        if(node.getAttributes().getNamedItem("search") != null){
            if(node.getAttributes().getNamedItem("search").getNodeValue().equals("linear"))
                auto.setLinear(true);
        }
        
        if(headersNode != null) {
            auto.setHeaders(getHeaders(headersNode));
        }
        if(widthsNode != null) {
        	auto.setWidths(getWidths(widthsNode));
        }
        
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
        
        if (node.getAttributes().getNamedItem("case") != null){
            fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
            if(fieldCase.equals("upper"))
                auto.textBox.addStyleName("Upper");
            else if(fieldCase.equals("lower"))
                auto.textBox.addStyleName("Lower");
            auto.setCase(fieldCase);
        }
        
        auto.setForm(screen);
        
        editor = auto;
        textValue.setValue("");
	}

	public void saveValue() {
		//editor.complete();
        if(field instanceof DropDownField)
            field.setValue(editor.getSelected());
        else{
            field.setValue(((DataSet)editor.getSelected().get(0)).getKey().getValue());
        }
		editor.closePopup();
        super.saveValue();
	}

	//the editor is shared so we need to set the textvalue
	public void setDisplay() {
		if(display == null){
			display = new Label();
			display.setWordWrap(false);
		}
        if(field instanceof DropDownField)
            editor.setSelected(((DropDownField)field).getSelections());
        else{
            ArrayList selected = new ArrayList();
            selected.add(field.getDataObject());
            editor.setSelected(selected);
        }
        if(field instanceof DropDownField){
            if(((DropDownField)field).getSelections().size() > 0)
                if(editor.cat == null){
                    DataObject key = ((DataSet)((DropDownField)field).getSelections().get(0)).getKey();
                    DataSet selection = (DataSet)editor.getModel().get(key);
                    String text = (String)selection.getObject(0).getValue();
                    display.setText(text);
                }else
                    display.setText((String)((DataSet)((DropDownField)field).getSelections().get(0)).getObject(0).getValue());
            else
                display.setText("");
        }else{
            display.setText((String)((DataSet)editor.getModel().get(field.getDataObject())).getObject(0).getValue());
        }
		setWidget(display);		
        super.setDisplay();
	}

	public void setEditor() {
		if(editor == null){
			editor = new AutoCompleteDropdown();
         //   editor.setWidth(width+"px");
        }
        if(field instanceof DropDownField)
            editor.setSelected(((DropDownField)field).getSelections());
        else{
            ArrayList selected = new ArrayList();
            selected.add(field.getDataObject());
            editor.setSelected(selected);
        }
		setWidget(editor);			
	}

	public void setField(AbstractField field) {
		this.field = field;
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
      
   public String[] getHeaders(Node node){
   	return node.getFirstChild()
               .getNodeValue()
               .split(",");
   }

	public boolean onEventPreview(Event event) {
		if (DOM.eventGetType(event) == Event.ONKEYDOWN || DOM.eventGetType(event) == Event.ONKEYUP) {
	        DOM.eventCancelBubble(event, true);
	        DOM.eventPreventDefault(event);
	        return false;
	    }
	    return true;
	}
	
	public void enable(boolean enabled) {}
	
	public void setModel(DataModel model){
		editor.setModel(model);
	}
    
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null){
            editor.setWidth(width+"px");
        }
        if(display != null)
            display.setWidth(width+"px");
    }
}

