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

import java.util.ArrayList;

import com.google.gwt.event.dom.client.HasMouseWheelHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MenuPanel extends Composite implements MouseWheelHandler, MouseOverHandler, MouseOutHandler{
    
    String layout;
    public CellPanel panel;
    
    private class MenuVP extends VerticalPanel implements HasMouseWheelHandlers {

		public HandlerRegistration addMouseWheelHandler(
				MouseWheelHandler handler) {
			return addDomHandler(handler, MouseWheelEvent.getType());
		}
        
    }
    
    public MenuItem activeItem;
    public AbsolutePanel ap;
    public boolean active;
    public ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
    public FocusPanel up = new FocusPanel();
    public FocusPanel down = new FocusPanel();
    Timer timer;

    public MenuPanel() {
        
    }
    
    public MenuPanel(String layout) {
        init(layout);
    }
    
    public void init(String layout) {
        if(layout.equals("vertical"))
            panel = new VerticalPanel();
        else
            panel = new HorizontalPanel();
        if(layout.equals("vertical")){
            MenuVP vp = new MenuVP();
            vp.addMouseWheelHandler(this);
            up.addMouseOverHandler(this);
            up.addMouseOutHandler(this);
            up.setStyleName("MenuUp");
            up.addStyleName("MenuDisabled");
            up.setVisible(false);
            vp.add(up);
            ap = new AbsolutePanel();
            DOM.setStyleAttribute(ap.getElement(),"overflow","hidden");
            ap.add(panel);
            vp.add(ap);
            down.addMouseOverHandler(this);
            down.addMouseOutHandler(this);
            down.setStyleName("MenuDown");
            down.setVisible(false);
            vp.add(down);
            initWidget(vp);
        }else
            initWidget(panel);
    }
    
    public void add(Widget wid){
        if(wid instanceof MenuItem){
            ((MenuItem)wid).parent = this;
            menuItems.add((MenuItem)wid);
        }
        panel.add(wid);
        
    }
    
    public void clear(){
        panel.clear();
    }
    
    public void itemEnter(MenuItem item) {
        if(activeItem != null && activeItem != item){
            boolean lowest = false;
            ArrayList<MenuItem> closeList = new ArrayList<MenuItem>();
            closeList.add(activeItem);
            MenuItem close = activeItem;
            while(!lowest){
                if(close.child == null || close.child.activeItem == null || close.child.activeItem.pop == null)
                    lowest = true;
                else{
                    closeList.add(close.child.activeItem);
                    close = close.child.activeItem;
                }
            }
            for(int i = closeList.size() -1; i > -1; i--){
                closeList.get(i).closePopup();
            }
            item.createPopup();
        }else if(active)
            item.createPopup();
    }
    
    public void itemLeave(MenuItem item) {
        
    }

    public void onMouseOver(MouseOverEvent event) {
    	try {
    	if(event.getSource() == down || event.getSource() == up) {
    		final Widget source = (Widget)event.getSource(); 
    		timer = new Timer() {
    			public void run() {
    				if(source == down){
    					if(ap.getWidgetTop(panel) <= ap.getOffsetHeight() - panel.getOffsetHeight()){
    						down.addStyleName("MenuDisabled");
    						cancel();
    					}else{
    						ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)-10);
    						up.removeStyleName("MenuDisabled");
    					}
    				}
    				if(source == up){
    					if(ap.getWidgetTop(panel) >= 0){
    						up.addStyleName("MenuDisabled");
    						cancel();
    					}else{
    						ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)+10);
    						down.removeStyleName("MenuDisabled");
    					}
    				}
    			}
    	    };
            timer.scheduleRepeating(50);
    	}
    	}catch(Exception e){
    		Window.alert(e.getMessage());
    	}
    }
    
    public void onMouseOut(MouseOutEvent event) {
        if(timer != null)
            timer.cancel();
        timer = null;
    }
    
    public void setSize(int top) {
        if(panel.getOffsetHeight() > Window.getClientHeight()- top){
            ap.setHeight((Window.getClientHeight()-top - 50)+"px");
            ap.setWidth(ap.getOffsetWidth()+"px");
            up.setVisible(true);
            down.setVisible(true);
        }else{
            ap.setHeight(panel.getOffsetHeight()+"px");
            ap.setWidth(panel.getOffsetWidth()+"px");
            ap.setWidgetPosition(panel, 0, 0);
            up.setVisible(false);
            down.setVisible(false);
        }
    }
    
    public void onMouseWheel(MouseWheelEvent event) {
        if(event.isSouth() && down.getStyleName().indexOf("MenuDisabled") == -1){
            if(ap.getWidgetTop(panel) <= ap.getOffsetHeight() - panel.getOffsetHeight()){
                down.addStyleName("MenuDisabled");
            }else{
                ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)-10);
                up.removeStyleName("MenuDisabled");
            }
        }
        if(event.isNorth() && up.getStyleName().indexOf("MenuDisabled") == -1){
            if(ap.getWidgetTop(panel) >= 0){
                up.addStyleName("MenuDisabled");
            }else{
                ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)+10);
                down.removeStyleName("MenuDisabled");
            }
        }
        
    }    


}
