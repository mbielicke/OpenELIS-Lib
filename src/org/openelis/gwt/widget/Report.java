package org.openelis.gwt.widget;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class Report implements EntryPoint {

	public void onModuleLoad() {
		String id = Window.Location.getParameter("id");
		RootPanel.get().add(new ReportFrame(id));
	}
	

}
