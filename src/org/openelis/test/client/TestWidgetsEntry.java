package org.openelis.test.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class TestWidgetsEntry implements EntryPoint{

	@Override
	public void onModuleLoad() {
		ClientFactory clientFactory = new ClientFactoryImpl();
		SimplePanel appPanel = new SimplePanel();
		
		ActivityMapper activityMapper = new AppActivityMapper(clientFactory);
		ActivityManager activityManager = new ActivityManager(activityMapper, clientFactory.eventBus());
		activityManager.setDisplay(appPanel);
		
		AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
		PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
		historyHandler.register(clientFactory.placeController(), clientFactory.eventBus(), new IndexPlace("Index"));
		
		RootPanel.get().add(appPanel);
		
		historyHandler.handleCurrentHistory();
	
	}

}
