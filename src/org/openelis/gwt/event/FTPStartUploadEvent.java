/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * This class will fire events to objects registered for FTP Start upload actions
 * @author tschmidt
 *
 */
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
	
	/**
	 * Returns the URL for the file to be fetched
	 * @return
	 */
	public String getURL() {
		return URL;
	}

}
