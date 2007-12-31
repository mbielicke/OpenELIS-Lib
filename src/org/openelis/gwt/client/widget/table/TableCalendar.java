package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.FormCalendarWidget;
import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.common.data.AbstractField;

public class TableCalendar extends SimplePanel implements TableCellWidget {

	private FormCalendarWidget editor;
	private Label display;
	private AbstractField field;
	private byte begin = 0;
	private byte end = 2;
	private boolean week = false;
	private boolean enabled;
    
    public TableCalendar() {
    	
    }

    public TableCalendar(byte begin, byte end, boolean week) {
    	this.begin = begin;
    	this.end = end;
    	this.week = week;
        editor = new FormCalendarWidget(begin,end,week);
    }

    public void setDisplay() {
        // TODO Auto-generated method stub
    	if(display == null){
    		display = new Label();
    		display.setWordWrap(false);
    	}
    	DatetimeRPC val = (DatetimeRPC)field.getValue();
        if (val != null)
            display.setText(val.toString());
        else
            display.setText("");
        setWidget(display);
    }

    public void setEditor() {
        if(!enabled)
            return;
    	if(editor == null){
    		editor = new FormCalendarWidget(begin,end,week);
    	}
    	DatetimeRPC val = (DatetimeRPC)field.getValue();
        if (val != null)
            editor.setText(val.toString());
        else
            editor.setText("");
        setWidget(editor);
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        byte begin = Byte.parseByte(node.getAttributes()
                                        .getNamedItem("begin")
                                        .getNodeValue());
        byte end = Byte.parseByte(node.getAttributes()
                                      .getNamedItem("end")
                                      .getNodeValue());
        if (node.getAttributes().getNamedItem("week") != null)
        	
            return new TableCalendar(begin,
                                     end,
                                     Boolean.valueOf(node.getAttributes()
                                                         .getNamedItem("week")
                                                         .getNodeValue())
                                            .booleanValue());
        else
            return new TableCalendar(begin, end, false);
    }

	public TableCellWidget getNewInstance() {
		// TODO Auto-generated method stub
		TableCalendar cal = new TableCalendar();
		cal.begin = begin;
		cal.end = end;
		cal.week = week;
        cal.enabled = enabled;
		return cal;
	}

	public void saveValue() {
        field.setValue(editor.getValue());
	}

	public void setField(AbstractField field) {
		this.field = field;
	}

    public void enable(boolean enabled) {
       this.enabled = enabled;
    }
}
