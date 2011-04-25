package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.Node;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeNodeDeletedEvent extends GwtEvent<BeforeNodeDeletedHandler> {
	
	private static Type<BeforeNodeDeletedHandler> TYPE;
	private int index;
	private Node node;
	private boolean cancelled;
	
	public static BeforeNodeDeletedEvent fire(HasBeforeNodeDeletedHandlers source, int index, Node node) {
		if(TYPE != null) {
			BeforeNodeDeletedEvent event = new BeforeNodeDeletedEvent(index, node);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeNodeDeletedEvent(int index, Node node) {
		this.node = node;
		this.index = index;
	}

	@Override
	protected void dispatch(BeforeNodeDeletedHandler handler) {
		handler.onBeforeNodeDeleted(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeNodeDeletedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeNodeDeletedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeNodeDeletedHandler>();
		}
		return TYPE;
	}
	
	public Node getNode() {
		return node;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	

}
