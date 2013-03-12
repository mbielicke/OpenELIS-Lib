package org.openelis.gwt.services;

import org.openelis.gwt.common.CalendarRPC;
import org.openelis.ui.common.Datetime;
import org.openelis.gwt.screen.Callback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CalendarService implements CalendarServiceInt, CalendarServiceIntAsync {
    
    static CalendarService instance;
    
    CalendarServiceIntAsync service;
    
    public static CalendarService get() {
        if(instance == null)
            instance = new CalendarService();
        
        return instance;
    }
    
    private CalendarService() {
        service = (CalendarServiceIntAsync)GWT.create(CalendarServiceInt.class);
    }

    @Override
    public void getCurrentDatetime(byte begin, byte end, AsyncCallback<Datetime> callback) {
        service.getCurrentDatetime(begin, end, callback);
    }

    @Override
    public void getMonth(CalendarRPC form, AsyncCallback<CalendarRPC> callback) {
        service.getMonth(form, callback);
    }

    @Override
    public void getMonthSelect(CalendarRPC form, AsyncCallback<CalendarRPC> callback) {
        service.getMonthSelect(form, callback);
    }

    @Override
    public void getScreen(CalendarRPC rpc, AsyncCallback<CalendarRPC> callback) {
        service.getScreen(rpc, callback);
    }

    @Override
    public CalendarRPC getMonth(CalendarRPC form) throws Exception {
        Callback<CalendarRPC> callback;
        
        callback = new Callback<CalendarRPC>();
        service.getMonth(form, callback);
        return callback.getResult();

    }

    @Override
    public CalendarRPC getMonthSelect(CalendarRPC form) throws Exception {
        Callback<CalendarRPC> callback;
        
        callback = new Callback<CalendarRPC>();
        service.getMonthSelect(form, callback);
        return callback.getResult();

    }

    @Override
    public CalendarRPC getScreen(CalendarRPC rpc) throws Exception {
        Callback<CalendarRPC> callback;
        
        callback = new Callback<CalendarRPC>();
        service.getScreen(rpc, callback);
        return callback.getResult();

    }

    @Override
    public Datetime getCurrentDatetime(byte begin, byte end) {
        Callback<Datetime> callback;
        
        callback = new Callback<Datetime>();
        service.getCurrentDatetime(begin, end, callback);
        try {
            return callback.getResult();
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
