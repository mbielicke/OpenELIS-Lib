package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasDropEnterHandlers<I> extends EventHandler {

	public HandlerRegistration addDropEnterHandler(DropEnterHandler<I> handler);
}
