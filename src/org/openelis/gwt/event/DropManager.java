package org.openelis.gwt.event;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.user.client.ui.Widget;

public interface DropManager {
    
    public void previewDrop(DragContext context) throws VetoDragException;
    
    public void dropEnded(DragContext context);
    
    public void onEnter(DragContext context);
    
    public void onLeave(DragContext context);
    
    public Widget getDropWidget(DragContext context);

}
