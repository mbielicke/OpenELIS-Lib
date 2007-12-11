package org.openelis.gwt.client.widget.table.small;

import org.openelis.gwt.common.AbstractField;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * An Image widget that implements CellWidget so it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableImage extends SimplePanel implements TableCellWidget {
  
    private Image editor;
    private AbstractField field;
    
    public TableImage() {
    }

    public void clear() {
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return new TableImage();
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableImage();
    }

	public void setDisplay() {
		setEditor();
		
	}

	public void setEditor() {
		if(editor == null){
			editor = new Image();
		}
		editor.setUrl((String)field.getValue());
		setWidget(editor);
	}

	public void saveValue() {
		// TODO Auto-generated method stub
		
	}

	public void setField(AbstractField field) {
		this.field = field;
		
	}
}
