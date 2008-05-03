package org.openelis.gwt.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.widget.MaskedTextBox;

/**
 * A MaskedTextBox that implements a CellWidget so that it can be used in a
 * Table.
 * 
 * @author tschmidt
 * 
 */
public class TableMaskedTextBox extends TableCellInputWidget {
	
	private MaskedTextBox editor;
	private Label display;
	private String mask;
    private boolean enabled;
    private int width;
    public static final String TAG_NAME = "table-maskedbox";
	
    public TableMaskedTextBox() {
    }

    public void clear() {
    	if(editor != null)
    		editor.setText("");
    	if(display != null)
    		display.setText("");
    }

    public TableCellWidget getNewInstance() {
        TableMaskedTextBox tb = new TableMaskedTextBox();
        tb.mask = mask;
        tb.enabled = enabled;
        return tb;
    }

    public TableMaskedTextBox(Node node) {
        // TODO Auto-generated method stub
        mask = (node.getAttributes().getNamedItem("mask").getNodeValue());
    }

	public void setDisplay() {
		if(display == null){
	        display = new Label();
	        display.setWordWrap(false);
            display.setWidth(width+"px");
		}
		display.setText((String)field.getValue());
		setWidget(display);
        super.setDisplay();
	}

	public void setEditor() {
        if(!enabled)
            return;
		if(editor == null){
			editor = new MaskedTextBox();
			editor.setMask(mask);
            editor.setWidth(width+"px");
		}
		editor.setText((String)field.getValue());
		setWidget(editor);
	}

	public void saveValue() {
        editor.format();
		field.setValue(editor.getText());
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
