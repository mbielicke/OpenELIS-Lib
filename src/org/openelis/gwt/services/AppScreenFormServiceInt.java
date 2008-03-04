package org.openelis.gwt.services;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;

public interface AppScreenFormServiceInt extends AppScreenServiceInt {
    
    public FormRPC commitUpdate(FormRPC rpcSend, FormRPC rpcReturn) throws RPCException;
    
    public FormRPC commitAdd(FormRPC rpcSend, FormRPC rpcReturn) throws RPCException;
    
    public DataModel commitQuery(FormRPC rpcSend, DataModel model) throws RPCException;
    
    public FormRPC fetch(DataSet key, FormRPC rpcReturn) throws RPCException;
    
    public FormRPC fetchForUpdate(DataSet key, FormRPC rpcReturn) throws RPCException;
    
    public FormRPC commitDelete(DataSet key, FormRPC rpcReturn) throws RPCException;
    
    public FormRPC abort(DataSet key, FormRPC rpcReturn) throws RPCException;
}
