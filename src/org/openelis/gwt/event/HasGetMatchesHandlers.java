package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasGetMatchesHandlers extends HasHandlers {
	
	public HandlerRegistration addGetMatchesHandler(GetMatchesHandler handler);

}
