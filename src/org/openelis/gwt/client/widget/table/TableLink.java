package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * A HTML Widget that implements CellWidget so it can be used in a table
 * 
 * @author tschmidt
 * 
 */
public class TableLink extends HTML implements TableCellWidget {
    public TableLink() {
        super();
    }

    public void clear() {
        // TODO Auto-generated method stub
    }

    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setValue(Object val) {
        setHTML("<div><a>" + (String)val + "</a></div>");
        addStyleName("encounters");
    }

    public Object getDisplay() {
        HTML hl = new HTML(getHTML());
        hl.setWordWrap(false);
        return hl;
    }

    public Widget getEditor() {
        // TODO Auto-generated method stub
        return this;
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableLink();
    }
}
