package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


import java.util.ArrayList;

/**
 * A Panel that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableCollection extends VerticalPanel implements TableCellWidget {
    private ArrayList vals;

    public TableCollection() {
        super();
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object val) {
        vals = (ArrayList)val;
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return new TableCollection();
    }

    public Object getDisplay() {
        if (vals.size() == 0)
            return "";
        VerticalPanel vp = new VerticalPanel();
        for (int i = 0; i < vals.size(); i++) {
            vp.add(new HTML((String)vals.get(i)));
        }
        return vp;
    }

    public Widget getEditor() {
        return this;
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableCollection();
    }
}
