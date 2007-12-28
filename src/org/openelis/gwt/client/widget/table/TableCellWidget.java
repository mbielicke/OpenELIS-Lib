package org.openelis.gwt.client.widget.table;

import org.openelis.gwt.common.data.AbstractField;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

public interface TableCellWidget {
    public void clear();

    public void setDisplay();

    public void setEditor();

    public Widget getInstance(Node node);
    
    public TableCellWidget getNewInstance();
    
    public void saveValue();
    
    public void setField(AbstractField field);
    
    public Widget getWidget();
}
