package org.openelis.test.server;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;

public class RemoteLogging extends RemoteLoggingServiceImpl{

	public RemoteLogging() {
		setSymbolMapsDirectory("/usr/pub/http/Application/testwidgets/symbolMaps/");
	}
	
}
