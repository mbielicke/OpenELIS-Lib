package org.openelis.gwt.client.services;

import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;

public interface AppScreenFormServiceInt extends AppScreenServiceInt {
    
    public FormRPC commitUpdate(FormRPC rpc) throws RPCException;
    
    public FormRPC commitAdd(FormRPC rpc) throws RPCException;
    
    public FormRPC commitQuery(FormRPC rpc) throws RPCException;
    
    public FormRPC fetch(AbstractField key) throws RPCException;
    
    public FormRPC fetchForUpdate(AbstractField key) throws RPCException;
    
    public FormRPC delete(AbstractField key) throws RPCException;
    
    public FormRPC abort(AbstractField key) throws RPCException;

}
