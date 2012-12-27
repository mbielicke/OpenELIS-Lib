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
package org.openelis.gwt.widget;

import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class ScrollableTabBar extends Composite implements HasSelectionHandlers<Integer>, HasBeforeSelectionHandlers<Integer>  {
  
  private TabBar tabBar = new TabBar();
  private TabBarScroller barScroller;
  
  public ScrollableTabBar(){
	  barScroller = new TabBarScroller(tabBar);
      initWidget(barScroller);

      final HasSelectionHandlers<Integer>  source = this;
      tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
    	  public void onSelection(SelectionEvent<Integer> event) {
    		  SelectionEvent.fire(source,event.getSelectedItem());
    		  barScroller.scrollToSelected();
    	  }
      });
                
  }
  
  public void selectTab(int index){
     tabBar.selectTab(index); 
  }
  
  public void setWidth(String width){
      barScroller.setWidth(width);
  }
  
  public void setHeight(String height) {
	  barScroller.setHeight(height);
  }
    
  public void addTab(String text){      
      tabBar.addTab(text);   
      tabBar.setVisible(true);
  }
  
  public void addTab(Widget widget){
      tabBar.addTab(widget);  
      tabBar.setVisible(true);
  }
  
  public void addTabAndSelect(String text){      
      tabBar.addTab(text); 
      selectTab(tabBar.getTabCount()-1);
      tabBar.setVisible(true);
  }
  
  public void addTabAndSelect(Widget widget){
      tabBar.addTab(widget);
      selectTab(tabBar.getTabCount()-1);
      tabBar.setVisible(true);
  }
    
  public void removeTab(int index){
     tabBar.removeTab(index);
     if(tabBar.getTabCount() == 0)
    	 tabBar.setVisible(false);
  }
  
  public void clearTabs(){   
     while(tabBar.getTabCount() > 0) 
         removeTab(0); 
  }
  
  public TabBar getTabBar(){
      return tabBar;
  }

   public void onSelection(SelectionEvent<Integer> event) {
	   selectTab(event.getSelectedItem());
	
   }

   public HandlerRegistration addSelectionHandler(SelectionHandler<Integer> handler) {
	   return addHandler(handler,SelectionEvent.getType());
   }

   public HandlerRegistration addBeforeSelectionHandler(
		BeforeSelectionHandler<Integer> handler) {
	   return tabBar.addBeforeSelectionHandler(handler);
   }
}
