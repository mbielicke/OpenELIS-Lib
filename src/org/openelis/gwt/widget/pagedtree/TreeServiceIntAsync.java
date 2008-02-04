package org.openelis.gwt.widget.pagedtree;

import org.openelis.gwt.common.RPCException;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TreeServiceIntAsync {
    
    public void getTreePage(int page, int selected, AsyncCallback callback) throws RPCException;
    public void getTreeModel(TreeModel model, AsyncCallback callback) throws RPCException;
    public void saveTreeModel(TreeModel model, AsyncCallback callback) throws RPCException;
}
