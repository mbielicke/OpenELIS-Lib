package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.openelis.gwt.common.FormRPC;
/**
 * ScreenServiceIntAsync is the Asynchronous version of
 * the ScreenServiceInt interface.
 * @author tschmidt
 *
 */
public interface ScreenServiceIntAsync {

    public void action(FormRPC rpc, AsyncCallback callback);
    
    public void query(FormRPC rpc, AsyncCallback callback);

}
