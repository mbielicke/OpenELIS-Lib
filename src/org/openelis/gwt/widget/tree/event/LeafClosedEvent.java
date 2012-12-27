package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.TreeDataItem;

import com.google.gwt.event.shared.GwtEvent;

public class LeafClosedEvent extends GwtEvent<LeafClosedHandler> {
	
	private static Type<LeafClosedHandler> TYPE;
	private int row;
	private TreeDataItem item;
	
	public static void fire(HasLeafClosedHandlers source, int row, TreeDataItem item) {
		if(TYPE != null) {
			LeafClosedEvent event = new LeafClosedEvent(row, item);
			source.fireEvent(event);
		}
	}
	
	protected LeafClosedEvent(int row, TreeDataItem item) {
		this.row = row;
		this.item = item;
	}

	@Override
	protected void dispatch(LeafClosedHandler handler) {
		handler.onLeafClosed(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LeafClosedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<LeafClosedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<LeafClosedHandler>();
		}
		return TYPE;
	}
	
	public int getRow() {
		return row;
	}
		
	public TreeDataItem getItem() {
		return item;
	}
 	

}
