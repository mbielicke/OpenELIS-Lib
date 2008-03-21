package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CalendarServiceIntAsync extends AppScreenServiceIntAsync {
    
    public void getMonth(String month, String year, AsyncCallback callback);

    public void getMonthSelect(String month, String year, AsyncCallback callback);

}
