package org.openelis.gwt.services;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.data.DataModel;
import org.openelis.gwt.common.data.DataSet;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppScreenFormServiceIntAsync extends AppScreenServiceIntAsync {

    public void commitUpdate(FormRPC rpcSend, FormRPC rpcReturn, AsyncCallback callback);
    
    public void commitAdd(FormRPC rpcSend, FormRPC rpcReturn, AsyncCallback callback);
    
    public void commitQuery(FormRPC rpcSend, DataModel model, AsyncCallback callback);
    
    public void fetch(DataSet key, FormRPC rpcReturn, AsyncCallback callback);
    
    public void fetchForUpdate(DataSet key, FormRPC rpcReturn, AsyncCallback callback);
    
    public void commitDelete(DataSet key, FormRPC rpcReturn, AsyncCallback callback);
    
    public void abort(DataSet key, FormRPC rpcReturn, AsyncCallback callback);
    
    public void getInitialModel(String cat, AsyncCallback callback);
    
 
}
