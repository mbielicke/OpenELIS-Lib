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
    


}
