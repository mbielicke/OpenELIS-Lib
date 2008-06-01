package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataObject;

import java.util.HashMap;

public interface AppScreenServiceInt extends RemoteService {
    
    public String getXML() throws RPCException;
    
    public HashMap<String,DataObject> getXMLData() throws RPCException;
    
    public HashMap<String,DataObject> getXMLData(HashMap<String,DataObject> args) throws RPCException;

}
