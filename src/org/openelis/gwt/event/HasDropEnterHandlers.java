package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasDropEnterHandlers<I> extends HasHandlers {

	public HandlerRegistration addDropEnterHandler(DropEnterHandler<I> handler);
}
