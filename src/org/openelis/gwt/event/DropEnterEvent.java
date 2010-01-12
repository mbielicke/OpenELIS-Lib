package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class DropEnterEvent<I> extends GwtEvent<DropEnterHandler<I>> {
	
	private static Type<DropEnterHandler<?>> TYPE;
	private I dragObject;
	private Object dropTarget;
	private boolean cancelled;
	
	public static <I> DropEnterEvent<I> fire(HasDropEnterHandlers<I> source, I dragObject, Object dropTarget) {
		if(TYPE != null) {
			DropEnterEvent<I> event = new DropEnterEvent<I>(dragObject, dropTarget);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected DropEnterEvent(I dragObject, Object dropTarget) {
		this.dragObject = dragObject;
		this.dropTarget = dropTarget;
	}

	@Override
	protected void dispatch(DropEnterHandler<I> handler) {
		handler.onDropEnter(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<DropEnterHandler<I>> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<DropEnterHandler<?>> getType() {
		if (TYPE == null) {
			TYPE = new Type<DropEnterHandler<?>>();
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
