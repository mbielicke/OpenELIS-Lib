package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeDropHandler<I> extends EventHandler {

	public void onBeforeDrop(BeforeDropEvent<I> event);
}
