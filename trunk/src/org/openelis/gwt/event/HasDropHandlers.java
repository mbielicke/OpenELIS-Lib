package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface HasDropHandlers<I> extends EventHandler {

	public HandlerRegistration addDropHandler(DropHandler<I> handler);
}
