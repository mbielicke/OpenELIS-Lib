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
        if(fieldCase.equals("upper"))
            val = val.toUpperCase();
        else if(fieldCase.equals("lower"))
            val = val.toLowerCase();
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
        if(fieldCase.equals("upper"))
            tl.addStyleName("Upper");
        else if(fieldCase.equals("lower"))
            tl.addStyleName("Lower");
        return tl;
    }

    public Widget getEditor() {
        if(fieldCase.equals("upper"))
            addStyleName("Upper");
        else if(fieldCase.equals("lower"))
            addStyleName("Lower");
        return this;
    }

    public Widget getInstance(Node node) {
        TableTextBox textbox  = new TableTextBox();
        if(node.getAttributes().getNamedItem("case") != null)
            textbox.fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
        return textbox;
    }

}
