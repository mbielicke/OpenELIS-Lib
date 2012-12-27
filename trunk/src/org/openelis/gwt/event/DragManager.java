package org.openelis.gwt.event;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;

public interface DragManager {
    
    public void previewDragStart(DragContext context) throws VetoDragException;
    
    public void previewDragEnd(DragContext context) throws VetoDragException;
    
    public void dragStarted(DragContext context);
    
    public void dragEnded(DragContext context);

}
