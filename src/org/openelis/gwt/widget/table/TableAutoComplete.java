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

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataSet;
import org.openelis.gwt.common.data.DropDownField;
import org.openelis.gwt.common.data.StringField;
import org.openelis.gwt.screen.ScreenAutoCompleteWidget;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.AutoComplete;

import java.util.ArrayList;

public class TableAutoComplete extends TableCellInputWidget implements ChangeListener {

	public AutoComplete editor;
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
    public static final String TAG_NAME = "table-autoComplete";
    
    
    public TableAutoComplete(){
    	sinkEvents(Event.KEYEVENTS);
    }
    
    public TableCellWidget getNewInstance() {
    	TableAutoComplete ta = new TableAutoComplete();
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
    
    public TableAutoComplete getNewTableAuto() {
    	TableAutoComplete ta = new TableAutoComplete();
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
    
	public TableAutoComplete(Node node, ScreenBase screen) {
        ScreenAutoCompleteWidget sAuto = new ScreenAutoCompleteWidget(node,screen);
        this.screen = screen;
        editor = (AutoComplete)sAuto.getWidget();
        textValue.setValue("");
	}

	public void saveValue() {
        if(editor.model.getData().size() == 0)
            return;
        field.setValue(editor.getSelections());
        ((DropDownField)field).setModel(editor.model.getData());
		editor.hideTable();
        super.saveValue();
	}

	//the editor is shared so we need to set the textvalue
	public void setDisplay() {
		if(display == null){
			display = new Label();
			display.setWordWrap(false);
		}
        editor.model.load(((DropDownField)field).getModel());
        editor.setSelections(((DropDownField)field).getSelections());
        
        display.setText(editor.getTextBoxDisplay());
		setWidget(display);
        super.setDisplay();
	}

	public void setEditor() {
        editor.model.load(((DropDownField)field).getModel());
        editor.activeCell = -1;
        editor.activeRow = -1;
        editor.setSelections(((DropDownField)field).getSelections());
        editor.setWidth(width+"px");
		setWidget(editor);			
	}

	public void setField(AbstractField field) {
		this.field = field;
    }
	
	public void enable(boolean enabled) {
	    editor.enabled(enabled);
    }
	
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null){
            editor.setWidth(width+"px");
        }
        if(display != null)
            display.setWidth(width+"px");
    }

    public void onChange(Widget sender) {
        
    }
    
    public void setFocus(boolean focused) {
        editor.setFocus(focused);
    }
}

