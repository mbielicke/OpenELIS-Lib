package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.AbstractField;
import org.openelis.gwt.common.FormRPC;

public interface AppScreenFormServiceIntAsync extends AppScreenServiceIntAsync {

    public void commitUpdate(FormRPC rpc, AsyncCallback callback);
    
    public void commitAdd(FormRPC rpc, AsyncCallback callback);
    
    public void commitQuery(FormRPC rpc, AsyncCallback callback);
    
    public void fetch(FormRPC rpc, AbstractField key, AsyncCallback callback);
    
    public void fetchForUpdate(FormRPC rpc, AbstractField key, AsyncCallback callback);
    
    public void delete(AbstractField key, AsyncCallback callback);
    
    public void abort(AbstractField key, AsyncCallback callback);
}
