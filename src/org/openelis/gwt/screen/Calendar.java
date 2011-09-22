package org.openelis.gwt.screen;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.services.ScreenService;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class Calendar {
	
	private static ScreenService service = new ScreenService("controller?service=org.openelis.gwt.server.CalendarService");

	public static Datetime getCurrentDatetime(int begin, int end) throws Exception {
		return service.callDatetime("getCurrentDatetime", begin, end);
	}
	
	public static void getCurrentDatetime(int begin, int end, AsyncCallback<Datetime> callback) {
		service.callDatetime("getCurrentDatetime",begin,end,callback);
	}
}
