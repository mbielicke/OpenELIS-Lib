package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.TreeDataItem;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeLeafOpenEvent extends GwtEvent<BeforeLeafOpenHandler> {
	
	private static Type<BeforeLeafOpenHandler> TYPE;
	private int row;
	private TreeDataItem item;
	private boolean cancelled;
	
	public static BeforeLeafOpenEvent fire(HasBeforeLeafOpenHandlers source, int row, TreeDataItem item) {
		if(TYPE != null) {
			BeforeLeafOpenEvent event = new BeforeLeafOpenEvent(row,item);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeLeafOpenEvent(int row, TreeDataItem item) {
		this.row = row;
		this.item = item;
	}

	@Override
	protected void dispatch(BeforeLeafOpenHandler handler) {
		handler.onBeforeLeafOpen(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeLeafOpenHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeLeafOpenHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeLeafOpenHandler>();
		}
		return TYPE;
	}
	
	public int getRow() {
		return row;
	}
	
	public TreeDataItem getItem() {
		return item;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
 	

}
