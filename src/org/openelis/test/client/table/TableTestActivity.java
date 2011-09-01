package org.openelis.test.client.table;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class TableTestActivity extends AbstractActivity{
	
	protected static TableTestView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if(view == null)
			view = new TableTestViewImpl();
		
		panel.setWidget(view);
		
	}

}
