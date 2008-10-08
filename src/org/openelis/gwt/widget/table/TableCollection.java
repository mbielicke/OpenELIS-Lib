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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.DataObject;

import java.util.ArrayList;

/**
 * A Panel that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableCollection extends SimplePanel implements TableCellWidget {
    
    private VerticalPanel editor;
    private DataObject field;
    public static final String TAG_NAME = "table-collection";
    protected int rowIndex;

    public TableCollection() {
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return new TableCollection();
    }

    public TableCollection(Node node) {
        // TODO Auto-generated method stub
        this();
    }

    public void setDisplay() {
        setEditor();
        
    }

    public void setEditor() {
        if(editor == null){
            editor = new VerticalPanel();
        }else{
            editor.clear();
        }
        ArrayList vals = (ArrayList)field.getValue();
        for(int i = 0; i < vals.size(); i++){
            editor.add(new Label((String)((DataObject)vals.get(i)).getValue()));
        }
        setWidget(editor);
    }

    public void saveValue() {
        // TODO Auto-generated method stub
        
    }

    public void setField(DataObject field) {
        this.field = field;
    }

    public void enable(boolean enabled) {
        // TODO Auto-generated method stub
        
    }

    public void setCellWidth(int width) {
        // TODO Auto-generated method stub
        
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return null;
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
