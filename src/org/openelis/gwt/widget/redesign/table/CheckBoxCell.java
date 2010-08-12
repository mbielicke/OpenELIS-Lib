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
import org.openelis.gwt.widget.CheckBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is used by Table to edit and render columns that use CheckBox 
 * @author tschmidt
 *
 */
public class CheckBoxCell implements CellEditor<String>, CellRenderer<String> {

    /**
     * Widget used to edit the cell
     */
    private CheckBox editor;
    
    /**
     * Container to hold the widget for formatting and spacing
     */
    private AbsolutePanel container;
    
    /**
     * Constructor that takes the editor to be used for the cell.
     * 
     * @param editor
     */
    public CheckBoxCell(CheckBox editor) {
        this.editor = editor;
        editor.setEnabled(true);
        container = new AbsolutePanel();
        container.setStyleName("CellContainer");
        container.add(editor);
        DOM.setStyleAttribute(container.getElement(), "align", "center");       
    }
    
    /**
     * Pulls value out of the editor returns it to the table. Will pass back a
     * QueryData object if in QueryMode or the editor value if in edit mode
     */
    public Object finishEditing(Table table, FlexTable flexTable, int row, int col) {
        table.ignoreReturn(false);
        if(table.getQueryMode()){
            return editor.getQuery();
        }
        return editor.getValue();
    }

    /**
     * Returns the current widget set as this cells editor.
     */
    public Widget getWidget() {
        return editor;
    }

    /**
     * Sets the model value to the editor and then places the editor into the
     * cell to be edited.
     */
    @SuppressWarnings("unchecked")
    public void startEditing(Table table,
                             FlexTable flexTable,
                             int row,
                             int col,
                             String value,
                             GwtEvent event) {
        editor.setQueryMode(false);
        editor.setValue(value);
        if(event instanceof ClickEvent)
            editor.changeValue();
        placeWidget(table,flexTable,row,col);
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
        if(event instanceof ClickEvent)
            editor.changeValue();
        placeWidget(table,flexTable,row,col);
    }

    /**
     * Gets Formatted value from editor and sets it as the cells display
     */
    public void render(Table table, FlexTable flexTable, int row, int col, String value) {
        String style;
        AbsolutePanel div;
        editor.setQueryMode(table.getQueryMode());
        style = CheckBox.Value.getValue(value).getStyle();
        div = new AbsolutePanel();
        div.setStyleName(style);
        flexTable.setWidget(row, col, div);
        flexTable.getCellFormatter().setHorizontalAlignment(row, col, HasAlignment.ALIGN_CENTER);
    }

    /**
     * Sets the QueryData to the editor and sets the Query string into the cell
     * text
     */
    public void renderQuery(Table table, FlexTable flexTable, int row, int col, QueryData qd) {
        render(table,flexTable,row,col,qd!=null?qd.query:null);   
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
        table.ignoreReturn(true);
       
        container.setWidth((table.getColumnAt(col).getWidth()-3)+"px");
        container.setHeight((table.getRowHeight()-3)+"px");
        flexTable.setWidget(row, col, container);
       
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                editor.setFocus(true);
            }
        });
    }
    

}
