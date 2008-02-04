package org.openelis.gwt.widget.pagedtree;

import org.openelis.gwt.common.RPCException;


import com.google.gwt.user.client.rpc.RemoteService;

public interface TreeServiceInt extends RemoteService {
    
    public TreeModel getTreePage(int page, int selected) throws RPCException;
    public TreeModel getTreeModel(TreeModel model) throws RPCException;
    public TreeModel saveTreeModel(TreeModel model) throws RPCException;

}
