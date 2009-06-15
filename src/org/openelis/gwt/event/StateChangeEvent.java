package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class StateChangeEvent<I> extends GwtEvent<StateChangeHandler<I>>{
	
	private static Type<StateChangeHandler<?>> TYPE;
	private I state;
	
    public static <I> void fire(HasStateChangeHandlers source, I state) {
	    if (TYPE != null) {
	      StateChangeEvent<I> event = new StateChangeEvent<I>(state);
	      source.fireEvent(event);
	    }
    }

    protected StateChangeEvent(I state) {
    	this.state = state;
    }
    
	@Override
	protected void dispatch(StateChangeHandler<I> handler) {
		handler.onStateChange(this);
		
	}

	@Override
	public final Type<StateChangeHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<StateChangeHandler<?>> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<StateChangeHandler<?>>();
	    }
	    return TYPE;
	 }
	
	public I getState() {
		return state;
	}
}
