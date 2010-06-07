package org.openelis.gwt.widget;

import java.util.ArrayList;

import org.openelis.gwt.widget.table.TableDataCell;
import org.openelis.gwt.widget.table.TableDataRow;

public class Item<T> extends TableDataRow {
    
    public T itemKey;
    
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

}
