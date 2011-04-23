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

import org.openelis.gwt.common.Datetime;
import org.openelis.gwt.common.RPC;

import com.google.gwt.user.client.rpc.AsyncCallback;
/**
 * ScreenServiceIntAsync is the Asynchronous version of
 * the ScreenServiceInt interface.
 * @author tschmidt
 *
 */
public interface ScreenServiceIntAsync {
    public void callString(String method, AsyncCallback<String> callback);
    
    public void callString(String method, String param, AsyncCallback<String> callback);
    
    public void callInteger(String method, AsyncCallback<Integer> callback);
    
    public void callBoolean(String method, AsyncCallback<Boolean> callback);
    
    public void callDatetime(String method, byte begin, byte end, AsyncCallback<Datetime> callback);
    
    public void callDouble(String method, AsyncCallback<Double> callback);
    
    public void callLong(String method, RPC param, AsyncCallback<Long> callback);
    
    public void call(String method, AsyncCallback<? extends RPC> callback);
    
    public void callVoid(String method, AsyncCallback<? extends RPC> callback);
    
    public void call(String method, Integer param, AsyncCallback<? extends RPC> callback);
    
    public void call(String method, RPC param, AsyncCallback<? extends RPC> callback);
    
    public void call(String method, Double param, AsyncCallback<? extends RPC> callback);
    
    public void call(String method, String param, AsyncCallback<? extends RPC> callback);
    
    public void call(String method, Datetime param, AsyncCallback<? extends RPC> callback);
    
    public void call(String method, Long param, AsyncCallback<? extends RPC> callback);
    
    public <T extends ArrayList<? extends RPC>> void callList(String method, RPC param, AsyncCallback<? extends ArrayList<? extends RPC>> callback);
    
    public <T extends ArrayList<? extends RPC>> void callList(String method, AsyncCallback<? extends ArrayList<? extends RPC>> callback);
    
    public <T extends ArrayList<? extends RPC>> void callList(String method, String param, AsyncCallback<? extends ArrayList<? extends RPC>> callback);
    
    public <T extends ArrayList<? extends RPC>> void callList(String method, Integer param, AsyncCallback<? extends ArrayList<? extends RPC>> callback);
}
