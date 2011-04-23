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

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * ScreenServiceInt is a GWT RemoteService interface for the Screen
 * Widget.  GWT RemoteServiceServlets that want to provide server
 * side logic for Screens must implement this interface.
 * 
 * @author tschmidt
 *
 */
public interface ScreenServiceInt extends RemoteService {
    public String            callString(String method) throws Exception;
    
    public String            callString(String method, String param) throws Exception;
    
    public Integer           callInteger(String method) throws Exception;
    
    public Boolean           callBoolean(String method) throws Exception;
    
    public Datetime          callDatetime(String method, byte begin, byte end) throws Exception;
    
    public Double            callDouble(String method) throws Exception;
    
    public Long              callLong(String method,RPC param) throws Exception;
    
    public <T extends RPC>  T call(String method) throws Exception;
    
    public void callVoid(String method) throws Exception;
    
    public <T extends RPC> T call(String method, Integer param) throws Exception;
    
    public <T extends RPC> T call(String method, RPC param) throws Exception;
    
    public <T extends RPC> T call(String method, Double param) throws Exception;
    
    public <T extends RPC> T call(String method, String param) throws Exception;
    
    public <T extends RPC> T call(String method, Datetime param) throws Exception;
    
    public <T extends RPC> T call(String method, Long param) throws Exception;
    
    public <T extends ArrayList<? extends RPC>> T  callList(String method, RPC param) throws Exception; 
    
    public <T extends ArrayList<? extends RPC>> T  callList(String method) throws Exception; 
    
    public <T extends ArrayList<? extends RPC>> T  callList(String method, String param) throws Exception;
    
    public <T extends ArrayList<? extends RPC>> T  callList(String method, Integer param) throws Exception; 
}
