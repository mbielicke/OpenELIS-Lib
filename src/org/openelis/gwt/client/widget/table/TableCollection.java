package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


import java.util.ArrayList;

import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataObject;

/**
 * A Panel that implements CellWidget so that it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableCollection extends SimplePanel implements TableCellWidget {
    
	private VerticalPanel editor;
	private AbstractField field;

    public TableCollection() {
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return new TableCollection();
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableCollection();
    }

	public void setDisplay() {
		setEditor();
		
	}

	public void setEditor() {
		if(editor == null){
			editor = new VerticalPanel();
		}else{
			editor.clear();
		}
		ArrayList vals = (ArrayList)field.getValue();
		for(int i = 0; i < vals.size(); i++){
			editor.add(new Label((String)((DataObject)vals.get(i)).getValue()));
		}
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
