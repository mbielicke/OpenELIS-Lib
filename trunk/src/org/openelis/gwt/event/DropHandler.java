package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface DropHandler<I> extends EventHandler {

	public void onDrop(DropEvent<I> event);
}
