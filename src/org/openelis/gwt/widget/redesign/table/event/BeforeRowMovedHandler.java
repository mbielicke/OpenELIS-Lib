package org.openelis.gwt.widget.redesign.table.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeRowMovedHandler<T> extends EventHandler {
	
	public void onBeforeRowMoved(BeforeRowMovedEvent<T> event);
}
