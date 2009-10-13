package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeRowMovedHandler extends EventHandler {
	
	public void onBeforeRowMoved(BeforeRowMovedEvent event);
}
