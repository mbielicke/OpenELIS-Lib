package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasDropHandlers<I> extends HasHandlers {

	public HandlerRegistration addDropHandler(DropHandler<I> handler);
}
