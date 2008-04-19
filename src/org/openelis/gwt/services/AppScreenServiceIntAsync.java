package org.openelis.gwt.services;

import org.openelis.gwt.common.data.DataObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppScreenServiceIntAsync {
    
    public void getXML(AsyncCallback callback);
    
    public void getXMLData(AsyncCallback callback);
    
    public void getXMLData(DataObject[] args, AsyncCallback callback);

}
