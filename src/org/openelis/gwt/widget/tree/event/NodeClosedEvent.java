package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.Node;

import com.google.gwt.event.shared.GwtEvent;

public class NodeClosedEvent extends GwtEvent<NodeClosedHandler> {
	
	private static Type<NodeClosedHandler> TYPE;
	private int row;
	private Node node;
	
	public static void fire(HasNodeClosedHandlers source, int row, Node node) {
		if(TYPE != null) {
			NodeClosedEvent event = new NodeClosedEvent(row, node);
			source.fireEvent(event);
		}
	}
	
	protected NodeClosedEvent(int row, Node node) {
		this.row = row;
		this.node = node;
	}

	@Override
	protected void dispatch(NodeClosedHandler handler) {
		handler.onNodeClosed(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public com.google.gwt.event.shared.GwtEvent.Type<NodeClosedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<NodeClosedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<NodeClosedHandler>();
		}
		return TYPE;
	}
	
	public int getRow() {
		return row;
	}
		
	public Node getNode() {
		return node;
	}
 	

}
