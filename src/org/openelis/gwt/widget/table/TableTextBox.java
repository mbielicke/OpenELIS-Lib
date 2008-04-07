package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;


/**
 * A TextBox that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableTextBox extends TableCellInputWidget {
    
    public String fieldCase = "";
    private int length = -1;
    private TextBox editor;
    private Label display;
    private boolean enabled;
    private int width;
    public static final String TAG_NAME = "table-textbox";
    
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
        textbox.length = length;
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
            display.setWidth(width+"px");
    	}
        if(field.getValue() != null)
            display.setText(field.getValue().toString());
        else
            display.setText("");
        setWidget(display);
        super.setDisplay();
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
    		
    		if(length > -1)
    			editor.setMaxLength(length);
            editor.setWidth(width+"px");
    	}
        if(field.getValue() != null)
            editor.setText(field.getValue().toString());
        else
            editor.setText("");
        setWidget(editor);
    }

    public TableTextBox(Node node) {
        if(node.getAttributes().getNamedItem("case") != null)
            fieldCase = node.getAttributes().getNamedItem("case").getNodeValue();
        
        if (node.getAttributes().getNamedItem("max") != null) 
        	length = Integer.parseInt(node.getAttributes().getNamedItem("max").getNodeValue());
    }

	public void saveValue() {
		String val = editor.getText();
        if(fieldCase.equals("upper"))
            val = val.toUpperCase();
        else if(fieldCase.equals("lower"))
            val = val.toLowerCase();
		field.setValue(val);
        super.saveValue();
	}

	public void setField(AbstractField field) {
		this.field = field;
	}

    public void enable(boolean enabled) {
       this.enabled = enabled;
        
    }
    
    public void setCellWidth(int width){
        this.width = width;
        if(editor != null)
            editor.setWidth(width+"px");
        if(display != null)
            display.setWidth(width+"px");
    }

}
