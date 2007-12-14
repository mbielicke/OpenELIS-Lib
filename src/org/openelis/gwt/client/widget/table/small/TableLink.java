package org.openelis.gwt.client.widget.table.small;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;

import org.openelis.gwt.common.AbstractField;


/**
 * A HTML Widget that implements CellWidget so it can be used in a table
 * 
 * @author tschmidt
 * 
 */
public class TableLink extends SimplePanel implements TableCellWidget {
    
    private HTML editor;
    private AbstractField field;
    
    public TableLink() {
        editor = new HTML();
        editor.setWordWrap(false);
    }

    public void clear() {
        // TODO Auto-generated method stub
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableLink();
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveValue() {
        // TODO Auto-generated method stub
        
    }

    public void setDisplay() {
        setEditor();
    }

    public void setEditor() {
        editor.setHTML("<div><a>" + (String)field.getValue() + "</a></div>");
        addStyleName("encounters");
        
    }

    public void setField(AbstractField field) {
        this.field = field;
        
    }
}
