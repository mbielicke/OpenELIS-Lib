package org.openelis.gwt.widget.redesign.tree.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeItemAddedHandler extends EventHandler {
	
	public void onBeforeItemAdded(BeforeItemAddedEvent event);
}
