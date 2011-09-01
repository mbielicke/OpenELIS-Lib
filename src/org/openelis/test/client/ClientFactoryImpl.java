package org.openelis.test.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;

public class ClientFactoryImpl implements ClientFactory {
	
	EventBus eventBus;
	PlaceController placeController;
	
	public ClientFactoryImpl() {
		eventBus = new SimpleEventBus();
		placeController = new PlaceController(eventBus);
	}

	@Override
	public EventBus eventBus() {
		return eventBus;
	}

	@Override
	public PlaceController placeController() {
		return placeController;
	}

}
