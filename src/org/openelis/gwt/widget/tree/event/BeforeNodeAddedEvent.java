package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.Node;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * @author tschmidt
 *
 */
public class BeforeNodeAddedEvent extends GwtEvent<BeforeNodeAddedHandler> {
	
	private static Type<BeforeNodeAddedHandler> TYPE;
	private int index;
	private Node node,parent;
	private boolean cancelled;
	
	public static BeforeNodeAddedEvent fire(HasBeforeNodeAddedHandlers source, int index, Node parent, Node node) {
		if(TYPE != null) {
			BeforeNodeAddedEvent event = new BeforeNodeAddedEvent(index, parent, node);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeNodeAddedEvent(int index, Node parent, Node node) {
		this.node = node;
		this.parent = parent;
		this.index = index;
	}

	@Override
	protected void dispatch(BeforeNodeAddedHandler handler) {
		handler.onBeforeNodeAdded(this);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeNodeAddedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeNodeAddedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeNodeAddedHandler>();
		}
		return TYPE;
	}
	
	public Node getRow() {
		return node;
	}
	
	public Node getParent() {
	    return parent;
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
