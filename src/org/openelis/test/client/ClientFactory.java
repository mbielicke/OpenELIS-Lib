package org.openelis.test.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.event.shared.EventBus;

public interface ClientFactory {
	
	public EventBus eventBus();
	
	public PlaceController placeController();
}
