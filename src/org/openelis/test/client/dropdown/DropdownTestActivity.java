package org.openelis.test.client.dropdown;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class DropdownTestActivity extends AbstractActivity {

	protected static DropdownTestView view;
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if(view == null) 
			view = new DropdownTestViewImpl();
		
		panel.setWidget(view);
		
	}

}
