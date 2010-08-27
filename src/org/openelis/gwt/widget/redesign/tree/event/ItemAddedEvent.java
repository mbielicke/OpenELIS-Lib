package org.openelis.gwt.widget.redesign.tree.event;

import org.openelis.gwt.widget.redesign.table.Row;
import org.openelis.gwt.widget.redesign.tree.Item;

import com.google.gwt.event.shared.GwtEvent;

public class ItemAddedEvent extends GwtEvent<ItemAddedHandler> {
	
	private static Type<ItemAddedHandler> TYPE;
	private int index;
	private Item item;
	
	public static void fire(HasItemAddedHandlers source, int index, Item item) {
		if(TYPE != null) {
			ItemAddedEvent event = new ItemAddedEvent(index, item);
			source.fireEvent(event);
		}
	}
	
	protected ItemAddedEvent(int index, Item item) {
		this.item = item;
		this.index = index;
	}

	@Override
	protected void dispatch(ItemAddedHandler handler) {
		handler.onItemAdded(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ItemAddedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<ItemAddedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<ItemAddedHandler>();
		}
		return TYPE;
	}
	
	public Item getItem() {
		return item;
	}
	
	public int getIndex() {
		return index;
	}
	

}
