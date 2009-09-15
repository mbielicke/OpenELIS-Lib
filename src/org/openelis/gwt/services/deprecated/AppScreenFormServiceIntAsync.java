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
package org.openelis.gwt.services.deprecated;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.RPC;

@Deprecated
public interface AppScreenFormServiceIntAsync<ScreenRPC extends RPC,QueryRPC extends RPC> extends AppScreenServiceIntAsync<ScreenRPC> {

    public Request commitUpdate(ScreenRPC rpc, AsyncCallback<ScreenRPC> callback);
    
    public Request commitAdd(ScreenRPC rpc, AsyncCallback<ScreenRPC> callback);
    
    public Request commitQuery(QueryRPC d, AsyncCallback<QueryRPC> callback);
    
    public Request fetch(ScreenRPC rpc, AsyncCallback<ScreenRPC> callback);
    
    public Request fetchForUpdate(ScreenRPC rpc, AsyncCallback<ScreenRPC> callback);
    
    public Request commitDelete(ScreenRPC rpc, AsyncCallback<ScreenRPC> callback);
    
    public Request abort(ScreenRPC rpc, AsyncCallback<ScreenRPC> callback);
    
}