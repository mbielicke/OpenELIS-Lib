package org.openelis.test.client;

import org.openelis.test.client.dropdown.DropdownTestPlace;
import org.openelis.test.client.table.TableTestPlace;
import org.openelis.test.client.textbox.TextBoxTestPlace;
import org.openelis.test.client.tree.TreeTestPlace;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class IndexActivity extends AbstractActivity implements IndexView.Presenter {

	protected static IndexView view;
	ClientFactory clientFactory;
	
	public IndexActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		if(view == null) {
			view = new IndexViewImpl();
			view.setPresenter(this);
		}
		
		panel.setWidget(view);
	}

	@Override
	public void goToTextBoxTest() {
		clientFactory.placeController().goTo(new TextBoxTestPlace(""));
	}

	@Override
	public void goToDropdownTest() {
		clientFactory.placeController().goTo(new DropdownTestPlace(""));
	}

	@Override
	public void goToTableTest() {
		clientFactory.placeController().goTo(new TableTestPlace(""));
	}

	@Override
	public void goToTreeTest() {
		clientFactory.placeController().goTo(new TreeTestPlace(""));
	}
	
}
