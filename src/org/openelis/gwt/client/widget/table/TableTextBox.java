package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * A TextBox that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableTextBox extends TextBox implements TableCellWidget{
    
    public String fieldCase = "";
    
    public void clear() {
        setText("");
    }

    public Object getValue() {
        String val = getText();
        return val;
    }

    public void setValue(Object val) {
        setText((String)val);
    }

    public TableCellWidget getNewInstance() {
        TableTextBox  textbox = new TableTextBox();
        textbox.fieldCase = fieldCase;
        return textbox;
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
        TableTextBox textbox  = new TableTextBox();
        if(node.getAttributes().getNamedItem("case") != null)
            textbox.fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
        return textbox;
    }

}
