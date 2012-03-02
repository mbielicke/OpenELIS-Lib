package org.openelis.test.client;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.HasWidgets;

public class Application {
	
	private static final long serialVersionUID = 1L;
	
	
	protected static ApplicationProxy proxy;
	
	protected static Logger logger;
	protected static Logger remoteLogger;
		
	public static Logger logger() {
		if(logger == null) 
			logger =  proxy().logger();
		
		return logger;
	}
	
	public static Logger remoteLogger() {
		/*
		 * Do this to make sure client logging is initialized 
		 */
		logger();
		
		if(remoteLogger == null)
			remoteLogger = proxy().remoteLogger();
		
		return remoteLogger;
	}
	
	public static HasWidgets getLogPanel() {
		return proxy().getLogPanel();
	}
	
	private static ApplicationProxy proxy() {
		if(proxy == null) 
			proxy = new ApplicationProxy();
		
		return proxy;
	}
	
	
	
	

	
	

}
