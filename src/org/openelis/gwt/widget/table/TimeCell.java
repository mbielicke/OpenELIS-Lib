package org.openelis.gwt.widget.table;

import java.util.ArrayList;

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.data.QueryData;
import org.openelis.gwt.widget.TextBox;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Widget;

public class TimeCell implements CellRenderer<Double>, CellEditor<Double> {

	private TextBox<String> editor;
	
	public TimeCell() {
		editor = new TextBox<String>();
		editor.setEnabled(true);
		editor.setStyleName("TableTextBox");
	}
	
	@Override
	public void startEditing(Double value, Container container, GwtEvent event) {
		editor.setValue(getTime(value));
		editor.setWidth(container.getWidth()+"px");
		container.setEditor(editor);
	}

	@Override
	public void startEditingQuery(QueryData qd, Container container,
			GwtEvent event) {
		
	}

	@Override
	public Object finishEditing() {
		return getHours(editor.getValue());
	}

	@Override
	public ArrayList<LocalizedException> validate() {
        editor.validateValue();
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
	public String display(Double value) {
		return getTime(value);
	}

	@Override
	public void render(HTMLTable table, int row, int col, Double value) {
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
        hours = Double.parseDouble(tm[0]);
        
        if(tm.length > 1) 
            mins = Double.parseDouble(tm[1]) / 60.0;
        
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
    
}
