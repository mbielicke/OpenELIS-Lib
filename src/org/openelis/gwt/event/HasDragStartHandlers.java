package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasDragStartHandlers<I> extends HasHandlers {

	public HandlerRegistration addStartHandler(DragStartHandler<I> handler);
}
