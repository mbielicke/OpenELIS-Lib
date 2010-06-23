package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface ScrollBarHandler extends EventHandler {
	
	public Object val = null;
	
	public void onScroll(ScrollBarEvent event);

}
