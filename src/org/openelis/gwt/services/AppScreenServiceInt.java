package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataObject;

public interface AppScreenServiceInt extends RemoteService {
    
    public String getXML() throws RPCException;
    
    public DataObject[] getXMLData() throws RPCException;
    
    public DataObject[] getXMLData(DataObject[] args) throws RPCException;

}
