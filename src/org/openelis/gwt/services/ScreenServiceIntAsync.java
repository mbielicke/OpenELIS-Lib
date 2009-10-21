/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.services;

import java.util.ArrayList;
import java.util.Date;

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.RPC;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
/**
 * ScreenServiceIntAsync is the Asynchronous version of
 * the ScreenServiceInt interface.
 * @author tschmidt
 *
 */
public interface ScreenServiceIntAsync {

    public Request callString(String method, AsyncCallback<String> callback);
    public Request callInteger(String method, AsyncCallback<Integer> callback);
    public Request callBoolean(String method, AsyncCallback<Boolean> callback);
    public Request callDatetime(String method, byte begin, byte end, AsyncCallback<Datetime> callback);
    public Request callDouble(String method, AsyncCallback<Double> callback);
    public Request call(String method, AsyncCallback<? extends RPC> callback);
    public Request callVoid(String method, AsyncCallback<? extends RPC> callback);
    public Request call(String method, Integer param, AsyncCallback<? extends RPC> callback);
    public Request call(String method, RPC param, AsyncCallback<? extends RPC> callback);
    public Request call(String method, Double param, AsyncCallback<? extends RPC> callback);
    public Request call(String method, String param, AsyncCallback<? extends RPC> callback);
    public Request call(String method, Datetime param, AsyncCallback<? extends RPC> callback);
    public Request call(String method, Long param, AsyncCallback<? extends RPC> callback);
    public <T extends RPC> Request callList(String method, RPC param, AsyncCallback<ArrayList<T>> callback);
    public <T extends RPC> Request callList(String method, AsyncCallback<ArrayList<T>> callback);
    public <T extends RPC> Request callList(String method, String param, AsyncCallback<ArrayList<T>> callback);

}
