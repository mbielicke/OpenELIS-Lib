package org.openelis.gwt.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.google.gwt.logging.server.RemoteLoggingServiceImpl;

public class LoggingServlet extends RemoteLoggingServiceImpl {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		setSymbolMapsDirectory(getServletContext().getRealPath("")+"/"+config.getInitParameter("symbolMaps"));
	}

}
