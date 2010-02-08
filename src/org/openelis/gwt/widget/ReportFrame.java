package org.openelis.gwt.widget;

import org.openelis.gwt.common.FileProgress;
import org.openelis.gwt.services.ScreenService;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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

		DeferredCommand.addCommand(new Command() {
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
		
		getFileProgress(id);
	}
	
	public void getFileProgress(final String id) {
		service.call("fileProgress", id, new AsyncCallback<FileProgress>() {
			public void onSuccess(FileProgress progress) {
				if(!bar.isVisible())
					bar.setVisible(true);
				bar.setMessage("Downloading : "+getSize(progress.progress)+" of "+getSize(progress.size));
				bar.setProgress(((int)(progress.progress/(double)progress.size*100.0)));
				if(!progress.done){
					Timer timer = new Timer() {
						public void run() {
							getFileProgress(id);
						}
					};
					timer.schedule(500);
				}else{
					bar.setProgress(100);
					bar.removeFromParent();
					DeferredCommand.addCommand(new Command() {
						public void execute() {
							frame.setWidth((Window.getClientWidth())+"px");
							frame.setHeight((Window.getClientHeight())+"px");
						}
					});
					
					
					
				}
			}	
			
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
		});
	}
	
	private String getSize(long size) {
		NumberFormat format = NumberFormat.getFormat("######.0");
		double bytes = size;
		if(bytes < 10240) {
			return format.format(bytes)+"B";
		}else if(bytes < 1024000){
			return format.format(bytes/10240.0)+"KB";
		}else {
			return format.format(bytes/1024000.0)+"MB";
		}
	}

}
