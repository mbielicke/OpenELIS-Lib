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
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.CheckBox;
import org.openelis.gwt.widget.CheckBox.CheckType;


/**
 * Checkbox Widget that implements CellWidget interface so that it can
 * participate in a Table.
 * 
 * @author tschmidt
 * 
 */
public class TableCheck extends TableCellInputWidget implements FocusListener, ClickListener {

	private CheckBox editor;
    private boolean enabled;
    private FocusPanel panel;
    private CheckType type = CheckType.TWO_STATE;
    private int width;
    public static final String TAG_NAME = "table-check";
   
	public TableCheck() {
		editor = new CheckBox();
        editor.enable(enabled);
        panel = new FocusPanel();
        panel.add(editor);
        setWidget(panel);
        panel.addFocusListener(this);
        DOM.setElementProperty(getElement(), "align", "center");
	}
    /**
     * Clears value of cell to default.
     */
    public void clear() {
    	if(editor != null)
    		editor.setState(CheckBox.UNKNOWN);
    }

    /**
     * Returns a new TableCheck widget.
     * 
     * @return
     */
    public TableCellWidget getNewInstance() {
        TableCheck ch = new TableCheck();
        ch.enable(enabled);
        ch.type = type;
        ch.editor.setType(type);
        ch.screen = screen;
        return ch;
    }

    public TableCheck(Node node, ScreenBase screen) {
        this();
        this.screen = screen;
        if(node.getAttributes().getNamedItem("threeState") != null){
            type = CheckType.THREE_STATE;
            editor.setType(CheckType.THREE_STATE);
        }
        
    }

	public void saveValue() {
		field.setValue(editor.getState());
		super.saveValue();
	}

	public void setField(AbstractField field) {
		this.field = field;
		editor.setState((String)field.getValue());
	}
    
    public void onFocus(Widget sender) {
        DOM.setStyleAttribute(sender.getElement(), "background", "white");
        
    }
    public void onLostFocus(Widget sender) {
        DOM.setStyleAttribute(sender.getElement(), "background", "none");
    }
    
    public void enable(boolean enabled){
        this.enabled = enabled;
        editor.enable(enabled);
        if(enabled){
            editor.removeClickListener(this);
            editor.addClickListener(this);
        }else
            editor.removeClickListener(this);
          
    }
    
    public void setCellWidth(int width){
        this.width = width;
        panel.setWidth(width+"px");
    }
    
    public void setDisplay() {
        editor.setState((String)field.getValue());
    }
    
    public void onClick(Widget sender) {
        if(enabled)
            saveValue();
    }
}
