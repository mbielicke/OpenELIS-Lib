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

import java.util.Date;

import org.openelis.gwt.common.RPC;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.impl.HTTPRequestImpl;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.SyncCallback;

public class ScreenService implements ScreenServiceInt, ScreenServiceIntAsync {
    private ScreenServiceIntAsync service;

    public ScreenService(ScreenServiceIntAsync service) {
        this.service = service;
    }

    public ScreenService(String url) {
        service = (ScreenServiceIntAsync)GWT.create(ScreenServiceInt.class);
        ServiceDefTarget target = (ServiceDefTarget)service;
        target.setServiceEntryPoint(GWT.getModuleBaseURL()+url);
        
    }

    public ScreenServiceIntAsync getAsyncService() {
        return service;
    }

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

    public <T extends RPC> T call(String method, Integer param) throws Exception {
        Callback<T> callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
    }

    public <T extends RPC> T call(String method, RPC param) throws Exception {
        Callback<T> callback = new Callback<T>();
        Request req = service.call(method, param, callback);
        
        return callback.getResult();
    }
    
    public <T extends RPC> T call(String method, String param) throws Exception {
        Callback<T> callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
    }

    public Request call(String method, Integer param, AsyncCallback<? extends RPC> callback) {
        return service.call(method, param, callback);
    }

    public Request call(String method, RPC param, AsyncCallback<? extends RPC> callback) {
        return service.call(method, param, callback);
    }

    public Request call(String method, String param, AsyncCallback<? extends RPC> callback) {
        return service.call(method, param, callback);
    }

	public <T extends RPC> T call(String method, Double param) throws Exception {
        Callback<T> callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
	}

	public <T extends RPC> T call(String method, Date param) throws Exception {
        Callback<T> callback = new Callback<T>();
        service.call(method, param, callback);
        return callback.getResult();
	}

	public Boolean callBoolean(String method) throws Exception {
        Callback<Boolean> callback = new Callback<Boolean>();
        service.callBoolean(method, callback);
        return callback.getResult();
	}

	public Date callDate(String method) throws Exception {
        Callback<Date> callback = new Callback<Date>();
        service.callDate(method, callback);
        return callback.getResult();
	}

	public Double callDouble(String method) throws Exception {
        Callback<Double> callback = new Callback<Double>();
        service.callDouble(method, callback);
        return callback.getResult();
	}

	public Integer callInteger(String method) throws Exception {
        Callback<Integer> callback = new Callback<Integer>();
        service.callInteger(method, callback);
        return callback.getResult();
	}

	public String callString(String method) throws Exception {
        Callback<String> callback = new Callback<String>();
        service.callString(method, callback);
        return callback.getResult();
	}

	public Request call(String method, Double param,
			AsyncCallback<? extends RPC> callback) {
		return service.call(method,param,callback);
	}

	public Request call(String method, Date param,
			AsyncCallback<? extends RPC> callback) {
		return service.call(method, param, callback);
	}

	public Request callBoolean(String method, AsyncCallback<Boolean> callback) {
		return service.callBoolean(method, callback);
	}

	public Request callDate(String method, AsyncCallback<Date> callback) {
		return service.callDate(method, callback);
	}

	public Request callDouble(String method, AsyncCallback<Double> callback) {
		return service.callDouble(method, callback);
	}

	public Request callInteger(String method, AsyncCallback<Integer> callback) {
		return service.callInteger(method, callback);
	}

	public Request callString(String method, AsyncCallback<String> callback) {
		return service.callString(method, callback);
	}

	public <T extends RPC> T call(String method) throws Exception {
        Callback<RPC> callback = new Callback<RPC>();
        service.call(method, callback);
        return (T)callback.getResult();
	}

	public void callVoid(String method) throws Exception {
        Callback<RPC> callback = new Callback<RPC>();
        service.callVoid(method, callback);
        callback.getResult();
	}

	public Request call(String method, AsyncCallback<? extends RPC> callback) {
		return service.call(method,callback);
	}

	public Request callVoid(String method, AsyncCallback<? extends RPC> callback) {
		return service.callVoid(method,callback);
	}

	public <T extends RPC> T call(String method, Long param) throws Exception {
        Callback<RPC> callback = new Callback<RPC>();
        service.call(method, param, callback);
        return (T)callback.getResult();
        
	}

	public Request call(String method, Long param,
			AsyncCallback<? extends RPC> callback) {
		return service.call(method,param, callback);
	}
}
