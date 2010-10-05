package org.openelis.gwt.widget.redesign.tree.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeNodeOpenHandlers extends HasHandlers {
	
	public HandlerRegistration addBeforeNodeOpenHandler(BeforeNodeOpenHandler handler);

}
