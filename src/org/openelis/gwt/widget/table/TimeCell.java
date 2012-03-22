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

public class TimeCell implements CellRenderer, CellEditor {

	private TextBox<String> editor;
	private Column          column;
    private boolean         query;
	
	public TimeCell() {
		editor = new TextBox<String>();
		editor.setEnabled(true);
		editor.setStyleName("TableTextBox");
		editor.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				column.getTable().finishEditing();
			}
		});
	}
	
	@Override
	public void startEditing(Object value, Container container, GwtEvent event) {
		if(value instanceof Double)
			editor.setValue(getTime((Double)value));
		else
			editor.setText(value.toString());
		editor.setWidth(container.getWidth()+"px");
		container.setEditor(editor);
		editor.selectAll();
	}

	@Override
	public void startEditingQuery(QueryData qd, Container container,
			GwtEvent event) {
		
	}

	@Override
	public Object finishEditing() {
        if (query)
            return editor.getQuery();
        
        if(!editor.hasExceptions())
        	return getHours(editor.getValue());
        else
        	return editor.getText();
	}

	@Override
	public ArrayList<LocalizedException> validate() {
        if (query) 
            return editor.getValidateExceptions();

        return editor.getValidateExceptions();
	}

	@Override
	public boolean ignoreKey(int keyCode) {
		return false;
	}

	@Override
	public Widget getWidget() {
		return editor;
	}

	@Override
	public String display(Object value) {
		if(value instanceof Double)
			return getTime((Double)value);
		else
			return value.toString();
	}

	@Override
	public void render(HTMLTable table, int row, int col, Object value) {
		table.setText(row, col, display(value));
	}

	@Override
	public void renderQuery(HTMLTable table, int row, int col, QueryData qd) {
		
	}
	
    private Double getHours(String time) {
    	double hours = 0.0 ,mins = 0.0;
    	String tm[];
    	
    	if(time == null || "".equals(time))
    		return null;
    	
        tm = time.split(":");
        try {
        	hours = Double.parseDouble(tm[0]);
        }catch(Exception e) {
        	hours = 0.0;
        }
        
        if(tm.length > 1) {
        	try {
        		mins = Double.parseDouble(tm[1]) / 60.0;
        	}catch(Exception e) {
        		mins = 0.0;
        	}
        }
        
        return new Double(hours += mins);
    }
    
    private String getTime(Double hours) {
    	int h,m;
    	
        if (hours != null && hours.doubleValue() > 0.0) {
            h = (int)Math.floor(hours);
            m = 0;
            if(h == 0)
                m = (int)Math.round(hours  * 60);
            else
                m = (int)Math.round((hours % h) * 60);
            return h + ":" + m;
        } 
  
        return "";      
    }
    
	@Override
	public void setColumn(Column col) {
		this.column = col;
	}
    
}
