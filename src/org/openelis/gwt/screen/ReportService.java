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
package org.openelis.gwt.screen;

import java.util.ArrayList;

import org.openelis.gwt.common.Prompt;
import org.openelis.gwt.common.ReportStatus;
import org.openelis.gwt.common.data.Query;
import org.openelis.gwt.screen.ScreenSessionTimer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.SyncCallback;

/**
 * This class implements and provides clients witth the standard Sync and Async service calls
 */
public class ReportService implements ReportServiceInt, ReportServiceIntAsync {
    
	/**
	 * GWT.created service to make calls to the server
	 */
	private ReportServiceIntAsync service;

	private static ScreenSessionTimer timer;
		
    /**
     * Constructor that takes a URL to a service and will create a service object from it 
     * to make calls
     * @param url
     */
    public ReportService(String url) {
        service = (ReportServiceIntAsync)GWT.create(ReportServiceInt.class);
        ServiceDefTarget target = (ServiceDefTarget)service;
        target.setServiceEntryPoint(GWT.getModuleBaseURL()+url);
    }
    
	@Override
	public void getPrompts(AsyncCallback<ArrayList<Prompt>> callback) {
		resetSessionScreenTimer();
		service.getPrompts(callback);
	}

	@Override
	public void runReport(Query query, AsyncCallback<ReportStatus> callback) {
		resetSessionScreenTimer();
		service.runReport(query, callback);
	}

	@Override
	public ArrayList<Prompt> getPrompts() throws Exception {
        Callback<ArrayList<Prompt>> callback;

        resetSessionScreenTimer();
        callback = new Callback<ArrayList<Prompt>>();
	    service.getPrompts(callback);
	    return callback.getResult();
	}

	@Override
	public ReportStatus runReport(Query query) throws Exception {
        Callback<ReportStatus> callback;

        resetSessionScreenTimer();
        callback = new Callback<ReportStatus>();
	    service.runReport(query,callback);
	    return callback.getResult();
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
        ReportService.timer = timer;
    }

    private void resetSessionScreenTimer() {
        if (timer != null)
            timer.resetTimeout();
    }

}
