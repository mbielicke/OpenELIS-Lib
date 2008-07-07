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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.MaskedTextBox;

/**
 * A MaskedTextBox that implements a CellWidget so that it can be used in a
 * Table.
 * 
 * @author tschmidt
 * 
 */
public class TableMaskedTextBox extends TableCellInputWidget {
	
	private MaskedTextBox editor;
	private Label display;
	private String mask;
    private boolean enabled;
    private int width;
    public static final String TAG_NAME = "table-maskedbox";
	
    public TableMaskedTextBox() {
    }

    public void clear() {
    	if(editor != null)
    		editor.setText("");
    	if(display != null)
    		display.setText("");
    }

    public TableCellWidget getNewInstance() {
        TableMaskedTextBox tb = new TableMaskedTextBox();
        tb.mask = mask;
        tb.enabled = enabled;
        tb.screen = screen;
        return tb;
    }

    public TableMaskedTextBox(Node node, ScreenBase screen) {
        // TODO Auto-generated method stub
        this.screen = screen;
        mask = (node.getAttributes().getNamedItem("mask").getNodeValue());
    }

	public void setDisplay() {
		if(display == null){
	        display = new Label();
	        display.setWordWrap(false);
            display.setWidth(width+"px");
		}
		display.setText((String)field.getValue());
		setWidget(display);
        super.setDisplay();
	}

	public void setEditor() {
        if(!enabled)
            return;
		if(editor == null){
			editor = new MaskedTextBox();
            editor.addFocusListener(this);
			editor.setMask(mask);
            editor.setWidth(width+"px");
		}
		editor.setText((String)field.getValue());
		setWidget(editor);
	}

	public void saveValue() {
        editor.format();
		field.setValue(editor.getText());
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
