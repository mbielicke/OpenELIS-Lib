package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.EventHandler;

public interface FilterHandler extends EventHandler {
	
	public void onFilter(FilterEvent event);
}
