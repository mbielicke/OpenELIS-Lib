package org.openelis.gwt.widget.tree.rewrite.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeLeafCloseHandlers extends HasHandlers {
	
	public HandlerRegistration addBeforeLeafCloseHandler(BeforeLeafCloseHandler handler);

}
