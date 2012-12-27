package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

public class NavigationSelectionEvent extends GwtEvent<NavigationSelectionHandler> {
	
	private static Type<NavigationSelectionHandler> TYPE;
	private Integer index;
	private boolean cancel;
	
	public static NavigationSelectionEvent fire(HasNavigationSelectionHandlers source, Integer index) {
		if(TYPE != null){
			NavigationSelectionEvent event = new NavigationSelectionEvent(index);
			source.fireEvent(event);
			return event;
		}
		return null;
	}
	
	protected NavigationSelectionEvent(Integer index) {
		this.index = index;
	}


	@Override
	protected void dispatch(NavigationSelectionHandler handler) {
		handler.onNavigationSelection(this);
	}

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<NavigationSelectionHandler> getAssociatedType() {
		return (Type)TYPE;
	}
	
	public static Type<NavigationSelectionHandler> getType() {
		   if (TYPE == null) {
		      TYPE = new Type<NavigationSelectionHandler>();
		    }
		    return TYPE;
	}

    public Integer getIndex() {
    	return index;
    }
    
    public void cancel() {
    	cancel = true;
    }
    
    public boolean isCancelled() {
    	return cancel;
    }

}
