package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.RPCException;

public interface AppScreenServiceInt extends RemoteService {
    
    public String getXML() throws RPCException; 

}
