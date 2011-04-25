package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.Node;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeNodeCloseEvent extends GwtEvent<BeforeNodeCloseHandler> {
	
	private static Type<BeforeNodeCloseHandler> TYPE;
	private int row;
	private Node node;
	private boolean cancelled;
	
	public static BeforeNodeCloseEvent fire(HasBeforeNodeCloseHandlers source, int row, Node node) {
		if(TYPE != null) {
			BeforeNodeCloseEvent event = new BeforeNodeCloseEvent(row,node);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeNodeCloseEvent(int row, Node node) {
		this.row = row;
		this.node = node;
	}

	@Override
	protected void dispatch(BeforeNodeCloseHandler handler) {
		handler.onBeforeNodeClose(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeNodeCloseHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeNodeCloseHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeNodeCloseHandler>();
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
