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

import org.openelis.gwt.common.data.AbstractField;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * A Label that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableLabel extends SimplePanel implements TableCellWidget {
	
	private Label editor;
	private AbstractField field;
    private int width;
    private NumberFormat displayMask;
    public static final String TAG_NAME = "table-label";

	
    public TableLabel() {

    }

    public void clear() {
    	if(editor != null)
    		editor.setText("");
    }

    public TableCellWidget getNewInstance() {
        TableLabel label = new TableLabel();
        label.width = width;
        label.displayMask = displayMask;
        
        return label;
    }

    public Widget getInstance(Node node) {
        return new TableLabel();
    }
    
    public TableLabel(Node node){
        if (node.getAttributes().getNamedItem("displayMask") != null) 
            displayMask = NumberFormat.getFormat(node.getAttributes().getNamedItem("displayMask").getNodeValue());
    }

	public void setDisplay() {
		setEditor();
		
	}

	public void setEditor() {
		if(editor == null){
			editor = new Label();
			editor.setWordWrap(false);
            editor.setWidth(width+"px");
		}
		Object val = field.getValue();
        if (val instanceof Integer)
            editor.setText(((Integer)val).toString());
        else if (val instanceof Double){
            if(displayMask != null && !"".equals(val)                            )
                editor.setText(displayMask.format((Double)val));
            else
                editor.setText(((Double)val).toString());
        }
        else if (val == null)
            editor.setText(" ");
        else
            editor.setText((String)val);
        setWidget(editor);
	}

	public void saveValue() {
		// TODO Auto-generated method stub
		
	}

	public void setField(AbstractField field) {
		this.field = field;
		
	}

    public void enable(boolean enabled) {
        // TODO Auto-generated method stub   
    }

    public void setCellWidth(int width) {
        this.width = width;
        if(editor != null){
            editor.setWidth(width+"px");
        }
    }
    
    
}
