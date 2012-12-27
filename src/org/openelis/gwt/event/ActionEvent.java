package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class ActionEvent<I> extends GwtEvent<ActionHandler<I>>{
	
	private static Type<ActionHandler<?>> TYPE;
	private I action;
	private Object data;
	private boolean failed;
	
    public static <I> ActionEvent<I> fire(HasActionHandlers<I> source, I action, Object data) {
	    if (TYPE != null) {
	      ActionEvent<I> event = new ActionEvent<I>(action,data);
	      source.fireEvent(event);
	      return event;
	    }
	    return null;
    }

    protected ActionEvent(I state,Object data) {
    	this.action = state;
    	this.data = data;
    }
    
	@Override
	protected void dispatch(ActionHandler<I> handler) {
		handler.onAction(this);
		
	}

	@Override
	public final Type<ActionHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<ActionHandler<?>> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<ActionHandler<?>>();
	    }
	    return TYPE;
	 }
	
	public I getAction() {
		return action;
	} 
	
	public Object getData() {
		return data;
	}
	
	public void fail() {
		failed = true;
	}
	
	public boolean failed() {
		return failed;
	}
}
