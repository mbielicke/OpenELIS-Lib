package org.openelis.gwt.screen;

import com.google.gwt.user.client.rpc.SyncCallback;

public class Callback<T> implements SyncCallback<T> {
    
	T         result;
    Throwable caught;
    
    public Callback() {
    	
    }
    
    public void onFailure(Throwable caught) {
        this.caught = caught;
    }

    public void onSuccess(T result) {
        this.result = result;
    }

    public T getResult() throws Exception {
        if (caught != null)
            throw (Exception)caught;
        return result;
    }

}
