package org.openelis.gwt.server;

import org.openelis.ui.common.Datetime;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RemoteServlet extends RemoteServiceServlet {

    private static final long serialVersionUID = 1L;
    
    @Override
    protected void onBeforeRequestDeserialized(String serializedRequest) {
        super.onBeforeRequestDeserialized(serializedRequest);
        
        getThreadLocalRequest().getSession().setAttribute("last_access", Datetime.getInstance(Datetime.YEAR,Datetime.MINUTE));

    }
    
    protected void onAfterResponseSerialized(String serializedResponse) {
        super.onAfterResponseSerialized(serializedResponse);
        getThreadLocalResponse().setHeader("pragma", "no-cache");
        getThreadLocalResponse().setHeader("Cache-Control","no-cache");
        getThreadLocalResponse().setHeader("Cache-Control","no-store");
        getThreadLocalResponse().setDateHeader("Expires", 0 );
    }
    

}
