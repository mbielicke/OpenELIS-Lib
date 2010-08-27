package org.openelis.gwt.widget.redesign.tree.event;

import org.openelis.gwt.widget.redesign.tree.Item;

import com.google.gwt.event.shared.GwtEvent;

public class ItemDeletedEvent extends GwtEvent<ItemDeletedHandler> {
	
	private static Type<ItemDeletedHandler> TYPE;
	private int index;
	private Item item;
	
	public static void fire(HasItemDeletedHandlers source, int index, Item item) {
		if(TYPE != null) {
			ItemDeletedEvent event = new ItemDeletedEvent(index, item);
			source.fireEvent(event);
		}
	}
	
	protected ItemDeletedEvent(int index, Item item) {
		this.item = item;
		this.index = index;
	}

	@Override
	protected void dispatch(ItemDeletedHandler handler) {
		handler.onItemDeleted(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ItemDeletedHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<ItemDeletedHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<ItemDeletedHandler>();
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
