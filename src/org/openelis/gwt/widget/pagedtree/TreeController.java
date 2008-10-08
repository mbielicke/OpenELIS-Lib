/** Exhibit A - UIRF Open-source Based Public Software License.
* 
* The contents of this file are subject to the UIRF Open-source Based
* Public Software License(the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
* openelis.uhl.uiowa.edu
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations
* under the License.
* 
* The Original Code is OpenELIS code.
* 
* The Initial Developer of the Original Code is The University of Iowa.
* Portions created by The University of Iowa are Copyright 2006-2008. All
* Rights Reserved.
* 
* Contributor(s): ______________________________________.
* 
* Alternatively, the contents of this file marked
* "Separately-Licensed" may be used under the terms of a UIRF Software
* license ("UIRF Software License"), in which case the provisions of a
* UIRF Software License are applicable instead of those above. 
*/
package org.openelis.gwt.widget.pagedtree;

import java.io.Serializable;

import org.openelis.gwt.common.RPCException;
import org.openelis.gwt.screen.ScreenLabel;
import org.openelis.gwt.widget.MenuLabel;



import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.Widget;

public class TreeController implements  ClickListener, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1338489405293614599L;

    public TreeModel model;
    public TreeView view;
    public int selected = -1;
    public int setItem = -1;    
    private int shown;
    private TreeCallback callback = new TreeCallback();
    private TreeServiceIntAsync treeService = (TreeServiceIntAsync)GWT.create(TreeServiceInt.class);
    private ServiceDefTarget target = (ServiceDefTarget)treeService;
    private TreeListener treeListener = null; 
    private ScrollPanel vScroll = new ScrollPanel();
    
    public TreeController() {
        view = new TreeView();
        view.tree.setStyleName("ScreenTree");
        model = new TreeModel();  
        initService("OpenELISService");
    }
        
    
    public void setModel(TreeModel model) {
        this.model = model;
        reset();
    }
    
    public void setView(TreeView view) {
        this.view = view;
        view.tree.setStyleName("ScreenTree");
        view.tree.addTreeListener(treeListener);
    }
    
    public void addItem() {
        model.addItem(null);
        int itemIndex = model.numItems() - 1;
        if (itemIndex > -1) {
            loadItem(itemIndex);
            if (model.paged)
                view.setShown("(" + model.shown
                              + " of "
                              + model.totalItems
                              + ")");
            else
                view.setShown("(" + shown + " of " + model.numItems() + ")");
            
        }
        
    }

    public void addItem(TreeModelItem item) {
        model.addItem(item);
        int itemIndex = model.numItems() - 1;
        if (itemIndex > -1) {
            loadItem(itemIndex);
            if (model.paged)
                view.setShown("(" + model.shown
                              + " of "
                              + model.totalItems
                              + ")");
            else
                view.setShown("(" + shown + " of " + model.numItems() + ")");            
        }       
    }
       
       
    public void onClick(Widget sender) {
        HTML nav = (HTML)sender;
        String htmlString = nav.getHTML();
        int start = htmlString.indexOf("value=\"") + 7;
        int end = htmlString.indexOf("\"", start);
        String page = htmlString.substring(start, end);
        if (page.equals("-1"))
            getPage(--model.pageIndex, -1);
        else if (page.equals("+1"))
            getPage(++model.pageIndex, -1);
        else
            getPage(Integer.parseInt(page), -1);

    }
    
    private class TreeCallback implements AsyncCallback {
        public void onSuccess(Object result) {
            if (result != null) {
                model = (TreeModel)result;
                reset();
                if(setItem > -1)
                    view.tree.setSelectedItem((view.tree.getItem(setItem)));
            }
        }

        
        public void onFailure(Throwable caught) {
            Window.alert("Failed Tree :" + caught.toString());
        }
    }
    
    
    public void initService(String url) {
        String base = GWT.getModuleBaseURL();
        base += url;
        target.setServiceEntryPoint(base);
    }

    private void loadItem(int index) {
        TreeItem loadedItem =null;
        //ScreenMenuLabel treeItemlabel  = new  ScreenMenuLabel();        
       // MenuLabel label = (MenuLabel)treeItemlabel.getWidget();
       
       
        
        
        if(model.getItem(index).getImage()!=null){
            MenuLabel label = new MenuLabel();        
            label.setText(model.getItem(index).getText());
            if(model.getItem(index).getStyleName()!=null){
             label.setStylePrimaryName(model.getItem(index).getStyleName());
            }else{ 
             label.setStylePrimaryName("ScreenLabel");
            }
            label.setImage(model.getItem(index).getImage());
            loadedItem = new TreeItem(label);
        } 
        else{
           ScreenLabel screenLabel = new ScreenLabel(model.getItem(index).getText(),model.getItem(index).getUserObject());
           if(model.getItem(index).getStyleName()!=null){
               screenLabel.setStylePrimaryName(model.getItem(index).getStyleName());
              }
           loadedItem = new TreeItem(screenLabel);
        }
        loadedItem.setUserObject(model.getItem(index).getUserObject());        
        view.tree.addItem(loadedItem);         
   }
    
    
    public void reset() {
        view.reset();
        view.tree.addTreeListener(treeListener);
        
        shown = 0;
        //ArrayList hasdummyChild = model.getHasDummyChild();
        
        for (int j = 0; j < model.numItems(); j++) {
            loadItem(j);            
            boolean hdc = (boolean)model.getItem(j).getHasDummyChild();
            if(hdc){          
            TreeItem dummyItem = new TreeItem("dummy");
            view.tree.getItem(j).addItem(dummyItem);
          } 
        }
        selected = -1;
        
        if (model.paged) {
            view.setNavPanel(model.pageIndex, model.totalPages, model.showIndex, this);           
        } 
       
    }
    
    public void getPage(int page, int selected) {
        try {
            treeService.getTreePage(page, selected, callback);
        } catch (RPCException e) {
            e.printStackTrace();
        }
    }
    
    public void getModel() {
       try{ 
        treeService.getTreeModel(model, callback);
       } catch (RPCException e) {
           e.printStackTrace();
       }
        
    }
    
    public void getModel(int setItem){
        this.setItem = setItem;
        getModel();
    }


    public void setTreeListener(TreeListener treeListener) {
        this.treeListener = treeListener;
        view.tree.addTreeListener(treeListener);
    }


    public TreeListener getTreeListener() {
        return treeListener;
    }
        
    public Widget getScrollableView(){
        vScroll.setWidget(view);        
        vScroll.setHeight(view.height);        
        vScroll.setWidth("100%");
        return vScroll;
    }
        
}
