package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasBeforeGetMatchesHandlers extends HasHandlers {
	
	public HandlerRegistration addBeforeGetMatchesHandler(BeforeGetMatchesHandler handler);
	

}
