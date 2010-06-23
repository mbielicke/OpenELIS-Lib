package org.openelis.gwt.widget.redesign.table.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeRowAddedHandler extends EventHandler {
	
	public void onBeforeRowAdded(BeforeRowAddedEvent event);
}
