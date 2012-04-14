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
package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.Dropdown;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class implements the CellRenderer and CellEditor interfaces and is used
 * to edit and render cells in a Table using an Dropdown<T> widget
 * 
 * @author tschmidt
 * 
 * @param <T>
 */
public class DropdownCell implements CellRenderer, CellEditor {

    /**
     * Widget used to edit the cell
     */
    private Dropdown      editor;

    private boolean       query;
    
    private ColumnInt     column;

    /**
     * Constructor that takes the editor to be used for the cell.
     * 
     * @param editor
     */
    public DropdownCell(Dropdown editor) {
        this.editor = editor;
        editor.setEnabled(true);
        editor.setStyleName("TableDropdown");
        editor.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				column.finishEditing();
			}
		});
    }

    public Object finishEditing() {
    	editor.finishEditing();
        if (query) 
            return editor.getQuery();
        
        return editor.getValue();
    }

    public ArrayList<LocalizedException> validate(Object value) {
    	if(!query) {
    		editor.setValue(value);
    		editor.hasExceptions();
        	return editor.getValidateExceptions();
    	}
        
        return null;
    }

    /**
     * Gets Formatted value from editor and sets it as the cells display
     */
    public void render(HTMLTable table, int row, int col, Object value) {
        editor.setQueryMode(false);
       	editor.setValue(value);
       	table.setText(row, col, editor.getDisplay());
    }

    public String display(Object value) {
        query = false;
        editor.setQueryMode(false);
        editor.setValue(value);
        return editor.getDisplay();
    }

    /**
     * Sets the QueryData to the editor and sets the Query string into the cell
     * text
     */
    public void renderQuery(HTMLTable table, int row, int col, QueryData qd) {
        query = true;
        editor.setQueryMode(true);
        editor.setQuery(qd);
        table.setText(row, col, editor.getDisplay());
    }

    /**
     * Returns the current widget set as this cells editor.
     */
    @SuppressWarnings("rawtypes")
	public void startEditing(Object value, Container container, GwtEvent event) {
        query = false;
        editor.setQueryMode(false);
        editor.setValue(value);
        editor.setWidth(container.getWidth()+"px");
        container.setEditor(editor);
    }

    @SuppressWarnings("rawtypes")
	public void startEditingQuery(QueryData qd, Container container, GwtEvent event) {
        query = true;
        editor.setQueryMode(true);
        editor.setQuery(qd);
        editor.setWidth(container.getWidth()+"px");
        container.setEditor(editor);
    }
    
    public boolean ignoreKey(int keyCode) {
        switch(keyCode) {
            case KeyCodes.KEY_ENTER :
            case KeyCodes.KEY_DOWN :
            case KeyCodes.KEY_UP :
                return true;
            default :
                return false;
        }
    }
    
    public Widget getWidget() {
    	return editor;
    }
    
	@Override
	public void setColumn(ColumnInt col) {
		this.column = col;
	}
}
