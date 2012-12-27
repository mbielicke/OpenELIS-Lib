package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasTableValueChangeHandlers extends HasHandlers {

	public HandlerRegistration addTableValueChangeHandler(TableValueChangeHandler handler);
}
