package org.openelis.gwt.widget;

import org.openelis.gwt.widget.table.Row;

/**
 * This class is used by Dropdown and Autocomplete widgets. 
 * This class extends TableDataRow and adds a key used to select rows
 * so they can be accessed by DB values.
 * 
 * @author tschmidt
 *
 * @param <T>
 */
public class Item<T> extends Row {
    
    protected T key;
    
    /**
     * Flag letting Widgets know if this DataSet is enabled and is available for
     * selection by users.
     */
    protected boolean enabled = true;

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
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled =  enabled;
    }
    
    public String getStyle(int index) {
    	if(enabled)
    		return null;
    	else
    		return "Disabled";
    }
    
    @SuppressWarnings("unchecked")
	public Object clone() {
    	Item<T> clone = (Item<T>)super.clone();
    	clone.setKey(key);
    	return clone;
    }

}
