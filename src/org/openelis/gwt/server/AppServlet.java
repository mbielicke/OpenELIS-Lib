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
package org.openelis.gwt.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.openelis.util.SessionManager;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;

/**
 * This class extends RemoteServiceServlet from GWT and adds
 * some functionality for Session management and application logging 
 *
 */
public class AppServlet extends RemoteServiceServlet {
    
    private static final long serialVersionUID = 1L;
    protected static SerializationPolicy sPolicy;

    /**
     * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#doGetSerializationPolicy(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     * 
     *  This method was overridden so that data could be returned to a CASified Hosted Browser.
     */
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
        // TODO Auto-generated method stub
    	if(sPolicy == null) {
    		sPolicy = super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
    		return sPolicy;
    	}else
    		return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
    }

    /**
     * This method was overridden to ensure that the SessionManager is updated with the current thread and Session.
     * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#onBeforeRequestDeserialized(java.lang.String)
     */
    protected void onBeforeRequestDeserialized(String serializedRequest) {
        // TODO Auto-generated method stub
        super.onBeforeRequestDeserialized(serializedRequest);
        HttpSession session = getThreadLocalRequest().getSession();
        session.setAttribute("IPAddress",
                             getThreadLocalRequest().getRemoteAddr());
        SessionManager.setSession(session);
        try {
           // ServiceUtils.getPermissions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void onAfterResponseSerialized(String serializedResponse) {
        // TODO Auto-generated method stub
        super.onAfterResponseSerialized(serializedResponse);
        getThreadLocalResponse().setHeader("pragma", "no-cache");
        getThreadLocalResponse().setHeader("Cache-Control","no-cache");
        getThreadLocalResponse().setHeader("Cache-Control","no-store");
        getThreadLocalResponse().setDateHeader("Expires", 0 );
    }
    
}
