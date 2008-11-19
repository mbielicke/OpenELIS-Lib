package org.openelis.gwt.widget.tree;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import org.openelis.gwt.common.data.TreeDataItem;
import org.openelis.gwt.services.TreeServiceIntAsync;
import org.openelis.gwt.widget.pagedtree.TreeServiceInt;

import java.util.ArrayList;

public class TreeServiceCall implements TreeServiceCallInt {
    
    private TreeServiceIntAsync treeService = (TreeServiceIntAsync) GWT
    .create(TreeServiceInt.class);

    private ServiceDefTarget target = (ServiceDefTarget) treeService;
    
    public TreeServiceCall(String url) {
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }

    public void getChildNodes(final TreeModel model, final int row) {
        try {
            treeService.getChildNodes(model.getRow(row), new AsyncCallback<ArrayList<TreeDataItem>>() {
               public void onSuccess(ArrayList<TreeDataItem> children) {
                   TreeDataItem item = model.getRow(row);
                   for(TreeDataItem child : children) {
                       item.addItem(child);
                   }
                   item.loaded = true;
                   model.toggle(row);
               }
               public void onFailure(Throwable caught) {
                   Window.alert("Tree Service Call :"+caught.toString());
               }
            });
        }catch(Exception e){
            Window.alert("Tree Service Call : "+e.toString());
        }
    }

}
