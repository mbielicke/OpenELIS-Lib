package org.openelis.gwt.widget.tree.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeLeafOpenHandlers extends HasHandlers {
	
	public HandlerRegistration addBeforeLeafOpenHandler(BeforeLeafOpenHandler handler);

}
