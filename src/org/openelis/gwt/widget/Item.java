package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.widget.table.TableDataCell;
import org.openelis.gwt.widget.table.TableDataRow;

/**
 * This class is used by Dropdown and Autocomplete widgets. 
 * This class extends TableDataRow and adds a key used to select rows
 * so they can be accessed by DB values.
 * 
 * @author tschmidt
 *
 * @param <T>
 */
public class Item<T> extends TableDataRow {
    
    // Refactor to key when TableDataRow is changed and before merge
    protected T itemKey;

    public Item() {
        
    }
    
    public Item(int size) {
       super(size);
    }
    
    public Item(T key, Object... display) {
        this.itemKey = key;
        cells = new ArrayList<TableDataCell>(display.length);
        for (int i= 0; i < display.length; i++)
            cells.add(new TableDataCell(display[i]));
    }
    
    public T getKey() {
        return itemKey;
    }

    public void setKey(T itemKey) {
        this.itemKey = itemKey;
    }

}
