package org.openelis.gwt.event;

import com.google.gwt.event.shared.EventHandler;

public interface DataChangeHandler extends EventHandler {
	
	public Object val = null;
	
	public void onDataChange(DataChangeEvent event);

}
