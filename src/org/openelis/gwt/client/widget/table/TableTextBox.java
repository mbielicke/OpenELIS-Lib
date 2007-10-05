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
        if(val != null){
            if(fieldCase.equals("lower"))
                return val.toLowerCase();
            if(fieldCase.equals("upper"))
                return val.toUpperCase();
        }
        return val;
    }

    public void setValue(Object val) {
        if(val != null) {
            if(fieldCase.equals("lower"))
                val = ((String)val).toLowerCase();
            if(fieldCase.equals("upper"))
                val = ((String)val).toUpperCase();
        }
        setText((String)val);
    }

    public TableCellWidget getNewInstance() {
        TableTextBox  textbox = new TableTextBox();
        textbox.fieldCase = fieldCase;
        return textbox;
    }

    public Object getDisplay(String title) {
        Label tl = new Label();
        if(getText() != null){
            if(fieldCase.equals("lower"))
                setText(getText().toLowerCase());
            if(fieldCase.equals("upper"))
                setText(getText().toUpperCase());
        }
        tl.setText(getText());
        tl.setWordWrap(false);
        if (title != null)
            tl.setTitle(title);
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
