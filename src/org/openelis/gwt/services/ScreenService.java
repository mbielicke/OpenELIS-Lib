/**
 * Exhibit A - UIRF Open-source Based Public Software License.
 * 
 * The contents of this file are subject to the UIRF Open-source Based Public Software License(the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at openelis.uhl.uiowa.edu
 * 
 * Software distributed under the License is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for the specific language governing rights and limitations under the License.
 * 
 * The Original Code is OpenELIS code.
 * 
 * The Initial Developer of the Original Code is The University of Iowa. Portions created by The University of Iowa are Copyright 2006-2008. All Rights Reserved.
 * 
 * Contributor(s): ______________________________________.
 * 
 * Alternatively, the contents of this file marked "Separately-Licensed" may be used under the terms of a UIRF Software license ("UIRF Software License"), in which case the provisions of a UIRF Software License are applicable instead of those above.
 */
package org.openelis.gwt.services;

import java.util.ArrayList;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.RPC;
import org.openelis.gwt.screen.ScreenSessionTimer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.SyncCallback;

/**
 * This class implements and provides clients witth the standard Sync and Async service calls
 */
public class ScreenService implements ScreenServiceInt, ScreenServiceIntAsync {
    
	/**
	 * GWT.created service to make calls to the server
	 */
	private ScreenServiceIntAsync service;

	private static ScreenSessionTimer timer;
	
	/**
	 * Constructor that takes an already created service to make calls
	 * @param service
	 */
    public ScreenService(ScreenServiceIntAsync service) {
        this.service = service;
    }

    /**
     * Constructor that takes a URL to a service and wii create a service object from it 
     * to make calls
     * @param url
     */
    public ScreenService(String url) {
        service = (ScreenServiceIntAsync)GWT.create(ScreenServiceInt.class);
        ServiceDefTarget target = (ServiceDefTarget)service;
        target.setServiceEntryPoint(GWT.getModuleBaseURL()+url);
        
    }

    /**
     * Returns the service to make calls
     * @return
     */
    public ScreenServiceIntAsync getAsyncService() {
        return service;
    }

    /**
     * Synchronous call to the method passed expecting an Integer parameter
     */
    public <T extends RPC> T call(String method, Integer param) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
    }

    /**
     * Synchronous call to the method passed expecting a parameter that implements RPC
     */
    public <T extends RPC> T call(String method, RPC param) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
    }
    
    /**
     * Synchronous call to the method passed expecting a String parameter
     */
    public <T extends RPC> T call(String method, String param) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
    }

    /**
     * Synchronous call to the method passed expecting a Double parameter
     */
	public <T extends RPC> T call(String method, Double param) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
	}

	/**
	 * Synchronous call to the method passed expecting a Datetime parameter
	 */
	public <T extends RPC> T call(String method, Datetime param) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
	}

	/**
	 * Synchronous call to the method passed that returns a Boolean value
	 */
	public Boolean callBoolean(String method) throws Exception {
        Callback<Boolean> callback;

        resetSessionScreenTimer();
        callback = new Callback<Boolean>();
        service.callBoolean(method, callback);
        return callback.getResult();
	}

	/**
	 * Synchronous call to the method passed that returns a Datetime value
	 */
	public Datetime callDatetime(String method, byte begin, byte end) throws Exception {
        Callback<Datetime> callback;

        resetSessionScreenTimer();
        callback = new Callback<Datetime>();
        service.callDatetime(method, begin, end, callback);
        return callback.getResult();
	}

	/**
	 * Synchronous call to the method passed that returns a Double value
	 */
	public Double callDouble(String method) throws Exception {
        Callback<Double> callback;

        resetSessionScreenTimer();
        callback = new Callback<Double>();
        service.callDouble(method, callback);
        return callback.getResult();
	}
	
	public Long callLong(String method, RPC rpc) throws Exception {
		Callback<Long> callback = new Callback<Long>();
		service.callLong(method,rpc,callback);
		return callback.getResult();
	}

	/**
	 * Synchronous call to the method passed that returns an Integer value
	 */
	public Integer callInteger(String method) throws Exception {
        Callback<Integer> callback;

        resetSessionScreenTimer();
        callback = new Callback<Integer>();
        service.callInteger(method, callback);
        return callback.getResult();
	}

	/**
	 * Synchronous call to the method passed that returns a String value
	 */
	public String callString(String method) throws Exception {
        Callback<String> callback;

        resetSessionScreenTimer();
        callback = new Callback<String>();
        service.callString(method, callback);
        return callback.getResult();
	}
	
	/**
	 * Synchronous call to the method passed that expects a String parameter and returns a String value
	 */
	public String callString(String method, String param) throws Exception {
        Callback<String> callback;

        resetSessionScreenTimer();
        callback = new Callback<String>();
        service.callString(method, param, callback);
        return callback.getResult();
	}
	
	/**
	 * Synchronous call the method called that expects no parameter
	 */
	public <T extends RPC> T call(String method) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
        service.call(method, callback);
        return callback.getResult();
	}

	/**
	 * Synchronous call to the method passed that expects no parameter and returns void
	 */
	public void callVoid(String method) throws Exception {
        Callback<RPC> callback;

        resetSessionScreenTimer();
        callback = new Callback<RPC>();
        service.callVoid(method, callback);
        callback.getResult();
	}
	
	/**
	 * Synchronous call to the method passed that expects a Long parameter
	 */
	public <T extends RPC> T call(String method, Long param) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
	}
	
	/**
	 * Synchronous call the method passed that returns a List of RPC
	 */
	public <T extends ArrayList<? extends RPC>> T callList(String method)	throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
	    service.callList(method, callback);
	    return callback.getResult();
	}
	
	/**
	 * Synchronous call to the method passed the expects a parameter that implements RPC and returns a List of RPC
	 */
	public <T extends ArrayList<? extends RPC>> T  callList(String method, RPC param) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
	    service.callList(method, param, callback);
	    return callback.getResult();
	}
	
	/**
	 * Synchronous call to the method passed that expects a String parameter and returns a List of RPC
	 */
	public <T extends ArrayList<? extends RPC>> T callList(String method, String param) throws Exception {
	       Callback<T> callback = new Callback<T>();
	       service.callList(method, param, callback);
	       return callback.getResult();
	}

    /**
     * Synchronous call to the method passes that expects an Integer parameter and returns a List of RPC
     */
    public <T extends ArrayList<? extends RPC>> T  callList(String method, Integer param) throws Exception {
        Callback<T> callback;

        resetSessionScreenTimer();
        callback = new Callback<T>();
        service.callList(method, param, callback);
        return callback.getResult();
    }
	
    /**
     * Asynchronous call to the method passed expecting an Integer parameter
     */
    public void call(String method, Integer param, AsyncCallback<? extends RPC> callback) {
    	resetSessionScreenTimer();
    	service.call(method, param, callback);
    }

    /**
     * Asynchronous call to the method passed expecting a parameter the implements RPC
     */
    public void call(String method, RPC param, AsyncCallback<? extends RPC> callback) {
    	resetSessionScreenTimer();
    	service.call(method, param, callback);
    }

    /**
     * Asynchronous call to the method passed expecting a String parameter
     */
    public void call(String method, String param, AsyncCallback<? extends RPC> callback) {
    	resetSessionScreenTimer();
    	service.call(method, param, callback);
    }

	/**
	 * Asynchronous call to the method passed that expects a Double parameter
	 */
	public void call(String method, Double param, AsyncCallback<? extends RPC> callback) {
		resetSessionScreenTimer();
		service.call(method,param,callback);
	}

	/**
	 * Asynchronous call to the method passed that expects a Datetime parameter
	 */
	public void call(String method, Datetime param, AsyncCallback<? extends RPC> callback) {
		resetSessionScreenTimer();
		service.call(method, param, callback);
	}

	/**
	 * Asynchronous call to the method passed that returns a Boolean value
	 */
	public void callBoolean(String method, AsyncCallback<Boolean> callback) {
		resetSessionScreenTimer();
		service.callBoolean(method, callback);
	}

	/**
	 * Asynchronous call to the method passed that returns a Datetime value
	 */
	public void callDatetime(String method, byte begin, byte end, AsyncCallback<Datetime> callback) {
		resetSessionScreenTimer();
		service.callDatetime(method, begin, end, callback);
	}

	/**
	 * Asynchronous call to the method passed that returns a Double value
	 */
	public void callDouble(String method, AsyncCallback<Double> callback) {
		resetSessionScreenTimer();
		service.callDouble(method, callback);
	}
	
	public void callLong(String method, RPC rpc, AsyncCallback<Long> callback) {
		service.callLong(method,rpc,callback);
	}

	/**
	 * Asynchronous call to the method passed that returns an Integer value
	 */
	public void callInteger(String method, AsyncCallback<Integer> callback) {
		resetSessionScreenTimer();
		service.callInteger(method, callback);
	}

	/**
	 * Asynchronous call to the method passed that returns a String value
	 */
	public void callString(String method, AsyncCallback<String> callback) {
		resetSessionScreenTimer();
		service.callString(method, callback);
	}
	
	/**
	 * Asynchronous call to the method passed that expects a String parameter and returns a String value
	 */
	public void callString(String method, String param, AsyncCallback<String> callback) {
		resetSessionScreenTimer();
		service.callString(method, param, callback);
	}

	/**
	 * Asynchronous call to the method passed that expects no parameter
	 */
	public void call(String method, AsyncCallback<? extends RPC> callback) {
		resetSessionScreenTimer();
		service.call(method,callback);
	}

	/**
	 * Asynchronous call to the method passed that expects no parameter and returns void
	 */
	public void callVoid(String method, AsyncCallback<? extends RPC> callback) {
		resetSessionScreenTimer();
		service.callVoid(method,callback);
	}

	/**
	 * Asynchronous call to the method passed that expects a Long parameter 
	 */
	public void call(String method, Long param,AsyncCallback<? extends RPC> callback) {
		resetSessionScreenTimer();
		service.call(method,param, callback);
	}

	/**
	 * Asynchronous call to the method passed that expects a String parameter and returns a List of RPC
	 */
	public <T extends ArrayList<? extends RPC>> void callList(String method, String param, AsyncCallback<? extends ArrayList<? extends RPC>> callback) {
		resetSessionScreenTimer();
		service.callList(method, param, callback);
	}
	
	/**
	 * Asynchronous call to the method passed that expects a parameter that implements RPC and returns a List of RPC
	 */
	public <T extends ArrayList<? extends RPC>> void callList(String method, RPC param,AsyncCallback<? extends ArrayList<? extends RPC>> callback) {
		resetSessionScreenTimer();
		service.callList(method,param, callback);
	}

	/**
	 * Asynchronous call to the method passed that returns a List of RPC
	 */
	public <T extends ArrayList<? extends RPC>> void callList(String method,AsyncCallback<? extends ArrayList<? extends RPC>> callback) {
		resetSessionScreenTimer();
		service.callList(method, callback);
	}

    /**
     * Asynchronous call to the method passed that expects an Integer parameter and returns a List of RPC 
     */
    public <T extends ArrayList<? extends RPC>> void callList(String method, Integer param, AsyncCallback<? extends ArrayList<? extends RPC>> callback) {
    	resetSessionScreenTimer();
    	service.callList(method,param, callback);
    }
    
    /**
     * This class handles the callback of synchronous calls and will return the result
     * or throw an exception back to the caller
     *
     * @param <T>
     */
    private class Callback<T> implements SyncCallback<T> {
        T         result;
        Throwable caught;

        public void onFailure(Throwable caught) {
            this.caught = caught;
        }

        public void onSuccess(T result) {
            this.result = result;
        }

        public T getResult() throws Exception {
            if (caught != null)
                throw (Exception)caught;
            return result;
        }
    }

    
    //
    // Session screen timer implementation
    //
    public static void setScreenSessionTimer(ScreenSessionTimer timer) {
        ScreenService.timer = timer;
    }

    private void resetSessionScreenTimer() {
        if (timer != null)
            timer.resetTimeout();
    }

}
