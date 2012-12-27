package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class BeforeGetMatchesEvent extends GwtEvent<BeforeGetMatchesHandler> {
	
	private static Type<BeforeGetMatchesHandler> TYPE;
	private String match;
	private boolean cancelled;
	
	public static BeforeGetMatchesEvent fire(HasBeforeGetMatchesHandlers source, String match) {
		if(TYPE != null) {
			BeforeGetMatchesEvent event = new BeforeGetMatchesEvent(match);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected BeforeGetMatchesEvent(String match) {
		this.match = match;
	}

	@Override
	protected void dispatch(BeforeGetMatchesHandler handler) {
		handler.onBeforeGetMatches(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<BeforeGetMatchesHandler> getAssociatedType() {
		return (Type) TYPE;
	}
	
	public static Type<BeforeGetMatchesHandler> getType() {
		if(TYPE == null) {
			TYPE = new Type<BeforeGetMatchesHandler>();
		}
		return TYPE;
	}
	
	public String getMatch() {
		return match;
	}
	
	public void cancel() {
		cancelled = true;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	

}
