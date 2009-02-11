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

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.Field;
import org.openelis.gwt.common.data.FieldType;


/**
 * A HTML Widget that implements CellWidget so it can be used in a table
 * 
 * @author tschmidt
 * 
 */
public class TableLink extends SimplePanel implements TableCellWidget {
    
    private HTML editor;
    private FieldType field;
    private boolean enabled;
    public static final String TAG_NAME = "table-link";
    public int rowIndex;
    
    public TableLink() {
        editor = new HTML();
        editor.setWordWrap(false);
    }

    public void clear() {
        // TODO Auto-generated method stub
    }

    public TableLink(Node node){
        this();
    }
    
    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableLink();
    }

    public TableCellWidget getNewInstance() {
        TableLink tl = new TableLink();
        tl.enabled = enabled;
        return tl;
    }

    public void saveValue() {
        // TODO Auto-generated method stub
        
    }

    public void setDisplay() {
        setEditor();
    }

    public void setEditor() {
        editor.setHTML("<div><a>" + (String)((AbstractField)field).getValue() + "</a></div>");
        addStyleName("encounters");
        
    }

    public void setField(FieldType field) {
        this.field = field;
        
    }

    public void enable(boolean enabled) {
      this.enabled = enabled;
        
    }

    public void setCellWidth(int width) {
        // TODO Auto-generated method stub
        
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
