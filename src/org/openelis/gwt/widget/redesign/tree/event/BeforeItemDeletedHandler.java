package org.openelis.gwt.widget.redesign.tree.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeItemDeletedHandler extends EventHandler {
	
	public void onBeforeItemDeleted(BeforeItemDeletedEvent event);
}
