package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.RPCException;

import java.util.HashMap;

public interface AppScreenServiceInt extends RemoteService {
    
    public String getXML() throws RPCException;
    
    /**
     *  @gwt.typeArgs <java.lang.String, org.openelis.gwt.common.data.DataObject>
     */
    public HashMap getXMLData() throws RPCException;
    
    /**
     *  @gwt.typeArgs <java.lang.String, org.openelis.gwt.common.data.DataObject>
     */
    public HashMap getXMLData(HashMap args) throws RPCException;

}
