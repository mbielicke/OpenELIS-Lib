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
package org.openelis.gwt.widget.rewrite;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class ScrollableTabBar extends Composite implements ClickHandler, HasSelectionHandlers<Integer>, HasBeforeSelectionHandlers<Integer>  {
  
  private HorizontalPanel mainPanel = new HorizontalPanel();
  private AbsolutePanel scrollPanel = new AbsolutePanel();
  private TabBar tabBar = new TabBar();
  private HTML next = new HTML("<img src=\"Images/nextbuttonimage.gif\">");
  private HTML previous = new HTML("<img src=\"Images/previousbuttonimage.gif\">");
  private boolean noHTMLs = true;
  private boolean nextDisabled = false;   
  private boolean prevDisabled = false;
  ArrayList<Widget> tabWidgets = new ArrayList<Widget>();
  
  public ScrollableTabBar(){
      initWidget(mainPanel);
      scrollPanel.add(tabBar);
      scrollPanel.setHeight("20px");
      scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)-10 , 0);       
      DOM.setStyleAttribute(scrollPanel.getElement(), "overflow", "hidden"); 
      previous.addClickHandler(this);
      next.addClickHandler(this);     
      
      if(scrollPanel.getOffsetWidth() > tabBar.getOffsetWidth()){
        noHTMLs = false;  
      }
      
      if(!noHTMLs){
       mainPanel.add(previous);
      }
      mainPanel.add(scrollPanel);
      
      if(!noHTMLs){
       mainPanel.add(next);
      } 
      
      tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
    	  public void onSelection(SelectionEvent<Integer> event) {
    		  selectTab(event.getSelectedItem());
    	  }
      });
                
  }
  
  public void onClick(ClickEvent event) {   
    if(event.getSource() == next){                 
      scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)+10 , 0);      
      manageScrolling(scrollPanel.getWidgetLeft(tabBar)+10);
    }
    if(event.getSource() == previous){               
       scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)-10 , 0);
       manageScrolling(scrollPanel.getWidgetLeft(tabBar)-10);
    }   
  }
  
  public void selectTab(int index){
     tabBar.selectTab(index); 
     SelectionEvent.fire(this,index);
  }
  
  public void setWidth(String width){
      mainPanel.setWidth(width);         
  }
  
  public void addTabWithContent(String text,Widget content) {
      addTab(text);
      tabWidgets.add(content);
  }
  
  public void addTabWithContent(Widget widget,Widget content) {
      addTab(widget);
      tabWidgets.add(content);
  }
    
  public void addTab(String text){           
      tabBar.addTab(text);     
      if(scrollPanel.getOffsetWidth() < tabBar.getOffsetWidth()){                   
             mainPanel.clear();              
             previous = new HTML("<img src=\"Images/previousbuttonimage.gif\">");
             previous.addClickHandler(this);
             
             mainPanel.add(previous); 
             mainPanel.add(scrollPanel);
                       
             mainPanel.add(next);                      
      }
   }
  
  public void addTab(Widget widget){
      tabBar.addTab(widget);
   }
  
  public void removeTab(int index){
     tabWidgets.remove(index);
     tabBar.removeTab(index);
     if(scrollPanel.getOffsetWidth() > tabBar.getOffsetWidth()){          
             mainPanel.clear();                      
             mainPanel.add(scrollPanel);             
         } 
      }
  
  public void clearTabs(){      
     tabBar = new TabBar(); 
     scrollPanel.clear();
     scrollPanel.add(tabBar);
     mainPanel.clear();  
     mainPanel.add(scrollPanel);
  }
  
  public TabBar getTabBar(){
      return tabBar;
  }
     
  
  
  private void manageScrolling(int left){           
       if(left >= 0){          
           mainPanel.remove(2);
           next = new HTML("<img src=\"Images/nextbuttonimagedisabled.gif\">");
           nextDisabled = true;
           mainPanel.add(next);
       }
       else{
          if(nextDisabled){ 
           mainPanel.remove(2);
           next = new HTML("<img src=\"Images/nextbuttonimage.gif\">");
           next.addClickHandler(this);
           mainPanel.add(next);    
           nextDisabled = false;
         }  
         
       }
       
       if(tabBar.getOffsetWidth() < (scrollPanel.getOffsetWidth()-left)){
           mainPanel.clear();
           previous = new HTML("<img src=\"Images/previousbuttonimagedisabled.gif\">");
           mainPanel.add(previous);
           mainPanel.add(scrollPanel);
           mainPanel.add(next);
           prevDisabled = true;
        }else{
         if(prevDisabled){  
           mainPanel.clear();
           previous = new HTML("<img src=\"Images/previousbuttonimage.gif\">");           
           previous.addClickHandler(this);
           mainPanel.add(previous);
           mainPanel.add(scrollPanel);
           mainPanel.add(next);
           prevDisabled = false;
          } 
         
        }
   }
  
   public void sizeTabBar() {
       scrollPanel.setWidth(mainPanel.getOffsetWidth()+"px");
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
