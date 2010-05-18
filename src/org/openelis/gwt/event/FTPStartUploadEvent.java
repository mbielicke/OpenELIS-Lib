package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

public class FTPStartUploadEvent extends GwtEvent<FTPStartUploadHandler> {
	
	private static Type<FTPStartUploadHandler> TYPE;
	private String URL;
	
	public static FTPStartUploadEvent fire(HasFTPStartUploadHandlers source, String URL) {
		 if (TYPE != null) {
		      FTPStartUploadEvent event = new FTPStartUploadEvent(URL);
		      source.fireEvent(event);
		      return event;
		    }
		    return null;
	}
	
	public FTPStartUploadEvent(String URL) {
		this.URL = URL;
	}
	
	@Override
	protected void dispatch(FTPStartUploadHandler handler) {
		handler.onFTPStart(this);
		
	}

	@Override
	public final Type<FTPStartUploadHandler> getAssociatedType() {
		return (Type) TYPE;
	}

	public static Type<FTPStartUploadHandler> getType() {
	   if (TYPE == null) {
	      TYPE = new Type<FTPStartUploadHandler>();
	    }
	    return TYPE;
	 }
	
	public String getURL() {
		return URL;
	}

}
