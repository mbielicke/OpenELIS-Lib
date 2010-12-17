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
package org.openelis.gwt.screen;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.services.ScreenService;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * This class provides date and calendar functionality for front end screens
 *
 */
public class Calendar {
	
	private static ScreenService service = new ScreenService("controller?service=org.openelis.gwt.server.CalendarService");

	/**
	 * This method makes a Synchronous call to the server to fetch the current Datetime at the precision passed in the params
	 * begin and end
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static Datetime getCurrentDatetime(byte begin, byte end) throws Exception {
		return service.callDatetime("getCurrentDatetime", begin, end);
	}
	
	/**
	 * This method makes an Asynchronous call to the server to fethc the current Datetime at the precision passed in the params
	 * begin and end
	 * @param begin
	 * @param end
	 * @param callback
	 */
	public static void getCurrentDatetime(byte begin, byte end, AsyncCallback<Datetime> callback) {
		service.callDatetime("getCurrentDatetime",begin,end,callback);
	}
}
