package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.Node;

import com.google.gwt.event.shared.GwtEvent;

public class NodeAddedEvent extends GwtEvent<NodeAddedHandler> {
	
	private static Type<NodeAddedHandler> TYPE;
	private int index;
	private Node node;
	private Node parent;
	
	public static void fire(HasNodeAddedHandlers source, int index, Node parent, Node node) {
		if(TYPE != null) {
			NodeAddedEvent event = new NodeAddedEvent(index, parent, node);
			source.fireEvent(event);
		}
	}
	
	protected NodeAddedEvent(int index, Node parent, Node node) {
		this.node = node;
		this.parent = parent;
		this.index = index;
	}

	@Override
	protected void dispatch(NodeAddedHandler handler) {
		handler.onNodeAdded(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NodeAddedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<NodeAddedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<NodeAddedHandler>();
		}
		return TYPE;
	}
	
	public Node getNode() {
		return node;
	}
	
	public Node getParent() {
	    return parent;
	}
	
	public int getIndex() {
		return index;
	}
	

}
