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

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * An Image widget that implements CellWidget so it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableImage extends SimplePanel implements TableCellWidget {
  
    private Image editor;
    private AbstractField field;
    public static final String TAG_NAME = "table-image";
    
    public TableImage() {
    }

    public void clear() {
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return new TableImage();
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableImage();
    }
    
    public TableImage(Node node){
        this();
    }

	public void setDisplay() {
		setEditor();
		
	}

	public void setEditor() {
		if(editor == null){
			editor = new Image();
		}
		editor.setUrl((String)field.getValue());
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
        // TODO Auto-generated method stub
        
    }
}
