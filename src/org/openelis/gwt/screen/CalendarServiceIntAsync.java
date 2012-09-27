package org.openelis.gwt.screen;

import org.openelis.gwt.common.Datetime;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CalendarServiceIntAsync {
	
	public void getCurrentDatetime(byte begin, byte end, AsyncCallback<Datetime> callback);

}
