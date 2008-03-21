package org.openelis.gwt.services;

import org.openelis.gwt.common.RPCException;

public interface CalendarServiceInt extends AppScreenServiceInt {
    
    public String getMonth(String month, String year) throws RPCException;
    
    public String getMonthSelect(String month, String year) throws RPCException;

}
