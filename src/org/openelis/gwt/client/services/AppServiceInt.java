package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.RPCException;

public interface AppServiceInt extends RemoteService {
    
    public String getScreen(String name) throws RPCException;
}
