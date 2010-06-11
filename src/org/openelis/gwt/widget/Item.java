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
    
    protected T key;

    public Item() {
        
    }
    
    public Item(int size) {
       super(size);
    }
    
    public Item(T key, Object... display) {
        super(display);
        this.key = key;
    }
    
    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }
    
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if(!(obj instanceof Item))
            return false;
        if(key == null && ((Item<T>)obj).key != null)
            return false;
        else if(key == null)
            return true;
        return key.equals(((Item<T>)obj).key);
    }

}
