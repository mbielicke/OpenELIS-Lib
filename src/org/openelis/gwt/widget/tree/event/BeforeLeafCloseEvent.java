package org.openelis.gwt.widget.tree.event;

import org.openelis.gwt.widget.tree.TreeDataItem;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeLeafCloseEvent extends GwtEvent<BeforeLeafCloseHandler> {
	
	private static Type<BeforeLeafCloseHandler> TYPE;
	private int row;
	private TreeDataItem item;
	private boolean cancelled;
	
	public static BeforeLeafCloseEvent fire(HasBeforeLeafCloseHandlers source, int row, TreeDataItem item) {
		if(TYPE != null) {
			BeforeLeafCloseEvent event = new BeforeLeafCloseEvent(row,item);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeLeafCloseEvent(int row, TreeDataItem item) {
		this.row = row;
		this.item = item;
	}

	@Override
	protected void dispatch(BeforeLeafCloseHandler handler) {
		handler.onBeforeLeafClose(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeLeafCloseHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeLeafCloseHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeLeafCloseHandler>();
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
