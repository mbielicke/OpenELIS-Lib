package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface DragStartHandler<I> extends EventHandler {

	public void onDragStart(DragStartEvent<I> event);
}
