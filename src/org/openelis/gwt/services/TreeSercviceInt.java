package org.openelis.gwt.services;

import com.google.gwt.user.client.rpc.RemoteService;

import org.openelis.gwt.common.data.TreeDataItem;

import java.util.ArrayList;

@Deprecated public interface TreeSercviceInt extends RemoteService {

    public ArrayList<TreeDataItem> getChildNodes(TreeDataItem item);
}
