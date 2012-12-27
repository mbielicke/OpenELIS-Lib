package org.openelis.gwt.event;

import com.allen_sauer.gwt.dnd.client.DragController;

public interface HasDragController {
    
    public DragController getDragController();
    
    public void setDragController(DragController controller);

}
