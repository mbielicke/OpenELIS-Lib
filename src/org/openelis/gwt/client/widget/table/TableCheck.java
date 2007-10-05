package org.openelis.gwt.client.widget.table;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Node;


/**
 * Checkbox Widget that implements CellWidget interface so that it can
 * participate in a Table.
 * 
 * @author tschmidt
 * 
 */
public class TableCheck extends CheckBox implements TableCellWidget {

    /**
     * Clears value of cell to default.
     */
    public void clear() {
        setChecked(false);
    }

    /**
     * Get current value displayed by this Widget
     */
    public Object getValue() {
        return new Boolean(isChecked());
    }

    /**
     * Set value of the Checkbox based on what is in the model.
     */
    public void setValue(Object val) {
        if (val == null)
            setChecked(false);
        else
            setChecked(((Boolean)val).booleanValue());
    }

    /**
     * Returns a new TableCheck widget.
     * 
     * @return
     */
    public TableCellWidget getNewInstance() {
        return new TableCheck();
    }

    /**
     * Returns a widget for displaying the value in a table.
     */
    public Object getDisplay(String title) {
        TableCheck cb = new TableCheck();
        cb.setChecked(isChecked());
        if (title != null)
            cb.setTitle(title);
        return cb;
    }

    /**
     * Returns a widget used for entering data into this cell.
     */
    public Widget getEditor() {
        return this;
    }

    public Widget getInstance(Node node) {
        // TODO Auto-generated method stub
        return new TableCheck();
    }
}
