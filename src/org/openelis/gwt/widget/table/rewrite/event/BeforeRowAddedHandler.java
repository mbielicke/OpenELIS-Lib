package org.openelis.gwt.widget.table.rewrite.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeRowAddedHandler extends EventHandler {
	
	public void onBeforeRowAdded(BeforeRowAddedEvent event);
}
