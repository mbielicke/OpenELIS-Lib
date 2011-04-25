package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.Node;

import com.google.gwt.event.shared.GwtEvent;

public class NodeDeletedEvent extends GwtEvent<NodeDeletedHandler> {
	
	private static Type<NodeDeletedHandler> TYPE;
	private int index;
	private Node node;
	
	public static void fire(HasNodeDeletedHandlers source, int index, Node node) {
		if(TYPE != null) {
			NodeDeletedEvent event = new NodeDeletedEvent(index, node);
			source.fireEvent(event);
		}
	}
	
	protected NodeDeletedEvent(int index, Node node) {
		this.node = node;
		this.index = index;
	}

	@Override
	protected void dispatch(NodeDeletedHandler handler) {
		handler.onNodeDeleted(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NodeDeletedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<NodeDeletedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<NodeDeletedHandler>();
		}
		return TYPE;
	}
	
	public Node getNode() {
		return node;
	}
	
	public int getIndex() {
		return index;
	}
}
