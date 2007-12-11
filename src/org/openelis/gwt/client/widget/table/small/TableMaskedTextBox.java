package org.openelis.gwt.client.widget.table.small;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.client.widget.MaskedTextBox;
import org.openelis.gwt.common.AbstractField;

/**
 * A MaskedTextBox that implements a CellWidget so that it can be used in a
 * Table.
 * 
 * @author tschmidt
 * 
 */
public class TableMaskedTextBox extends SimplePanel implements TableCellWidget {
	
	private MaskedTextBox editor;
	private Label display;
	private AbstractField field;
	private String mask;
	
    public TableMaskedTextBox() {
    }

    public void clear() {
    	if(editor != null)
    		editor.setText("");
    	if(display != null)
    		display.setText("");
    }

    public TableCellWidget getNewInstance() {
        return new TableMaskedTextBox();
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        TableMaskedTextBox mtb = new TableMaskedTextBox();
        mtb.mask = (node.getAttributes().getNamedItem("mask").getNodeValue());
        return mtb;
    }

	public void setDisplay() {
		if(display == null){
	        display = new Label();
	        display.setWordWrap(false);
		}
		display.setText((String)field.getValue());
		setWidget(display);
	}

	public void setEditor() {
		if(editor == null){
			editor = new MaskedTextBox();
			editor.setMask(mask);
		}
		editor.setText((String)field.getValue());
		setWidget(editor);
	}

	public void saveValue() {
		field.setValue(editor.getText());
	}

	public void setField(AbstractField field) {
		this.field = field;
	}
}
