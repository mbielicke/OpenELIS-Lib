package org.openelis.gwt.client.services;

import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;

public interface AppScreenFormServiceInt extends AppScreenServiceInt {
    
    public FormRPC commitUpdate(FormRPC rpc) throws RPCException;
    
    public FormRPC commitAdd(FormRPC rpc) throws RPCException;
    
    public AbstractField commitQuery(FormRPC rpc) throws RPCException;
    
    public FormRPC fetch(FormRPC rpc, AbstractField key) throws RPCException;
    
    public FormRPC fetchForUpdate(FormRPC rpc, AbstractField key) throws RPCException;
    
    public FormRPC delete(FormRPC rpc, AbstractField key) throws RPCException;
    
    public FormRPC abort(FormRPC rpc, AbstractField key) throws RPCException;

}
