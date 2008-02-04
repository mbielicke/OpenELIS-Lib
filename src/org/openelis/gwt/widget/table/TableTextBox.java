package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.data.AbstractField;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * A TextBox that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableTextBox extends SimplePanel implements TableCellWidget{
    
    public String fieldCase = "";
    private TextBox editor;
    private Label display;
    private AbstractField field;
    private boolean enabled;
    
    public TableTextBox() {
    	
    }
    
    public void clear() {
    	if(editor != null)
    		editor.setText("");
    	if(display != null)
    		display.setText("");
    }

    public TableCellWidget getNewInstance() {
        TableTextBox  textbox = new TableTextBox();
        textbox.fieldCase = fieldCase;
        textbox.enabled = enabled;
        return textbox;
    }

    public void setDisplay() {
    	if(display == null){
    		display = new Label();
            display.setWordWrap(false);
            if(fieldCase.equals("upper"))
                display.addStyleName("Upper");
            else if(fieldCase.equals("lower"))
                display.addStyleName("Lower");
    	}
        display.setText((String)field.getValue());
        setWidget(display);
    }

    public void setEditor() {
        if(!enabled)
            return;
    	if(editor == null){
    		editor = new TextBox();
    		if(fieldCase.equals("upper"))
    			editor.addStyleName("Upper");
    		else if(fieldCase.equals("lower"))
    			editor.addStyleName("Lower");
    	}
    	editor.setText((String)field.getValue());
        setWidget(editor);
    }

    public Widget getInstance(Node node) {
        TableTextBox textbox  = new TableTextBox();
        if(node.getAttributes().getNamedItem("case") != null)
            textbox.fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
        return textbox;
    }

	public void saveValue() {
		String val = editor.getText();
        if(fieldCase.equals("upper"))
            val = val.toUpperCase();
        else if(fieldCase.equals("lower"))
            val = val.toLowerCase();
		field.setValue(val);
	}

	public void setField(AbstractField field) {
		this.field = field;
	}

    public void enable(boolean enabled) {
       this.enabled = enabled;
        
    }

}
