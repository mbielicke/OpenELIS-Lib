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
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.Field;
import org.openelis.gwt.common.data.FieldType;


/**
 * A Label that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
@Deprecated
public class TableLabel extends SimplePanel implements TableCellWidget {
    
    private Label editor;
    private FieldType field;
    private int width;
    public static final String TAG_NAME = "table-label";
    public int rowIndex;

    
    public TableLabel() {

    }

    public void clear() {
        if(editor != null)
            editor.setText("");
    }

    public TableCellWidget getNewInstance() {
        TableLabel label = new TableLabel();
        label.width = width;
        return label;
    }

    public Widget getInstance(Node node) {
        return new TableLabel();
    }
    
    public TableLabel(Node node){
        
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

    public void setField(FieldType field) {
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
    
    public void setFocus(boolean focused) {

    }
    
    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int row) {
        rowIndex = row;
        
    }
    
    
}
