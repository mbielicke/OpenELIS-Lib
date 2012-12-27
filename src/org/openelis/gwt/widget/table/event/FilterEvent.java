package org.openelis.gwt.widget.table.event;

import com.google.gwt.event.shared.GwtEvent;

public class FilterEvent extends GwtEvent<FilterHandler> {
	
	private static Type<FilterHandler> TYPE;
	
	public static void fire(HasFilterHandlers source) {
		if(TYPE != null) {
			FilterEvent event = new FilterEvent();
			source.fireEvent(event);
		}
	}
	
	protected FilterEvent() {
	}

	@Override
	protected void dispatch(FilterHandler handler) {
		handler.onFilter(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<FilterHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<FilterHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<FilterHandler>();
		}
		return TYPE;
	}	

}
