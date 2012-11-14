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
import java.util.Iterator;

import org.openelis.gwt.common.DataBaseUtil;
import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.calendar.Calendar;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class implements the CellRenderer and CellEditor interfaces and is used
 * to edit and render cells in a Table using a Calendar
 * 
 * @author tschmidt
 * 
 * @param <T>
 */
public class CalendarCell implements CellRenderer, CellEditor, IsWidget, HasWidgets.ForIsWidget {
    /**
     * Editor used by this cell
     */
    private Calendar  editor;
    private boolean   query;
    private ColumnInt column;

    
    public CalendarCell() {
    	
    }
    
    /**
     * Constructor that takes the editor to be used as a param
     * 
     * @param editor
     */
    public CalendarCell(Calendar editor) {
    	setEditor(editor);
    }
    
    public void setEditor(Calendar editor) {
        this.editor = editor;
        editor.setEnabled(true);
       // editor.setStyleName("TableCalendar");
        editor.addBlurHandler(new BlurHandler() {
			public void onBlur(BlurEvent event) {
				column.finishEditing();
			}
		});
    }

    /**
     * Method to return the editor set for this cell
     */
    @SuppressWarnings("rawtypes")
	public void startEditing(Object value, Container container, GwtEvent event) {
        if(value instanceof Datetime)
        	editor.setValue((Datetime)value);
        else
        	editor.setText(DataBaseUtil.asString(value));
        editor.setWidth(container.getWidth()+"px");
        container.setEditor(editor);
        editor.selectAll();
    }

    @SuppressWarnings("rawtypes")
	public void startEditingQuery(QueryData qd, Container container, GwtEvent event) {
        query = true;
        editor.setQueryMode(true);
        editor.setQuery(qd);
        editor.setWidth(container.getWidth()+"px");
        container.setEditor(editor);
    }

    public Object finishEditing() {
    	editor.finishEditing();
        if (query) 
            return editor.getQuery();
        
        if(!editor.hasExceptions())
        	return editor.getValue();
        else
        	return editor.getText();
    }

    public ArrayList<LocalizedException> validate(Object value) {
        if (!query) 
        	return editor.getHelper().validate(value);
        else {
        	editor.setQuery((QueryData)value);
        	return editor.getValidateExceptions();
        }
    }

    /**
     * Gets Formatted value from editor and sets it as the cells display
     */
    public void render(HTMLTable table, int row, int col, Object value) {
        table.setText(row, col, display(value));
    }

    public String display(Object value) {
        query = false;
        editor.setQueryMode(false);
        if(value instanceof Datetime)
        	return editor.getHelper().format((Datetime)value);
        else
        	return DataBaseUtil.asString(value);
    }

    /**
     * Sets the QueryData to the editor and sets the Query string into the cell
     * text
     */
    public void renderQuery(HTMLTable table, int row, int col, QueryData qd) {
        query = true;
        editor.setQueryMode(true);
        editor.setQuery(qd);
        table.setText(row, col, editor.getText());
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

	@Override
	public void add(Widget w) {
		assert w instanceof Calendar;
		
		setEditor((Calendar)w);
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Iterator<Widget> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Widget w) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void add(IsWidget w) {
		assert w instanceof Calendar;
		
		setEditor((Calendar)w);
	}

	@Override
	public boolean remove(IsWidget w) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
