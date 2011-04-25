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

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class will display a group of MenuItems in a vertical display that pops up over 
 * all other widgets on the screen 
 */
public class PopupMenuPanel extends PopupPanel {
    
    /**
     * Panel used to display Menus
     */
    protected VerticalPanel panel;
    
    /**
     * Div used to scroll panel when height exceeds screen height
     */
    protected AbsolutePanel ap;
    
    /**
     * Panels used to scroll the menu up or down
     */
    protected FocusPanel up, down;
    
    /**
     * Timers used to scroll the menu up or down
     */
    protected Timer upTimer, downTimer;
    
    /**
     * Reference to the current items child menu displayed
     */
    protected PopupPanel openChildMenu;
    
    /**
     * Enum to set the Dir to scroll the menu
     */
    protected enum SCROLL_DIR {UP,DOWN};
    
    /**
     * No-Arg constructor
     */
    public PopupMenuPanel() {
        super(true);
       
        VerticalPanel outer;
        
        outer = new VerticalPanel();
        
        /* Setup and add Up arrow scroller */
        up = new FocusPanel();
        up.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                upTimer.scheduleRepeating(50);
            }
        });
        up.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                upTimer.cancel();
            }
        });
        up.setStyleName("MenuUp");
        up.addStyleName("MenuDisabled");
        up.setVisible(false);
        outer.add(up);
        
        /* Setup scrolling window and panel display */
        ap = new AbsolutePanel();
        DOM.setStyleAttribute(ap.getElement(),"overflow","hidden");
        panel = new VerticalPanel();
        ap.add(panel);
        outer.add(ap);
        
        /* Setup and add Down arrow scroller */
        down = new FocusPanel();
        down.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                downTimer.scheduleRepeating(50);
            }
        });
        down.addMouseOutHandler(new MouseOutHandler() {
            public void onMouseOut(MouseOutEvent event) {
                downTimer.cancel();
            }
        });
        down.setStyleName("MenuDown");
        down.setVisible(false);
        outer.add(down);
        setWidget(outer);
       
        /* Add MouseWheel scrolling to menu */
        addDomHandler(new MouseWheelHandler() {
            public void onMouseWheel(MouseWheelEvent event) {
                if(event.isSouth() && down.getStyleName().indexOf("MenuDisabled") == -1)
                  scroll(SCROLL_DIR.DOWN);
                if(event.isNorth() && up.getStyleName().indexOf("MenuDisabled") == -1)
                  scroll(SCROLL_DIR.UP);
            }
        },MouseWheelEvent.getType());
        
        /* Setup Timer to scroll the menu down repeatedly */
        downTimer = new Timer() {
            public void run() {
                scroll(SCROLL_DIR.DOWN);
            }
        };
        
        /* Setup Timer to scroll the menu up repeatedly */
        upTimer = new Timer() {
            public void run() {
                scroll(SCROLL_DIR.UP);
             }
        };
        
        /* Add CloseHandler to ensure that any child menus that could be open are
         * also closed.
         */
        addCloseHandler(new CloseHandler<PopupPanel>() {
            public void onClose(CloseEvent<PopupPanel> event) {
                if(openChildMenu != null && openChildMenu.isShowing())
                    openChildMenu.hide();
            }
        });
        
    }
    
    /**
     * This method will add MenuItem to the display of this panel
     */
    public void addItem(MenuItem item){
        panel.add(item);
    
        /**
         * Add command to item to close this menu when the item is clicked when autoClose is true
         */
        if(item.autoClose()) {
            item.addCommand(new Command() {
                public void execute() {
                    hide();
                }
            });
        }
        
        /*
         * Setup MouseOver on MenuItem to hide an openChildMenu if necessary
         */
        item.addMouseOverHandler(new MouseOverHandler() {
           public void onMouseOver(MouseOverEvent event) {
               /*
                * Hide any currently shown ChildMenu
                */
               if(openChildMenu != null && openChildMenu.isShowing())  
                   openChildMenu.hide();
            } 
        });   
    }
    
    /**
     * This method will add a Menu to the display of this Panel.
     * @param menu
     */
    public void addItem(final Menu menu) {
        panel.add(menu);
        
        menu.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                if(openChildMenu != null && openChildMenu.isShowing())
                    openChildMenu.hide();
                
                openChildMenu = menu.showSubMenu();
            }
        });
    }
    
    /**
     * Clears all MenuItems and widgets from this panel
     */
    public void clear(){
        panel.clear();
    } 
    
    /**
     * Adds a line separating items in the panel
     */
    public void addMenuSeparator() {
        panel.add(new HTML("<hr/>"));
    }
    
    /**
     * Method overridden to check if the Menu's height needs to be adjusted and 
     * scrolling enables.
     */
    @Override
    protected void onAttach() {
        super.onAttach();
        if(panel.getOffsetHeight() > Window.getClientHeight()- getAbsoluteTop()){
            ap.setHeight((Window.getClientHeight()- getAbsoluteTop() - 50)+"px");
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
    
    /**
     * This method will scroll the menu up or down depending on when the mouse is placed over the up or down
     * panels or if the Mouse wheel is used.
     * @param dir
     */
    private void scroll(SCROLL_DIR dir) {
        if(dir == SCROLL_DIR.DOWN && down.getStyleName().indexOf("MenuDisabled") == -1){
            if(ap.getWidgetTop(panel) <= ap.getOffsetHeight() - panel.getOffsetHeight()){
                down.addStyleName("MenuDisabled");
                downTimer.cancel();
            }else{
                ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)-10);
                up.removeStyleName("MenuDisabled");
            }
        }
        if(dir == SCROLL_DIR.UP && up.getStyleName().indexOf("MenuDisabled") == -1){
            if(ap.getWidgetTop(panel) >= 0){
                up.addStyleName("MenuDisabled");
                upTimer.cancel();
            }else{
                ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)+10);
                down.removeStyleName("MenuDisabled");
            }
        }
    }
    
}
