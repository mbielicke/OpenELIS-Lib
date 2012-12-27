package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeRowDeletedHandler extends EventHandler {
	
	public void onBeforeRowDeleted(BeforeRowDeletedEvent event);
}
