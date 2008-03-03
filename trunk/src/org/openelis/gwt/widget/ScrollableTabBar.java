package org.openelis.gwt.widget;

import org.openelis.gwt.screen.ScreenBase;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.Widget;

public class ScrollableTabBar extends Composite implements ClickListener{
  private HorizontalPanel mainPanel = new HorizontalPanel();
  private AbsolutePanel scrollPanel = new AbsolutePanel();
  private TabBar tabBar = new TabBar();
  private HTML next = new HTML("<img src=\"Images/resultset_next.png\">");
  private HTML previous = new HTML("<img src=\"Images/resultset_previous.png\">");
  private boolean noHTMLs = true;
  private boolean nextDisabled = false;   
  private boolean prevDisabled = false; 
  
  public ScrollableTabBar(){
      initWidget(mainPanel);
      scrollPanel.add(tabBar);
      scrollPanel.setWidth("300px");
      scrollPanel.setHeight("20px");
      scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)-10 , 0);       
      DOM.setStyleAttribute(scrollPanel.getElement(), "overflow", "hidden"); 
      previous.addClickListener(this);
      next.addClickListener(this);     
      
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
                
  }
  
  public void onClick(Widget sender) {   
    if(sender == next){        
        //Window.alert(new Integer(left).toString());  
      scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)+10 , 0);
      //Window.alert("left "+new Integer(left).toString());
      manageScrolling(scrollPanel.getWidgetLeft(tabBar)+10);
    }
    if(sender == previous){       
        //Window.alert(new Integer(right).toString()); 
       scrollPanel.setWidgetPosition(tabBar,scrollPanel.getWidgetLeft(tabBar)-10 , 0);
       manageScrolling(scrollPanel.getWidgetLeft(tabBar)-10);
    }
  }
  
  public void selectTab(int index){
     tabBar.selectTab(index); 
  }
  
  public void addTabListener(ScreenBase screen){
     tabBar.addTabListener(screen);
  }
  
  public void addTab(String text){           
      tabBar.addTab(text);     
      if(scrollPanel.getOffsetWidth() < tabBar.getOffsetWidth()){                   
             mainPanel.clear(); 
             /*if(scrollPanel.getOffsetWidth() < tabBar.getOffsetWidth()){
                 
             }
             else{
               previous = new HTML("<img src=\"Images/resultset_previous_disabled.png\">");   
             }*/
             previous = new HTML("<img src=\"Images/resultset_previous.png\">");
             previous.addClickListener(this);
             
             mainPanel.add(previous); 
             mainPanel.add(scrollPanel);
             
            /* if(scrollPanel.getWidgetLeft(tabBar)==0){
                 next = new HTML("<img src=\"Images/resultset_next_disabled.png\">");
             }
                     
               next = new HTML("<img src=\"Images/resultset_next.png\">");
               next.addClickListener(this);
             }*/
             mainPanel.add(next);         
             
      }
   }
  
  public void addTab(Widget widget){
      tabBar.addTab(widget);
   }
  
  public void removeTab(int index){
     tabBar.removeTab(index);
     if(scrollPanel.getOffsetWidth() > tabBar.getOffsetWidth()){          
             mainPanel.clear();                      
             mainPanel.add(scrollPanel);             
         } 
      }
     
  
  
  private void manageScrolling(int left){           
       if(left >= 0){          
           mainPanel.remove(2);
           next = new HTML("<img src=\"Images/resultset_next_disabled.png\">");
           //next.setEnabled(false);  
           nextDisabled = true;
           mainPanel.add(next);
       }
       else{
         //if(!next.isEnabled()){ 
          //Window.alert("nextDisabled "+new Boolean(nextDisabled).toString()); 
          if(nextDisabled){ 
           mainPanel.remove(2);
           next = new HTML("<img src=\"Images/resultset_next.png\">");
           //next.setEnabled(true);  
           next.addClickListener(this);
           mainPanel.add(next);    
           nextDisabled = false;
         }  
         
       }
       
       if(tabBar.getOffsetWidth() < (scrollPanel.getOffsetWidth()-left)){
           mainPanel.clear();
           previous = new HTML("<img src=\"Images/resultset_previous_disabled.png\">");
           //previous.setEnabled(false);
           mainPanel.add(previous);
           mainPanel.add(scrollPanel);
           mainPanel.add(next);
           prevDisabled = true;
        }else{
          //if(!previous.isEnabled()){ 
           // Window.alert("prevDisabled "+new Boolean(prevDisabled).toString());    
         if(prevDisabled){  
           mainPanel.clear();
           previous = new HTML("<img src=\"Images/resultset_previous.png\">");           
           //previous.setEnabled(true);
           previous.addClickListener(this);
           mainPanel.add(previous);
           mainPanel.add(scrollPanel);
           mainPanel.add(next);
           prevDisabled = false;
          } 
         
        }
       
       
   }
  
}
