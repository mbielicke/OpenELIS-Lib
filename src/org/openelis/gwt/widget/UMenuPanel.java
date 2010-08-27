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
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class UMenuPanel extends PopupPanel {
    
    protected VerticalPanel panel;
    protected AbsolutePanel ap;
    protected FocusPanel up = new FocusPanel();
    protected FocusPanel down = new FocusPanel();
    protected Timer upTimer, downTimer;
    
    public UMenuPanel() {
        super(true);
        VerticalPanel outer = new VerticalPanel();
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
        ap = new AbsolutePanel();
        DOM.setStyleAttribute(ap.getElement(),"overflow","hidden");
        panel = new VerticalPanel();
        ap.add(panel);
        outer.add(ap);
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
       
        addDomHandler(new MouseWheelHandler() {
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
        },MouseWheelEvent.getType());
        downTimer = new Timer() {
            public void run() {
                if(ap.getWidgetTop(panel) <= ap.getOffsetHeight() - panel.getOffsetHeight()){
                    down.addStyleName("MenuDisabled");
                    cancel();
                }else{
                    ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)-10);
                    up.removeStyleName("MenuDisabled");
                }
            }
        };
        upTimer = new Timer() {
            public void run() {
                if(ap.getWidgetTop(panel) >= 0){
                     up.addStyleName("MenuDisabled");
                    cancel();
                }else{
                    ap.setWidgetPosition(panel, 0, ap.getWidgetTop(panel)+10);
                    down.removeStyleName("MenuDisabled");
                }
             }
        };
    }
    
    public void add(Widget wid){
        ((VerticalPanel)ap.getWidget(0)).add(wid);
    }
    
    public void clear(){
        panel.clear();
    }
    
    public void show(int x, int y) {
        setPopupPosition(x, y);
        show();
    }
    
    public void hide() {
        hide();
    }
    
}
