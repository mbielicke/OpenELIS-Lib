/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.DatetimeRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DateField;
import org.openelis.gwt.common.data.QueryDateField;
import org.openelis.gwt.common.data.QueryField;
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
        setStyleName("ScreenCalendar");
    	
    }

    public TableCalendar(byte begin, byte end, boolean week) {
    	this.begin = begin;
    	this.end = end;
    	this.week = week;
        editor = new FormCalendarWidget(begin,end,week);
        editor.init();
        setStyleName("ScreenCalendar");
    }

    public void setDisplay() {
        // TODO Auto-generated method stub
    	if(display == null){
    		display = new Label();
    		display.setWordWrap(false);
            display.setWidth(width+"px");
    	}
        String val = "";
        if(field instanceof DateField){
            DatetimeRPC valDate = (DatetimeRPC)field.getValue();
            if(valDate != null)
                val = valDate.toString();
        }else
            val = (String)field.getValue();
        display.setText(val);
        setWidget(display);
        super.setDisplay();
    }

    public void setEditor() {
        if(!enabled)
            return;
    	if(editor == null){
    		editor = new FormCalendarWidget(begin,end,week);
            editor.init();
            editor.setWidth((width-15)+"px");
    	}
        String val = "";
        if(field instanceof DateField){
            DatetimeRPC valDate = (DatetimeRPC)field.getValue();
            if(valDate != null)
                val = valDate.toString();
        }else
            val = (String)field.getValue();
        editor.setText(val);
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
        editor.init();
        setStyleName("ScreenCalendar");
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
        if(field instanceof QueryDateField)
            field.setValue(editor.getText());
        else
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
