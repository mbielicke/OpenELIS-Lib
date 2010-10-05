package org.openelis.gwt.widget.tree.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeNodeDeletedHandlers extends HasHandlers {
	
	public HandlerRegistration addBeforeNodeDeletedHandler(BeforeNodeDeletedHandler handler);
	

}
