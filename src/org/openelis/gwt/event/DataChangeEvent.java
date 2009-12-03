package org.openelis.gwt.event;

import org.openelis.gwt.screen.ScreenEventHandler;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

public class DataChangeEvent extends GwtEvent<DataChangeHandler>{
	
	private static Type<DataChangeHandler> TYPE;
	private Widget target;
	
	public static void fire(HasDataChangeHandlers source, Widget target) {
	    if (TYPE != null) {
		    DataChangeEvent event = new DataChangeEvent(target);
		    source.fireEvent(event);
		}
	}
	
    public static void fire(HasDataChangeHandlers source) {
	    if (TYPE != null) {
	      DataChangeEvent event = new DataChangeEvent(null);
	      source.fireEvent(event);
	    }
    }
    
    protected DataChangeEvent(Widget target) {
    	this.target = target;
    }

	@Override
	protected void dispatch(DataChangeHandler handler) {
		if(target == null || (handler instanceof ScreenEventHandler && target == ((ScreenEventHandler)handler).target))
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
