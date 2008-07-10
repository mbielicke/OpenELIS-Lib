/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import org.openelis.util.SessionManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AppServlet extends RemoteServiceServlet {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * @see com.google.gwt.user.server.rpc.RemoteServiceServlet#doGetSerializationPolicy(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String)
     * 
     *  This method was overridden so that data could be returned to a CASified Hosted Browser.
     */
    protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL, String strongName) {
        // TODO Auto-generated method stub
        if(moduleBaseURL.indexOf("/shell") > -1) {
            String temp = moduleBaseURL.substring(0, moduleBaseURL.indexOf("/shell"));
            moduleBaseURL = temp + moduleBaseURL.substring(moduleBaseURL.indexOf("/shell")+6);
        }
        return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
    }

    /*
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
            ServiceUtils.getPermissions();
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
