package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.TreeDataItem;

import com.google.gwt.event.shared.GwtEvent;

public class LeafOpenedEvent extends GwtEvent<LeafOpenedHandler> {
	
	private static Type<LeafOpenedHandler> TYPE;
	private int row;
	private TreeDataItem item;
	
	public static void fire(HasLeafOpenedHandlers source, int row, TreeDataItem item) {
		if(TYPE != null) {
			LeafOpenedEvent event = new LeafOpenedEvent(row, item);
			source.fireEvent(event);
		}
	}
	
	protected LeafOpenedEvent(int row, TreeDataItem item) {
		this.row = row;
		this.item = item;
	}

	@Override
	protected void dispatch(LeafOpenedHandler handler) {
		handler.onLeafOpened(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<LeafOpenedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<LeafOpenedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<LeafOpenedHandler>();
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
