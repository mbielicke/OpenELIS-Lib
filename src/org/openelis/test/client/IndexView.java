package org.openelis.test.client;

import com.google.gwt.user.client.ui.IsWidget;

public interface IndexView extends IsWidget {

	public void setPresenter(Presenter presenter);

	public interface Presenter {
		public void goToTextBoxTest();
		public void goToDropdownTest();
		public void goToTableTest();
	}
}
