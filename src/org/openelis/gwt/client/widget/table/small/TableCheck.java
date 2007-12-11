package org.openelis.gwt.client.widget.table.small;

import org.openelis.gwt.common.AbstractField;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * Checkbox Widget that implements CellWidget interface so that it can
 * participate in a Table.
 * 
 * @author tschmidt
 * 
 */
public class TableCheck extends SimplePanel implements TableCellWidget {

	private CheckBox editor;
	private AbstractField field;
	
	public TableCheck() {
		editor = new CheckBox();
		setWidget(editor);
	}
    /**
     * Clears value of cell to default.
     */
    public void clear() {
    	if(editor != null)
    		editor.setChecked(false);
    }

    /**
     * Returns a new TableCheck widget.
     * 
     * @return
     */
    public TableCellWidget getNewInstance() {
        return new TableCheck();
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableCheck();
    }

	public void setDisplay() {
		//setEditor();
	}

	public void setEditor() {
        /*if(field.getValue() != null)
        	editor.setChecked(((Boolean)field.getValue()).booleanValue());
        else
        	editor.setChecked(false);
        */
	}

	public void saveValue() {
		field.setValue(new Boolean(editor.isChecked()));
		
	}

	public void setField(AbstractField field) {
		this.field = field;
		if(field.getValue() != null)
			editor.setChecked(((Boolean)field.getValue()).booleanValue());
		else
			editor.setChecked(false);
	}
}
