/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public
 * Software License(the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa.
 * Portions created by The University of Iowa are Copyright 2006-2008. All
 * Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be
 * used under the terms of a UIRF Software license ("UIRF Software License"), in
 * which case the provisions of a UIRF Software License are applicable instead
 * of those above.
 */
package org.openelis.gwt.widget.redesign.table;

import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * This class implements the CellRenderer and CellEditor interfaces and is used
 * to edit and render cells in a Table using a TextBox<T>
 * 
 * @author tschmidt
 * 
 * @param <T>
 */
public class TextBoxCell<T> implements CellRenderer<T>, CellEditor<T> {

    /**
     * Editor used by this cell
     */
    private TextBox<T>    editor;
    
    /**
     * Container to hold the widget for formatting and spacing
     */
    private AbsolutePanel container;

    /**
     * Constructor that takes the editor to be used as a param
     * 
     * @param editor
     */
    public TextBoxCell(TextBox<T> editor) {
        this.editor = editor;
        editor.setEnabled(true);
        editor.setStyleName("TableTextBox");
        container = new AbsolutePanel();
        container.add(editor, 0, 1);
        container.setStyleName("CellContainer");
    }

    /**
     * Will set the value passed into the editor and set the editor into the
     * table cell passed
     */
    @SuppressWarnings("unchecked")
    public void startEditing(final Table table,
                             FlexTable flexTable,
                             int row,
                             int col,
                             T value,
                             GwtEvent event) {
        editor.setQueryMode(false);
        editor.clearExceptions();
        editor.setValue(value);
        placeWidget(table, flexTable, row, col);
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
                                  QueryData qd,
                                  GwtEvent event) {
        editor.setQueryMode(true);
        editor.setQuery(qd);
        placeWidget(table, flexTable, row, col);
    }

    /**
     * Pulls value out of the editor returns it to the table. Will pass back a
     * QueryData object if in QueryMode or the editor value if in edit mode
     */
    public Object finishEditing(Table table, FlexTable flexTable, int row, int col) {
        
        if (table.getQueryMode()){
            editor.validateQuery();
            table.setValidateException(row, col, editor.getValidateExceptions());
            return editor.getQuery();
        }
        
        editor.validateValue();
        table.setValidateException(row, col, editor.getValidateExceptions());
        return editor.getValue();
    }

    /**
     * Gets Formatted value from editor and sets it as the cells display
     */
    public void render(Table table, FlexTable flexTable, int row, int col, T value) {
        editor.setQueryMode(false);
        editor.setValue(value);
        flexTable.setText(row, col, editor.getText());
    }

    /**
     * Sets the QueryData to the editor and sets the Query string into the cell
     * text
     */
    public void renderQuery(Table table, FlexTable flexTable, int row, int col, QueryData qd) {
        editor.setQueryMode(true);
        editor.setQuery(qd);
        flexTable.setText(row, col, editor.getText());
    }

    /**
     * Returns the current widget set as this cells editor.
     */
    public TextBox<T> getWidget() {
        return editor;
    }
    
    /**
     * Sizes and places the editor into the passed cell into the table.
     * 
     * @param table
     * @param flexTable
     * @param row
     * @param col
     */
    private void placeWidget(Table table, FlexTable flexTable, int row, int col) {
        editor.setWidth(table.getColumnAt(col).getWidth() + "px");
        container.setWidth( (table.getColumnAt(col).getWidth() - 3) + "px");
        container.setHeight( (table.getRowHeight() - 3) + "px");
        flexTable.setWidget(row, col, container);
        /*
         * This done in a deferred command otherwise IE will not set focus
         * consistently
         */
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                editor.setFocus(true);
            }
        });
    }

}
