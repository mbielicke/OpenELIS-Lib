package org.openelis.gwt.widget.tree.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeNodeDeletedHandler extends EventHandler {
	
	public void onBeforeNodeDeleted(BeforeNodeDeletedEvent event);
}
