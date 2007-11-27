package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.MaskedTextBox;

/**
 * A MaskedTextBox that implements a CellWidget so that it can be used in a
 * Table.
 * 
 * @author tschmidt
 * 
 */
public class TableMaskedTextBox extends MaskedTextBox implements TableCellWidget {
    public TableMaskedTextBox() {
        super();
    }

    public void clear() {
        setText("");
    }

    public Object getValue() {
        return getText();
    }

    public void setValue(Object val) {
        setText((String)val);
    }

    public TableCellWidget getNewInstance() {
        return new TableMaskedTextBox();
    }

    public Object getDisplay() {
        Label tl = new Label();
        tl.setText(getText());
        tl.setWordWrap(false);
        return tl;
    }

    public Widget getEditor() {
        return this;
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        TableMaskedTextBox mtb = new TableMaskedTextBox();
        mtb.setMask(node.getAttributes().getNamedItem("mask").getNodeValue());
        return mtb;
    }
}
