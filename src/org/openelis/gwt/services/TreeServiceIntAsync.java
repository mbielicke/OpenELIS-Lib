package org.openelis.gwt.services;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.openelis.gwt.common.data.TreeDataItem;

import java.util.ArrayList;
@Deprecated
public interface TreeServiceIntAsync {
    
    public Request getChildNodes(TreeDataItem item, AsyncCallback<ArrayList<TreeDataItem>> callback);

}
