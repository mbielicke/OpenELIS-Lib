package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.EventHandler;

public interface BeforeSortHandler extends EventHandler {
	
	public void onBeforeSort(BeforeSortEvent event);
}
