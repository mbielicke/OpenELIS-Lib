package org.openelis.gwt.widget;

import org.openelis.gwt.services.ScreenService;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;

public class ReportFrame extends Composite {
	
	ScreenService service;
	Frame         frame;
	ProgressBar   bar;
	
	public ReportFrame(String id) {
		service = new ScreenService("controller?service=org.openelis.gwt.server.ReportService");
		
		final AbsolutePanel ap1 = new AbsolutePanel();
		ap1.setWidth("100%");
		ap1.setHeight("100%");
		bar = new ProgressBar();
		bar.setVisible(false);
		
		initWidget(ap1);

		frame = new Frame("report?service=org.openelis.gwt.server.ReportService&method=returnPDF&id="+id);
		frame.setHeight("1px");
		frame.setWidth("1px");
		DOM.setStyleAttribute(frame.getElement(), "border", "0");
		
		ap1.add(frame,0,0);
		
		Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			
			@Override
			public void execute() {
				ap1.add(bar,(ap1.getOffsetWidth()-200)/2,(ap1.getOffsetHeight()-40)/2);
			}
		});
				
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				frame.setWidth((Window.getClientWidth())+"px");
				frame.setHeight((Window.getClientHeight())+"px");
			}
		});
	}

}
