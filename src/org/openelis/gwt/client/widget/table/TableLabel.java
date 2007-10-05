package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * A Label that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableLabel extends Label implements TableCellWidget {
    public TableLabel() {
        super();
    }

    public void clear() {
        setText("");
    }

    public Object getValue() {
        return getText();
    }

    public void setValue(Object val) {
        if (val instanceof Integer)
            setText(((Integer)val).toString());
        else if (val == null)
            setText(" ");
        else
            setText((String)val);
    }

    public TableCellWidget getNewInstance() {
        return new TableLabel();
    }

    public Object getDisplay(String title) {
        Label tl = new Label();
        tl.setText(getText());
        tl.setWordWrap(false);
        DOM.setStyleAttribute(tl.getElement(), "overflow", "hidden");
        if (title != null)
            tl.setTitle(title);
        return tl;
    }

    public Widget getEditor() {
        return null;
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableLabel();
    }
}
