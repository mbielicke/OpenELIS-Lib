package org.openelis.gwt.widget.table;

import org.openelis.gwt.common.data.AbstractField;

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
    public static final String TAG_NAME = "table-image";
    
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
    
    public TableImage(Node node){
        this();
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

    public void enable(boolean enabled) {
        // TODO Auto-generated method stub
        
    }

    public void setCellWidth(int width) {
        // TODO Auto-generated method stub
        
    }
}
