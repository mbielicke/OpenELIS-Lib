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

import org.openelis.gwt.widget.CheckBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Widget;

public class CheckBoxCell implements CellEditor<String>, CellRenderer<String> {

    private CheckBox editor;
    private AbsolutePanel container;
    
    public CheckBoxCell(CheckBox editor) {
        this.editor = editor;
        editor.setEnabled(true);
        container = new AbsolutePanel();
        container.setStyleName("CellContainer");
        container.add(editor);
        DOM.setStyleAttribute(container.getElement(), "align", "center");
        
    }
    
    public String finishEditing(Table table, FlexTable flexTable, int row, int col) {
        table.ignoreReturn(false);
        return editor.getValue();
        
    }

    public Widget getWidget() {
        return editor;
    }

    public void startEditing(Table table,
                             FlexTable flexTable,
                             int row,
                             int col,
                             String value,
                             GwtEvent event) {
        table.ignoreReturn(true);
        editor.setValue(value);
        if(event instanceof ClickEvent)
            editor.changeValue();
        container.setWidth((table.getColumnAt(col).getWidth()-3)+"px");
        container.setHeight((table.getRowHeight()-3)+"px");
        flexTable.setWidget(row, col, container);
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                editor.setFocus(true);
            }
        });
        
    }

    public void render(Table table, FlexTable flexTable, int row, int col, String value) {
        String style;
        AbsolutePanel div;
        
        style = CheckBox.Value.getValue(value).getStyle();
        div = new AbsolutePanel();
        div.setStyleName(style);
        flexTable.setWidget(row, col, div);
        flexTable.getCellFormatter().setHorizontalAlignment(row, col, HasAlignment.ALIGN_CENTER);
    }

}
