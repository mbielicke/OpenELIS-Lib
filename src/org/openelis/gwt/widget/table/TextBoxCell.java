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
import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.resources.TableCSS;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class implements the CellRenderer and CellEditor interfaces and is used
 * to edit and render cells in a Table using a TextBox<T>
 * 
 * @author tschmidt
 * 
 * @param <T>
 */
public class TextBoxCell implements CellRenderer, CellEditor, IsWidget, HasWidgets.ForIsWidget {

    /**
     * Editor used by this cell
     */
    private TextBox     editor;

    private boolean     query;

    private ColumnInt   column;
    
    protected TableCSS  css;
    
    public TextBoxCell() {
    	
    }
    /**
     * Constructor that takes the editor to be used as a param
     * 
     * @param editor
     */
    public TextBoxCell(final TextBox editor) {
    	setEditor(editor);
    }
    
    public void setEditor(TextBox editor) {
    	css = OpenELISResources.INSTANCE.table();
    	css.ensureInjected();
        this.editor = editor;
        editor.setEnabled(true);
        editor.setStyleName(css.TableTextBox());
        editor.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				column.finishEditing();
			}
		});
    }

    public String display(Object value) {
        editor.setQueryMode(false);
        if(editor.getHelper().isCorrectType(value))
        	return editor.getHelper().format(value);
        else
        	return DataBaseUtil.asString(value);
    }

    /**
     * Returns the current widget set as this cells editor.
     */
    @SuppressWarnings("rawtypes")
	public void startEditing(Object value, Container container, GwtEvent event) {
    	if(!editor.getHelper().isCorrectType(value))
    		editor.setText(DataBaseUtil.asString(value));
    	else 
    		editor.setValue(value);
        editor.setWidth(container.getWidth()+"px");
        container.setEditor(editor);
        editor.selectAll();
    }

    public void render(HTMLTable table, int row, int col, Object value) {
   		table.setText(row, col, display(value));
    }

    public void renderQuery(HTMLTable table, int row, int col, QueryData qd) {
        editor.setQueryMode(true);
        editor.setQuery(qd);
        table.setText(row, col, editor.getText());
    }

    public ArrayList<LocalizedException> validate(Object value) {
        return editor.getHelper().validate(value);
    }

    public Object finishEditing() {
    	editor.finishEditing();
        if (query)
            return editor.getQuery();
        
        try {
        	return editor.getHelper().getValue(editor.getText());
        }catch(Exception e){
       		return editor.getText();
        }
        
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
	public void setColumn(ColumnInt col) {
		this.column = col;
	}
	@Override
	public void add(Widget w) {
		assert w instanceof TextBox;
		
		setEditor((TextBox)w);
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
		assert w instanceof TextBox;
		
		setEditor((TextBox)w);
	}
	@Override
	public boolean remove(IsWidget w) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return new Label("TextBox Cell");
	}

}
