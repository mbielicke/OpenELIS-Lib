package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeDragStartHandler<I> extends EventHandler {

	public void onBeforeDragStart(BeforeDragStartEvent<I> event);
}
