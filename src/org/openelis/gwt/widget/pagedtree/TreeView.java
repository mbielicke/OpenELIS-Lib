/**
* The contents of this file are subject to the Mozilla Public License
* Version 1.1 (the "License"); you may not use this file except in
* compliance with the License. You may obtain a copy of the License at
* http://www.mozilla.org/MPL/
* 
* Software distributed under the License is distributed on an "AS IS"
* basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
* License for the specific language governing rights and limitations under
* the License.
* 
* The Original Code is OpenELIS code.
* 
* Copyright (C) OpenELIS.  All Rights Reserved.
*/
package org.openelis.gwt.widget.pagedtree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeImages;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.core.client.GWT;

public class TreeView extends Composite {
    
    private String title = "";    
    private String shown = "";
    protected HorizontalPanel navPanel = new HorizontalPanel(); 
    private VerticalPanel vp = new VerticalPanel();
    public ScrollPanel vScroll = new ScrollPanel();    
    
    //private VerticalPanel vtreePanel = new VerticalPanel();
    public String width;
    public String height;
    private String navLinks = "NavLinks";
    
    interface MyTreeImages extends TreeImages {
        
        /**
         * @gwt.resource dorado_tree_open.gif
         */
        AbstractImagePrototype treeOpen();
        
        /** 
         * @gwt.resource dorado_tree_closed.gif
         */
        AbstractImagePrototype treeClosed();
        
        /** 
         * @gwt.resource report.png
         */
      // AbstractImagePrototype treeLeaf();
      }
    
    TreeImages images = (TreeImages)GWT.create(MyTreeImages.class);
    
    //public Tree tree = new Tree();
    public Tree tree = new Tree(images);
    
    private final Label titleLabel = new Label();
    private final HorizontalPanel titlePanel = new HorizontalPanel();
    
    public TreeView() {
        initWidget(vp);
        initTree();
    }
    
    public void initTree(){ 
        
    	if(title != null ){
           if(!title.equals("")){
    		titleLabel.setText(title);
    		titlePanel.add(titleLabel);
    		titlePanel.addStyleName("TitlePanel");
    		vp.add(titlePanel);
           }
        }   
        vScroll.setWidget(tree);
        vp.add(vScroll);
        //vtreePanel.add(tree);
        //vp.add(vtreePanel);
        vp.add(navPanel);
    }
    
        
    public void setHeight(String height) {     
        vScroll.setHeight(height); 
        this.height = height; 
        //vtreePanel.setHeight(height);
        tree.setHeight(height);        
       
    }
    
    public void setWidth(String width) {
        
        vScroll.setWidth(width);
        this.width = width; 
        //vtreePanel.setWidth(width);
        tree.setWidth(width);               
    }
    
    public void setTreeListener(TreeListener listener) {
        tree.addTreeListener(listener);
    }
    
    public void reset(){
        //vtreePanel.clear();
        tree = new Tree(images);
        //tree = new Tree();
        vScroll.setWidget(tree);
        
        //vtreePanel.add(tree);
        tree.addStyleName("ScreenTree");
    }
    
    public void setTitle(String title) {
        this.title = title;
        //titleLabel.setText(title);
        if(title != null ){
            if(!title.trim().equals("")){
             if(!(vp.getWidget(0).equals(titlePanel))){                 
               titleLabel.setText(title);
               titlePanel.add(titleLabel);
               titlePanel.addStyleName("TitlePanel");
               vp.clear();
               vp.add(titlePanel);
               //vp.add(vtreePanel);
               vp.add(vScroll);
               vp.add(navPanel);
             }
            }
         }  
    }

    public void setShown(String shown) {
        this.shown = shown;
        titleLabel.setText(title + " " + shown);
    }
        
    public void setNavPanel(int curIndex, int pages, boolean showIndex, ClickListener listener) {
        vp.remove(navPanel);
        navPanel = new HorizontalPanel();
        navPanel.setSpacing(3);
        navPanel.addStyleName(navLinks);
        navPanel.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        
        HTML prevNav = new HTML("<a class='navIndex' value='-1'>&lt;</a>");
        prevNav.addClickListener(listener);        
        
        HTML noprevNav = new HTML("<a class='navIndex' style = 'color:lightgrey;'>&lt;</a>");        
        
        HTML nextNav = new HTML("<a class='navIndex' value='+1'>&gt;</a>");
        nextNav.addClickListener(listener);        
        
        HTML nonextNav = new HTML("<a class='navIndex' style = 'color:lightgrey;' >&gt;</a>");               
        HTML currNav = new HTML();
        currNav.addClickListener(listener);
        navPanel.add(currNav);
        currNav.setVisible(false);
        
        
        DOM.setAttribute(navPanel.getElement(), "align", "center");
        if (curIndex > 0) {            
            navPanel.add(prevNav);                       
            
        }
        else{            
            navPanel.add(noprevNav);
        }
        
        if(showIndex){
            for (int i = 1; i <= pages; i++) {
                final int index = i - 1;
        
                if (index != curIndex) {                 
                    currNav.setHTML("<a class='navIndex' value='" + index
                                    + "'>"
                                    + i
                                    + "</a>");
                    currNav.addClickListener(listener);
                } else {
                   
                    currNav.setHTML("" + i);
                    currNav.setStyleName("current");
                }              
                currNav.setVisible(true);
                
            }
        }else{
         currNav.setVisible(false);
        }
        
        if (curIndex < pages - 1) {
            
            navPanel.add(nextNav);         
           
        }
        else{
         
            navPanel.add(nonextNav);
        }
        vp.add(navPanel);
    }

    public String getShown() {
        return shown;
    }

    
}
