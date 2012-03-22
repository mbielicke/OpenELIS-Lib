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
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class implements the CellRenderer and CellEditor interfaces and is used
 * to edit and render cells in a Table using a TextBox<T>
 * 
 * @author tschmidt
 * 
 * @param <T>
 */
public class TextBoxCell implements CellRenderer, CellEditor {

    /**
     * Editor used by this cell
     */
    private TextBox editor;

    private boolean    query;

    private Column     column;
    /**
     * Constructor that takes the editor to be used as a param
     * 
     * @param editor
     */
    public TextBoxCell(final TextBox editor) {
        this.editor = editor;
        editor.setEnabled(true);
        editor.setStyleName("TableTextBox");
        editor.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				column.getTable().finishEditing();
			}
		});
    }

    public String display(Object value) {
        editor.setQueryMode(false);
        try {
        	editor.setValue(value);
        }catch(ClassCastException e) {
        	return value.toString();
        }
        return editor.getText();
    }

    /**
     * Returns the current widget set as this cells editor.
     */
    @SuppressWarnings("rawtypes")
	public void startEditing(Object value, Container container, GwtEvent event) {
    	if(column.getTable().hasExceptions(column.getTable().getEditingRow(),column.getTable().getEditingCol()))
    		editor.setText(value.toString());
    	else 
    		editor.setValue(value);
        editor.setWidth(container.getWidth()+"px");
        container.setEditor(editor);
        editor.selectAll();
    }

    public void render(HTMLTable table, int row, int col, Object value) {
    	if(column.getTable().hasExceptions(row, col))
    		table.setText(row, col, value.toString());
    	else
    		table.setText(row, col, display(value));
    }

    public void renderQuery(HTMLTable table, int row, int col, QueryData qd) {
        editor.setQueryMode(true);
        editor.setQuery(qd);
        table.setText(row, col, editor.getText());
    }

    public ArrayList<LocalizedException> validate() {
        if (query) 
            return editor.getValidateExceptions();

        return editor.getValidateExceptions();
    }

    public Object finishEditing() {
        if (query)
            return editor.getQuery();
        
        if(!editor.hasExceptions())
        	return editor.getValue();
        else
        	return editor.getText();
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
        return false;
    }
    
    public Widget getWidget() {
    	return editor;
    }
    
	@Override
	public void setColumn(Column col) {
		this.column = col;
	}

}
