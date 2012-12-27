package org.openelis.gwt.event;

import com.allen_sauer.gwt.dnd.client.drop.DropController;

public interface HasDropController {
    
    public DropController getDropController();
    
    public void setDropController(DropController controller);

}
