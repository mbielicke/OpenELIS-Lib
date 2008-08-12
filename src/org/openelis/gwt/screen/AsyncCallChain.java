package org.openelis.gwt.screen;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public class AsyncCallChain extends ArrayList<AsyncCallback> implements AsyncCallback{
    
    private static final long serialVersionUID = 1L;
    
    public AsyncCallChain() {
        
    }
    
    public AsyncCallChain(AsyncCallback[] callbacks){
        for(AsyncCallback callback: callbacks){
            add(callback);
        }
    }

    public void onSuccess(Object result) {
        for(AsyncCallback callback : this){
            callback.onSuccess(result);
        }
    }
    
    public void onFailure(Throwable caught) {
        for(AsyncCallback callback : this){
            callback.onFailure(caught);
        }
    }

}
