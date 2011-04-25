package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.Node;

import com.google.gwt.event.shared.GwtEvent;

public class NodeOpenedEvent extends GwtEvent<NodeOpenedHandler> {
	
	private static Type<NodeOpenedHandler> TYPE;
	private int row;
	private Node node;
	
	public static void fire(HasNodeOpenedHandlers source, int row, Node node) {
		if(TYPE != null) {
			NodeOpenedEvent event = new NodeOpenedEvent(row, node);
			source.fireEvent(event);
		}
	}
	
	protected NodeOpenedEvent(int row, Node node) {
		this.row = row;
		this.node = node;
	}

	@Override
	protected void dispatch(NodeOpenedHandler handler) {
		handler.onNodeOpened(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	public com.google.gwt.event.shared.GwtEvent.Type<NodeOpenedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<NodeOpenedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<NodeOpenedHandler>();
		}
		return TYPE;
	}
	
	public int getRow() {
		return row;
	}
		
	public  Node getNode() {
		return node;
	}
 	

}
