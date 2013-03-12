package org.openelis.gwt.services;

import org.openelis.gwt.common.CalendarRPC;
import org.openelis.ui.common.Datetime;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("calendar")
public interface CalendarServiceInt extends RemoteService {

    CalendarRPC getMonth(CalendarRPC form) throws Exception;

    CalendarRPC getMonthSelect(CalendarRPC form) throws Exception;

    CalendarRPC getScreen(CalendarRPC rpc) throws Exception;

    Datetime getCurrentDatetime(byte begin, byte end);

}