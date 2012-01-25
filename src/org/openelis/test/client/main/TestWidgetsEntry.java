package org.openelis.test.client.main;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestWidgetsEntry implements EntryPoint, NativePreviewHandler {
	  	 
	  
	  /**
	   * This is the entry point method.
	   */
	  public void onModuleLoad() {
		  
		  
		  GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable e) {
				Window.alert("Sorry, but an unexpected error has occurred.  Please contact IT support");
				e.printStackTrace();
			}
		  });
		  
		  //All Events will flow this this handler first before any other handlers.
		  Event.addNativePreviewHandler(this);
		  Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			  public void execute() {
				  GWT.runAsync(new RunAsyncCallback() {
					  public void onSuccess() {
						  try {
							  Window.enableScrolling(true);
							  RootPanel.get().add(new org.openelis.test.client.main.TestWidgetsScreen());
						  }catch(Throwable e){
							  Window.alert("Unable to start app : "+e.getMessage());
							  e.printStackTrace();
						  }
					  }

					  public void onFailure(Throwable caught) {
						  Window.alert(caught.getMessage());
					  }
				  });
			  }
		  });
	  }

	  /**
	   * All events created by the application will flow through here.  The event can be inspected for type and other user input
	   * then certain actions can be taken such as preventing default browser before or even cancelling events
	   */
	  public void onPreviewNativeEvent(NativePreviewEvent event) {
		  //This check is to prevent FireFox from highlighting HTML Elements when mouseDown is combined with the ctrl key
		  if(event.getTypeInt() == Event.ONMOUSEDOWN && event.getNativeEvent().getCtrlKey())
			  event.getNativeEvent().preventDefault();
		  if(event.getTypeInt() == Event.ONMOUSEWHEEL && event.getNativeEvent().getShiftKey())
			  event.getNativeEvent().preventDefault();
		
	  }

}