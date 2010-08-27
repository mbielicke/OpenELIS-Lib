package org.openelis.gwt.widget.redesign.tree.event;

import org.openelis.gwt.widget.redesign.table.Row;
import org.openelis.gwt.widget.redesign.tree.Item;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeItemAddedEvent extends GwtEvent<BeforeItemAddedHandler> {
	
	private static Type<BeforeItemAddedHandler> TYPE;
	private int index;
	private Item item;
	private boolean cancelled;
	
	public static BeforeItemAddedEvent fire(HasBeforeItemAddedHandlers source, int index, Item item) {
		if(TYPE != null) {
			BeforeItemAddedEvent event = new BeforeItemAddedEvent(index, item);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeItemAddedEvent(int index, Item item) {
		this.item = item;
		this.index = index;
	}

	@Override
	protected void dispatch(BeforeItemAddedHandler handler) {
		handler.onBeforeItemAdded(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeItemAddedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeItemAddedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeItemAddedHandler>();
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
