package org.openelis.test.client.textbox;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class TextBoxTestActivity extends AbstractActivity{
	
	protected static TextBoxTestView view;
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if(view == null)
			view = new TextBoxTestViewImpl();
		
		panel.setWidget(view);
	}
}
