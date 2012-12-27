package org.openelis.gwt.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasFTPStartUploadHandlers extends HasHandlers {
	
	public HandlerRegistration addFTPStartUploadHandler(FTPStartUploadHandler handler);

}
