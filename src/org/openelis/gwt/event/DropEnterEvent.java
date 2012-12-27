package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class DropEnterEvent<I> extends GwtEvent<DropEnterHandler<I>> {
	
	private static Type<DropEnterHandler<?>> TYPE;
	private I dragObject;
	private Object dropTarget;
	private boolean cancelled;
	public enum DropPosition {BELOW, ON, ABOVE};
	public DropPosition dropPos;
	
	public static <I> DropEnterEvent<I> fire(HasDropEnterHandlers<I> source, I dragObject, Object dropTarget, DropPosition pos) {
		if(TYPE != null) {
			DropEnterEvent<I> event = new DropEnterEvent<I>(dragObject, dropTarget, pos);
			//source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	public DropEnterEvent(I dragObject, Object dropTarget, DropPosition pos) {
		this.dragObject = dragObject;
		this.dropTarget = dropTarget;
		this.dropPos = pos;
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

	public DropPosition getDropPosition() {
		return dropPos;
	}
	
}
