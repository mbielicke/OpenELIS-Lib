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
package org.openelis.gwt.widget.redesign.table;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.Label;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class implements the CellRenderer and CellEditor interfaces and is used
 * to edit and render cells in a Table that is text only
 * 
 * @author tschmidt
 * 
 */
public class LabelCell<T> implements CellRenderer<T>, CellEditor<T> {
    
    /**
     * Widget used to edit the cell
     */
    private Label<T>  editor;
    
    /**
     * Constructor that takes the editor to be used for the cell.
     * 
     * @param editor
     */
    public LabelCell(Label<T> editor) {
        this.editor = editor;
    }
    
    /**
     * Gets Formatted value from editor and sets it as the cells display
     */
    public void render(Table table, FlexTable flexTable, int row, int col, T value) {
        editor.setValue(value);
        flexTable.setText(row,col,editor.getText());
    }
    
    /**
     * Pulls value out of the editor returns it to the table. Will pass back a
     * QueryData object if in QueryMode or the editor value if in edit mode
     */
    public T finishEditing(Table table, FlexTable flexTable, int row, int col) {
        return editor.getValue();
    }

    /**
     * Sets the model value to the editor and then places the editor into the
     * cell to be edited.
     */
    @SuppressWarnings("unchecked")
    public void startEditing(Table table, FlexTable flexTable, int row, int col, T value, GwtEvent event) {
        editor.setValue(value);
        flexTable.setText(row,col,editor.getText());
    }
    
    /**
     * Makes sure the widget is in query mode and will set its value to the
     * passed QueryData and will place the widget in the table for editing
     */
    @SuppressWarnings("unchecked")
    public void startQueryEditing(Table table,
                                  FlexTable flexTable,
                                  int row,
                                  int col,
                                  QueryData query,
                                  GwtEvent event) {
        //Do nothing
               
    }
    
    /**
     * Returns the current widget set as this cells editor.
     */
    public Widget getWidget() {
        return editor;
    }
    
    /**
     * Sets the QueryData to the editor and sets the Query string into the cell
     * text
     */
    public void renderQuery(Table table, FlexTable flexTable, int row, int col, QueryData qd) {
        // TODO Auto-generated method stub    
    }

}
