package org.openelis.test.client.tree;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class TreeTestActivity extends AbstractActivity {
	
	protected static TreeTestView view;

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if(view == null)
			view = new TreeTestViewImpl();
		
		panel.setWidget(view);
	}

}
