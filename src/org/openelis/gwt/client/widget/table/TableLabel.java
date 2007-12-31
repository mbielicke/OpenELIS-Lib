package org.openelis.gwt.client.widget.table;

import org.openelis.gwt.common.data.AbstractField;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * A Label that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableLabel extends SimplePanel implements TableCellWidget {
	
	private Label editor;
	private AbstractField field;
	
    public TableLabel() {

    }

    public void clear() {
    	if(editor != null)
    		editor.setText("");
    }

    public TableCellWidget getNewInstance() {
        return new TableLabel();
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableLabel();
    }

	public void setDisplay() {
		setEditor();
		
	}

	public void setEditor() {
		if(editor == null){
			editor = new Label();
			editor.setWordWrap(false);
		}
		Object val = field.getValue();
        if (val instanceof Integer)
            editor.setText(((Integer)val).toString());
        else if (val == null)
            editor.setText(" ");
        else
            editor.setText((String)val);
        setWidget(editor);
	}

	public void saveValue() {
		// TODO Auto-generated method stub
		
	}

	public void setField(AbstractField field) {
		this.field = field;
		
	}

    public void enable(boolean enabled) {
        // TODO Auto-generated method stub
        
    }
}
