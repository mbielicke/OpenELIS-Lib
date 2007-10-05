package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.FormCalendarWidget;
import org.openelis.gwt.common.DatetimeRPC;

import java.util.Date;

public class TableCalendar extends FormCalendarWidget implements TableCellWidget {

    public TableCalendar() {
    }

    public TableCalendar(byte begin, byte end, boolean week) {
        super(begin, end, week);
        // TODO Auto-generated constructor stub
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        Date date = new Date(getText().replaceAll("-", "/"));
        return DatetimeRPC.getInstance(begin, end, date);
    }

    public void setValue(Object val) {
        // TODO Auto-generated method stub
        if (val != null)
            setText(((DatetimeRPC)val).toString());
        else
            setText("");
    }

    public Object getDisplay(String title) {
        // TODO Auto-generated method stub
        Label tl = new Label();
        tl.setText(getText());
        tl.setWordWrap(false);
        if (title != null)
            tl.setTitle(title);
        return tl;
    }

    public Widget getEditor() {
        // TODO Auto-generated method stub
        return this;
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
}
