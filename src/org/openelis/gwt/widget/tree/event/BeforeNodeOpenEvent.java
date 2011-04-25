package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.Node;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeNodeOpenEvent extends GwtEvent<BeforeNodeOpenHandler> {
	
	private static Type<BeforeNodeOpenHandler> TYPE;
	private int row;
	private Node node;
	private boolean cancelled;
	
	public static BeforeNodeOpenEvent fire(HasBeforeNodeOpenHandlers source, int row, Node node) {
		if(TYPE != null) {
			BeforeNodeOpenEvent event = new BeforeNodeOpenEvent(row,node);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeNodeOpenEvent(int row, Node node) {
		this.row = row;
		this.node = node;
	}

	@Override
	protected void dispatch(BeforeNodeOpenHandler handler) {
		handler.onBeforeNodeOpen(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeNodeOpenHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeNodeOpenHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeNodeOpenHandler>();
		}
		return TYPE;
	}
	
	public int getRow() {
		return row;
	}
	
	public Node getNode() {
		return node;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
 	

}
