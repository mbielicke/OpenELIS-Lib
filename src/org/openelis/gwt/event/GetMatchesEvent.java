package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class GetMatchesEvent extends GwtEvent<GetMatchesHandler>{
	
	private static Type<GetMatchesHandler> TYPE;
	private String match;
	private Object data;
	private boolean failed;
	
    public static GetMatchesEvent fire(HasGetMatchesHandlers source, String match) {
	    if (TYPE != null) {
	      GetMatchesEvent event = new GetMatchesEvent(match);
	      source.fireEvent(event);
	      return event;
	    }
	    return null;
    }

    protected GetMatchesEvent(String match) {
    	this.match = match;
    }
    
	@Override
	protected void dispatch(GetMatchesHandler handler) {
		handler.onGetMatches(this);
		
	}

	@Override
	public final Type<GetMatchesHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<GetMatchesHandler> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<GetMatchesHandler>();
	    }
	    return TYPE;
	 }
	
	public String getMatch() {
		return match;
	} 
		
	public void fail() {
		failed = true;
	}
	
	public boolean failed() {
		return failed;
	}
}
