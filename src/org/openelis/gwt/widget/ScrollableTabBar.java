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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ScrollableTabBar extends Composite implements ClickHandler, HasSelectionHandlers<Integer>, HasBeforeSelectionHandlers<Integer>, MouseDownHandler, MouseUpHandler  {
  
  private VerticalPanel outer = new VerticalPanel();
  private HorizontalPanel barPanel = new HorizontalPanel();
  private AbsolutePanel scrollPanel = new AbsolutePanel();
  private AbsolutePanel content = new AbsolutePanel();
  private TabBar tabBar = new TabBar();
  private IconContainer next = new IconContainer();
  private IconContainer previous = new IconContainer();
  private Timer timer;
  
  public ScrollableTabBar(){
      initWidget(outer);
      outer.add(barPanel);
      outer.add(content);
      outer.setSpacing(0);
      barPanel.setSpacing(0);
      barPanel.setHeight("20px");
      outer.setCellHeight(barPanel, "20px");
      outer.setCellWidth(barPanel,"100%");
      outer.setCellHeight(content, "100%");
      outer.setCellWidth(content,"100%");
      content.setHeight("100%");
      content.setWidth("100%");
      //DOM.setStyleAttribute(content.getElement(), "border", "1px solid black");
      scrollPanel.add(tabBar);
      scrollPanel.setHeight("20px");
      DOM.setStyleAttribute(scrollPanel.getElement(), "overflow", "hidden"); 
      previous.addClickHandler(this);
      next.addClickHandler(this); 
      next.addMouseDownHandler(this);
      previous.addMouseDownHandler(this);
      next.addMouseUpHandler(this);
      previous.addMouseUpHandler(this);
      previous.setStyleName("MoveLeft");
      next.setStyleName("MoveRight");
      previous.addStyleName("inactive");
      next.addStyleName("inactive");
      tabBar.setVisible(false);
 
      barPanel.add(previous);
      barPanel.add(scrollPanel);
      barPanel.add(next);
      barPanel.setCellHeight(previous, "20px");
      barPanel.setStyleName("tabbar");

      final HasSelectionHandlers<Integer>  source = this;
      tabBar.addSelectionHandler(new SelectionHandler<Integer>() {
    	  public void onSelection(SelectionEvent<Integer> event) {
    		  SelectionEvent.fire(source,event.getSelectedItem());
    		  scrollToSelected();
    	  }
      });
                
  }
  
  public void onClick(ClickEvent event) {   
    if(event.getSource() == previous && scrollPanel.getWidgetLeft(tabBar) < 0){                 
       scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)+15 , 0);      
    }
    if(event.getSource() == next && scrollPanel.getWidgetLeft(tabBar) > -(tabBar.getOffsetWidth()-scrollPanel.getOffsetWidth())){               
       scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)-15 , 0);
    }  
    checkScroll();
  }
  
  public void selectTab(int index){
     tabBar.selectTab(index); 
  }
  
  public void setWidth(String width){
      outer.setWidth(width);
      int wid = Integer.parseInt(width.substring(0,width.indexOf("px"))) - 32;
      scrollPanel.setWidth(wid+"px");
  }
  
  public void setHeight(String height) {
	  outer.setHeight(height);
	  int hght = Integer.parseInt(height.substring(0,height.indexOf("px"))) - 20;
	  content.setHeight(hght+"px");
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
   
  public void setContent(Widget wid) {
	  content.clear();
	  content.add(wid);
  }

  public void onMouseDown(MouseDownEvent event) {
	  if(event.getSource() == previous){
	   	timer = new Timer() {
	   		public void run() {
	   			if(scrollPanel.getWidgetLeft(tabBar) < 0){       
	   				scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)+15 , 0);
	   			}
	   			checkScroll();
	   		}
	   	};
	  }else {
    	timer = new Timer() {
    		public void run() {
    			if(scrollPanel.getWidgetLeft(tabBar) > -(tabBar.getOffsetWidth()-scrollPanel.getOffsetWidth())){   
    				scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)-15 , 0);
    			}
    			checkScroll();
    		}	    			
	    };
	  }
	  timer.scheduleRepeating(100);
  }

  public void onMouseUp(MouseUpEvent event) {
	if(timer != null)
		timer.cancel();
	timer = null;
  }
  
  private void checkScroll() {
	  if(scrollPanel.getWidgetLeft(tabBar) >= 0){
		  if(previous.getStyleName().indexOf("inactive") == -1 )
			  previous.addStyleName("inactive");
	  }else
		  previous.removeStyleName("inactive");
	  if(scrollPanel.getWidgetLeft(tabBar) <= -(tabBar.getOffsetWidth()-scrollPanel.getOffsetWidth())){
		  if(next.getStyleName().indexOf("inactive") == -1)
			  next.addStyleName("inactive");
	  }else
		  next.removeStyleName("inactive");
  }
  
  
  public void scrollToSelected() {
	  Widget wid = (Widget)tabBar.getTab(tabBar.getSelectedTab());
	  int left = wid.getAbsoluteLeft();
	  int width = wid.getOffsetWidth();
	  int barLeft = scrollPanel.getAbsoluteLeft();
	  int barWidth = scrollPanel.getOffsetWidth();
	  if(left+width > barLeft+barWidth) {
		  scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)-((left+width)-(barLeft+barWidth)) , 0);
	  }else if(left < barLeft){
		  scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)+(barLeft-left) , 0);
	  }
	  checkScroll();
  }
}
