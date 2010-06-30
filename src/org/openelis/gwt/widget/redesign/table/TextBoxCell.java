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

import org.openelis.gwt.widget.TextBox;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;

public class TextBoxCell<T> implements CellRenderer<T>, CellEditor<T> {

    private TextBox<T> editor;
    
    public TextBoxCell() {
        
    }
    
    public void setEditor(TextBox<T> editor) {
        this.editor = editor;
        editor.setEnabled(true);
    }
        
    public void startEditing(Table table, FlexTable flexTable, int row, int col, T value, Event event) {
        editor.setValue(value);
        editor.setWidth(table.getColumnAt(col).getWidth()+"px");
        flexTable.setWidget(row, col, editor);
        editor.setFocus(true);
    }

    public T finishEditing() {
        return editor.getValue();
    }
    
    public void render(Table table, FlexTable flexTable, int row, int col, T value) {
        editor.setValue(value);
        flexTable.setText(row, col, editor.getText());
    }

}
