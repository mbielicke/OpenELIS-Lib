package org.openelis.gwt.services;

import org.openelis.gwt.common.CalendarRPC;
import org.openelis.ui.common.Datetime;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CalendarServiceIntAsync {

    void getCurrentDatetime(byte begin, byte end, AsyncCallback<Datetime> callback);

    void getMonth(CalendarRPC form, AsyncCallback<CalendarRPC> callback);

    void getMonthSelect(CalendarRPC form, AsyncCallback<CalendarRPC> callback);

    void getScreen(CalendarRPC rpc, AsyncCallback<CalendarRPC> callback);

}
