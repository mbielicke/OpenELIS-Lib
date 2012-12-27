package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class DropEvent<I> extends GwtEvent<DropHandler<I>> {
	
	private static Type<DropHandler<?>> TYPE;
	private I dragObject;
	private Object dropTarget;
	private boolean cancelled;
	
	public static <I> DropEvent<I> fire(HasDropHandlers<I> source, I dragObject, Object dropTarget) {
		if(TYPE != null) {
			DropEvent<I> event = new DropEvent<I>(dragObject,dropTarget);
			//source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	
	public DropEvent(I dragObject,Object dropTarget) {
		this.dragObject = dragObject;
		this.dropTarget = dropTarget;
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
	
	public Object getDropTarget() {
		return dropTarget;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	
	

}
