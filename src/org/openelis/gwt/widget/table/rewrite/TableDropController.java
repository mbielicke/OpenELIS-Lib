package org.openelis.gwt.widget.table.rewrite;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.allen_sauer.gwt.dnd.client.drop.AbstractIndexedDropController;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.Window;

public class TableDropController extends SimpleDropController {
    private TableWidget target;
    public TableDropController(TableWidget dropTarget) {
        super(dropTarget);
        target = dropTarget;
    }
    
    @Override
    public void onDrop(DragContext context) {
        TableRow row = (TableRow)context.draggable;
        target.model.addRow(row.row);
        target.model.refresh();
        super.onDrop(context);
    }
    

}
