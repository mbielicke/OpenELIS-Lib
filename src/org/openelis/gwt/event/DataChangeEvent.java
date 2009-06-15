package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class DataChangeEvent extends GwtEvent<DataChangeHandler>{
	
	private static Type<DataChangeHandler> TYPE;
	
    public static void fire(HasDataChangeHandlers source) {
	    if (TYPE != null) {
	      DataChangeEvent event = new DataChangeEvent();
	      source.fireEvent(event);
	    }
    }

	@Override
	protected void dispatch(DataChangeHandler handler) {
		handler.onDataChange(this);
		
	}

	@Override
	public Type<DataChangeHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<DataChangeHandler> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<DataChangeHandler>();
	    }
	    return TYPE;
	 }
	
	
}
