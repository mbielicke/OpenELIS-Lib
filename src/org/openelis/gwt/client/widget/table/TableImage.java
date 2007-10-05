package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * An Image widget that implements CellWidget so it can be used in a table.
 * 
 * @author tschmidt
 * 
 */
public class TableImage extends Image implements TableCellWidget {
    private String title;
    private boolean dynamic;

    public TableImage() {
        super();
    }

    public void clear() {
    }

    public Object getValue() {
        return null;
    }

    public void setValue(Object val) {
        setUrl((String)val);
    }

    public TableCellWidget getNewInstance() {
        // TODO Auto-generated method stub
        return new TableImage();
    }

    public Object getDisplay(String title) {
        TableImage ti = new TableImage();
        ti.setUrl(getUrl());
        if (title != null)
            ti.setTitle(title);
        return ti;
    }

    public Widget getEditor() {
        return this;
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableImage();
    }
}
