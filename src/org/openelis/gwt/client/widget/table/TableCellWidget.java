package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public interface TableCellWidget {
    public void clear();

    public Object getValue();

    public void setValue(Object val);

    public Object getDisplay();

    public Widget getEditor();

    public Widget getInstance(Node node);
}
