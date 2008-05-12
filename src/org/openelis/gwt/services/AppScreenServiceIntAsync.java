package org.openelis.gwt.services;


import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppScreenServiceIntAsync {
    
    public void getXML(AsyncCallback callback);
    
    public void getXMLData(AsyncCallback callback);
    
    public void getXMLData(HashMap args, AsyncCallback callback);

}
