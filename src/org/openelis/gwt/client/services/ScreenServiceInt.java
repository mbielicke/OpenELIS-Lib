package org.openelis.gwt.client.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.FormRPC;
import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.common.data.AbstractField;

/**
 * ScreenServiceInt is a GWT RemoteService interface for the Screen
 * Widget.  GWT RemoteServiceServlets that want to provide server
 * side logic for Screens must implement this interface.
 * 
 * @author tschmidt
 *
 */
public interface ScreenServiceInt extends RemoteService {

    public FormRPC action(FormRPC rpc) throws RPCException;
    
    public AbstractField query(FormRPC rpc) throws RPCException;
    
}
