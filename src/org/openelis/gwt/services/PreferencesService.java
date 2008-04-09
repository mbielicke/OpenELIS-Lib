package org.openelis.gwt.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class PreferencesService {
    
    protected static PreferencesServiceIntAsync prefService = (PreferencesServiceIntAsync) GWT
    .create(PreferencesServiceInt.class);
    protected static ServiceDefTarget target = (ServiceDefTarget) prefService;
    
    static {
        String base = GWT.getModuleBaseURL();
        base += "PreferencesServlet";        
        target.setServiceEntryPoint(base);
    }
    
    public static PreferencesServiceIntAsync getService() {
        return prefService;
    }

}
