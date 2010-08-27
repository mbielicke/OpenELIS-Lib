package org.openelis.gwt.widget.redesign.tree.event;

import org.openelis.gwt.widget.redesign.table.Row;
import org.openelis.gwt.widget.redesign.tree.Item;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeItemDeletedEvent extends GwtEvent<BeforeItemDeletedHandler> {
	
	private static Type<BeforeItemDeletedHandler> TYPE;
	private int index;
	private Item item;
	private boolean cancelled;
	
	public static BeforeItemDeletedEvent fire(HasBeforeItemDeletedHandlers source, int index, Item item) {
		if(TYPE != null) {
			BeforeItemDeletedEvent event = new BeforeItemDeletedEvent(index, item);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeItemDeletedEvent(int index, Item item) {
		this.item = item;
		this.index = index;
	}

	@Override
	protected void dispatch(BeforeItemDeletedHandler handler) {
		handler.onBeforeItemDeleted(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeItemDeletedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeItemDeletedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeItemDeletedHandler>();
		}
		return TYPE;
	}
	
	public Item getItem() {
		return item;
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
