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

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.CheckBox;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTMLTable;
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
    private boolean query;
    
    /**
     * Constructor that takes the editor to be used for the cell.
     * 
     * @param editor
     */
    public CheckBoxCell(CheckBox editor) {
        this.editor = editor;
        editor.setEnabled(true);
    }
    
    public Object finishEditing() {
        if(query){
            return editor.getQuery();
        }
        return editor.getValue();
    }

    public ArrayList<LocalizedException> validate() {
        if (query){
            editor.validateQuery();
            return editor.getValidateExceptions();
        }        
        editor.validateValue();
        return editor.getValidateExceptions();
    }
    
    /**
     * Returns the current widget set as this cells editor.
     */
    @SuppressWarnings("rawtypes")
	public void startEditing(String value, Container container, GwtEvent event) {
        query = false;
        editor.setQueryMode(false);
        editor.setValue(value);
        if(event instanceof ClickEvent)
            editor.changeValue();
        container.setEditor(editor);
        DOM.setStyleAttribute(container.getElement(), "align", "center");  
    }
    
    @SuppressWarnings("rawtypes")
	public void startEditingQuery(QueryData qd, Container container, GwtEvent event) {        
        query = true;
        editor.setQueryMode(true);
        editor.setQuery(qd);
        if(event instanceof ClickEvent)
            editor.changeValue();
        container.setEditor(editor);
        DOM.setStyleAttribute(container.getElement(), "align", "center");          
    }
    
    /**
     * Gets Formatted value from editor and sets it as the cells display
     */
    public void render(HTMLTable table, int row, int col, String value) {
        String style;
        AbsolutePanel div;
        
        query = false;
        editor.setQueryMode(false);
        style = CheckBox.Value.getValue(value).getStyle();
        div = new AbsolutePanel();
        div.setStyleName(style);
        table.setWidget(row, col, div);
        table.getCellFormatter().setHorizontalAlignment(row, col, HasAlignment.ALIGN_CENTER);
    }
    
    public String display(String value) {
        return null;
    }

    /**
     * Sets the QueryData to the editor and sets the Query string into the cell
     * text
     */
    public void renderQuery(HTMLTable table, int row, int col, QueryData qd) {
        String style;
        AbsolutePanel div;
        
        query = true;
        editor.setQueryMode(true);
        style = CheckBox.Value.getValue(qd!=null?qd.getQuery():null).getStyle();
        div = new AbsolutePanel();
        div.setStyleName(style);
        table.setWidget(row, col, div);
        table.getCellFormatter().setHorizontalAlignment(row, col, HasAlignment.ALIGN_CENTER);
    }

    public boolean ignoreKey(int keyCode) {
        switch(keyCode) {
            case KeyCodes.KEY_ENTER :
                return true;
            default :
                return false;
        }
    }
    
    public Widget getWidget() {
    	return editor;
    }
  

}
