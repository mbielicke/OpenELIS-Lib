package org.openelis.gwt.widget.tree.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeNodeCloseHandlers extends HasHandlers {
	
	public HandlerRegistration addBeforeNodeCloseHandler(BeforeNodeCloseHandler handler);

}
