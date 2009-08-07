package org.openelis.gwt.widget.table;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;

public class TableDropController extends SimpleDropController {
    private TableWidget target;
    public TableDropController(TableWidget dropTarget) {
        super(dropTarget);
        target = dropTarget;
    }
    
    @Override
    public void onDrop(DragContext context) {
        TableRow row = (TableRow)context.draggable;
        target.addRow(row.row);
        target.refresh();
        super.onDrop(context);
    }
    

}
