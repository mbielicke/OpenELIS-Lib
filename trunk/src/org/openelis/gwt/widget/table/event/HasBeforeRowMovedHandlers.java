package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeRowMovedHandlers<T> extends HasHandlers {
	
	public HandlerRegistration addBeforeRowMovedHandler(BeforeRowMovedHandler<T> handler);
	

}
