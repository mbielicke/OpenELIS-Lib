package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class DropEvent<I> extends GwtEvent<DropHandler<I>> {
	
	private static Type<DropHandler<?>> TYPE;
	private I dragObject;
	
	public static <I> DropEvent<I> fire(HasDropHandlers<I> source, I dragObject) {
	    if(TYPE != null) {
			DropEvent<I> event = new DropEvent<I>(dragObject);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	
	public DropEvent(I dragObject) {
		this.dragObject = dragObject;
	}
	
	protected void dispatch(DropHandler<I> handler) {
		handler.onDrop(this);
	}
	
	public final Type<DropHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<DropHandler<?>> getType() {
	   if (TYPE == null) {
		   TYPE = new Type<DropHandler<?>>();
	   }
	   return TYPE;
	}
	
	public I getDragObject() {
		return dragObject;
	}	
}
