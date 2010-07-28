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

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.widget.calendar.Calendar;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class implements the CellRenderer and CellEditor interfaces and is used 
 * to edit and render cells in a Table using a Calendar
 * @author tschmidt
 *
 * @param <T>
 */
public class CalendarCell implements CellRenderer<Datetime>, CellEditor<Datetime> {
    /**
     * Editor used by this cell
     */
    private Calendar editor;
    private AbsolutePanel container;
    
    /**
     * Constructor that takes the editor to be used as a param
     * @param editor
     */
    public CalendarCell(Calendar editor) {
        this.editor = editor;
        editor.setEnabled(true);
        editor.setStyleName("TableCalendar");
        container = new AbsolutePanel();
        container.add(editor,0,1);
        container.setStyleName("CellContainer");
    }
    
    /**
     * Method to return the editor set for this cell
     */
    public Widget getWidget() {
        return editor;
    }
    
    /**
     * Will set the value passed into the editor and set the editor into the
     * table cell passed
     */
    public void startEditing(Table table,
                             FlexTable flexTable,
                             int row,
                             int col,
                             Datetime value,
                             GwtEvent event) {
        editor.setValue(value);
        editor.setWidth(table.getColumnAt(col).getWidth()-15+"px");
        container.setWidth((table.getColumnAt(col).getWidth()-3)+"px");
        container.setHeight((table.getRowHeight()-3)+"px");
        flexTable.setWidget(row, col, container);
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                editor.setFocus(true);
            }
        });
    }

    public Datetime finishEditing(Table table, FlexTable flexTable, int row, int col) {
        return editor.getValue();
    }
    
    public void render(Table table, FlexTable flexTable, int row, int col, Datetime value) {
        editor.setValue(value);
        flexTable.setText(row, col, editor.getText());
    }
    


}
