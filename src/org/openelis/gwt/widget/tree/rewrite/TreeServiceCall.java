package org.openelis.gwt.widget.tree.rewrite;


public class TreeServiceCall implements TreeServiceCallInt {
    
    //private TreeServiceIntAsync treeService = (TreeServiceIntAsync) GWT
    //.create(TreeServiceInt.class);

    //private ServiceDefTarget target = (ServiceDefTarget) treeService;
    
    public TreeServiceCall(String url) {
       // String base = GWT.getModuleBaseURL();
       // base += url;
       // target.setServiceEntryPoint(base);
    }

    public void getChildNodes(final TreeWidget model, final int row) {
        /*try {
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
        */
    }

}
