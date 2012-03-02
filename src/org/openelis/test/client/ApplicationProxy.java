package org.openelis.test.client;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.openelis.gwt.services.ScreenService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.ConsoleLogHandler;
import com.google.gwt.logging.client.FirebugLogHandler;
import com.google.gwt.logging.client.HasWidgetsLogHandler;
import com.google.gwt.logging.client.HtmlLogFormatter;
import com.google.gwt.logging.client.SimpleRemoteLogHandler;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ApplicationProxy {
    protected static final String APPLICATION_SERVICE_URL = "org.openelis.test.server.ApplicationService";
    protected ScreenService       service;
    protected HasWidgets 		  logPanel;
    
    public ApplicationProxy() {
    	if(GWT.isClient())
    		service = new ScreenService("controller?service=" + APPLICATION_SERVICE_URL);
    }
    	
	public Logger logger() {
		Logger logger;
		HasWidgetsLogHandler handler;
		HtmlLogFormatter formatter;
		
		if(GWT.isClient()) {
			logPanel = new VerticalPanel();
			logger = Logger.getLogger("testwidgets");
			logger.addHandler(new FirebugLogHandler());
			logger.addHandler(new ConsoleLogHandler());
			handler = new HasWidgetsLogHandler(logPanel);
			formatter = new HtmlLogFormatter(true) {
				@Override
				protected String getHtmlPrefix(LogRecord event) {
				    StringBuilder prefix = new StringBuilder();
				    prefix.append("<span>");
				    prefix.append("<code>");
				    return prefix.toString();
				}
			};
			handler.setFormatter(formatter);
			logger.addHandler(handler);
			
		}else 
			logger = Logger.getLogger("testwidgets");


		return logger;
	}
	
	public Logger remoteLogger() {
		Logger remoteLogger;
		
		remoteLogger = Logger.getLogger("testwidgets.server");
		remoteLogger.addHandler(new SimpleRemoteLogHandler());
		
		return remoteLogger;
	}
	
	public HasWidgets getLogPanel() {
		return logPanel;
	}

}
