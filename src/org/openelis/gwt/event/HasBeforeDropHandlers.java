package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;

public interface HasBeforeDropHandlers<I> extends EventHandler{

	public HandlerRegistration addBeforeDropHandler(BeforeDropHandler<I> handler);
}
