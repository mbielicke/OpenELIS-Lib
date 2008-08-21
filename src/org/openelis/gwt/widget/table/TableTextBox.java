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

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;


/**
 * A TextBox that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableTextBox extends TableCellInputWidget {
    
    public String fieldCase = "";
    private int length = -1;
    public TextBox editor;
    private Label display;
    private boolean enabled;
    private int width;
    private NumberFormat editorMask;
    private NumberFormat displayMask;
    public static final String TAG_NAME = "table-textbox";
    
    public TableTextBox() {
    	
    }
    
    public void clear() {
    	if(editor != null)
    		editor.setText("");
    	if(display != null)
    		display.setText("");
    }

    public TableCellWidget getNewInstance() {
        TableTextBox  textbox = new TableTextBox();
        textbox.fieldCase = fieldCase;
        textbox.enabled = enabled;
        textbox.length = length;
        textbox.editorMask = editorMask;
        textbox.displayMask = displayMask;
        return textbox;
    }

    public void setDisplay() {
    	if(display == null){
    		display = new Label();
            display.setWordWrap(false);
            if(fieldCase.equals("upper"))
                display.addStyleName("Upper");
            else if(fieldCase.equals("lower"))
                display.addStyleName("Lower");
            display.setWidth(width+"px");
    	}
        if(field.getValue() != null){
            String val = field.getValue().toString();
        
            if(displayMask != null && !"".equals(val))
                val = displayMask.format(Double.valueOf(val).doubleValue());
            display.setText(val);
        } else
            display.setText("");
        
        setWidget(display);
        super.setDisplay();
    }

    public void setEditor() {
        if(!enabled)
            return;
    	if(editor == null){
    		editor = new TextBox();
            editor.addFocusListener(this);
    		if(fieldCase.equals("upper"))
    			editor.addStyleName("Upper");
    		else if(fieldCase.equals("lower"))
    			editor.addStyleName("Lower");
    		
    		if(length > -1)
    			editor.setMaxLength(length);
            editor.setWidth(width+"px");
    	}
        if(field.getValue() != null)
            editor.setText(field.getValue().toString());
        else
            editor.setText("");
        setWidget(editor);
    }

    public TableTextBox(Node node, ScreenBase screen) {
        this.screen = screen;
        if(node.getAttributes().getNamedItem("case") != null)
            fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
        
        if (node.getAttributes().getNamedItem("max") != null) 
        	length = Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue());
        
        if (node.getAttributes().getNamedItem("editorMask") != null) 
            editorMask = NumberFormat.getFormat(node.getAttributes().getNamedItem("editorMask").getNodeValue());
        
        if (node.getAttributes().getNamedItem("displayMask") != null) 
            displayMask = NumberFormat.getFormat(node.getAttributes().getNamedItem("displayMask").getNodeValue());
    }

	public void saveValue() {
		String val = editor.getText();
        if(fieldCase.equals("upper"))
            val = val.toUpperCase();
        else if(fieldCase.equals("lower"))
            val = val.toLowerCase();
        
        if(editorMask != null && !"".equals(val))
            val = editorMask.format(Double.valueOf(val).doubleValue());
        
		field.setValue(val);
        super.saveValue();
	}

	public void setField(AbstractField field) {
		this.field = field;
	}

    public void enable(boolean enabled) {
       this.enabled = enabled;
        
    }
    
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null)
            editor.setWidth(width+"px");
        if(display != null)
            display.setWidth(width+"px");
    }

}
