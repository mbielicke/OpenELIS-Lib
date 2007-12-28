package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.AbstractField;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;

public interface AppScreenFormServiceIntAsync extends AppScreenServiceIntAsync {

    public void commitUpdate(FormRPC rpcSend, FormRPC rpcReturn, AsyncCallback callback);
    
    public void commitAdd(FormRPC rpcSend, FormRPC rpcReturn, AsyncCallback callback);
    
    public void commitQuery(FormRPC rpcSend, DataModel model, AsyncCallback callback);
    
    public void fetch(DataSet key, FormRPC rpcReturn, AsyncCallback callback);
    
    public void fetchForUpdate(DataSet key, FormRPC rpcReturn, AsyncCallback callback);
    
    public void delete(DataSet key, FormRPC rpcReturn, AsyncCallback callback);
    
    public void abort(DataSet key, FormRPC rpcReturn, AsyncCallback callback);
}
