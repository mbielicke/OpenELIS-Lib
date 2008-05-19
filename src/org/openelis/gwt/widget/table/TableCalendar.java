package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.screen.ScreenBase;
import org.openelis.gwt.widget.FormCalendarWidget;

public class TableCalendar extends TableCellInputWidget {

	private FormCalendarWidget editor;
	private Label display;
	private byte begin = 0;
	private byte end = 2;
	private boolean week = false;
	private boolean enabled;
    private int width;
    public static final String TAG_NAME = "table-calendar";
    
    public TableCalendar() {
        setStyleName("TableCalendar");
    	
    }

    public TableCalendar(byte begin, byte end, boolean week) {
    	this.begin = begin;
    	this.end = end;
    	this.week = week;
        editor = new FormCalendarWidget(begin,end,week);
        setStyleName("TableCalendar");
    }

    public void setDisplay() {
        // TODO Auto-generated method stub
    	if(display == null){
    		display = new Label();
    		display.setWordWrap(false);
            display.setWidth(width+"px");
    	}
    	DatetimeRPC val = (DatetimeRPC)field.getValue();
        if (val != null)
            display.setText(val.toString());
        else
            display.setText("");
        setWidget(display);
        super.setDisplay();
    }

    public void setEditor() {
        if(!enabled)
            return;
    	if(editor == null){
    		editor = new FormCalendarWidget(begin,end,week);
            editor.setWidth((width-15)+"px");
    	}
    	DatetimeRPC val = (DatetimeRPC)field.getValue();
        if (val != null)
            editor.setText(val.toString());
        else
            editor.setText("");
        setWidget(editor);
    }

    public TableCalendar(Node node, ScreenBase screen) {
        this.screen = screen;
        // TODO Auto-generated method stub
        begin = Byte.parseByte(node.getAttributes()
                                        .getNamedItem("begin")
                                        .getNodeValue());
        end = Byte.parseByte(node.getAttributes()
                                      .getNamedItem("end")
                                      .getNodeValue());
        if (node.getAttributes().getNamedItem("week") != null)
           week = Boolean.valueOf(node.getAttributes().getNamedItem("week").getNodeValue()).booleanValue();
        else
           week = false;
        editor = new FormCalendarWidget(begin,end,week);
        setStyleName("TableCalendar");
    }

	public TableCellWidget getNewInstance() {
		// TODO Auto-generated method stub
		TableCalendar cal = new TableCalendar();
		cal.begin = begin;
		cal.end = end;
		cal.week = week;
        cal.enabled = enabled;
        cal.screen = screen;
		return cal;
	}

	public void saveValue() {
        if(!enabled)
            return;
        if(editor.getValue() != null)
            field.setValue(editor.getValue());
        super.saveValue();
	}

	public void setField(AbstractField field) {
		this.field = field;
	}

    public void enable(boolean enabled) {
       this.enabled = enabled;
    }
    
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null)
            editor.setWidth((width-15)+"px");
        if(display != null)
            display.setWidth(width+"px");
    }
}
