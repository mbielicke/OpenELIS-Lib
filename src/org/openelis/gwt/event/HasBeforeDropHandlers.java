package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeDropHandlers<I> extends HasHandlers{

	public HandlerRegistration addBeforeDropHandler(BeforeDropHandler<I> handler);
}
