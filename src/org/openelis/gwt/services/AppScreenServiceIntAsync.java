package org.openelis.gwt.services;


import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.data.DataObject;

public interface AppScreenServiceIntAsync {
    
    public void getXML(AsyncCallback callback);
    
    public void getXMLData(AsyncCallback callback);
    
    public void getXMLData(HashMap<String,DataObject> args, AsyncCallback callback);

}
