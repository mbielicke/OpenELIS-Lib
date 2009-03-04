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
* Copyright (C) The University of Iowa.  All Rights Reserved.
*/
package org.openelis.gwt.widget;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesChangeEvents;
import com.google.gwt.user.client.ui.Widget;

public class CollapsePanel extends Composite implements ClickListener, MouseListener, SourcesChangeEvents{
    
    private HorizontalPanel panel = new HorizontalPanel();
    private HorizontalPanel content = new HorizontalPanel();
    private FocusPanel middleBar = new FocusPanel();
    private FocusPanel arrow = new FocusPanel();
    public boolean isOpen;
    private ChangeListenerCollection changeListeners;
    
    public CollapsePanel(){
        initWidget(panel);
        panel.setHeight("100%");
        content.setVisible(false);
        middleBar.setHeight("100%");
        middleBar.setStyleName("LeftMenuPanePanelClosed");
        arrow.setStyleName("LeftMenuPanePanelDiv");
        arrow.addClickListener(this);
        arrow.addMouseListener(this);
        middleBar.add(arrow);
        panel.add(content);
        panel.add(middleBar);   
    }
    
    public void setContent(Widget wid){
        if(content.getWidgetCount() > 0)
            content.remove(0);
        content.add(wid);
    }
    
    public void open() {
        if(!isOpen){
            content.setVisible(true);
            middleBar.setStyleName("LeftMenuPanePanelOpen");
            arrow.setFocus(false);
            isOpen = true;
            changeListeners.fireChange(this);
        }
    }
    
    public void close(){
        if(isOpen){
            content.setVisible(false);
            middleBar.setStyleName("LeftMenuPanePanelClosed");
            arrow.setFocus(false);
            isOpen = false;
            changeListeners.fireChange(this);
        }
    }

    public void onClick(Widget sender) {
        if(content.isVisible()){
            close();
        }else{
            open();
        }   
        
    }

    public void onMouseDown(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseEnter(Widget sender) {
        arrow.addStyleName("Hover");
        middleBar.addStyleName("Hover");
        
    }

    public void onMouseLeave(Widget sender) {
        arrow.removeStyleName("Hover");
        middleBar.addStyleName("Hover");
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void addChangeListener(ChangeListener listener) {
        if(changeListeners == null){
            changeListeners = new ChangeListenerCollection();
        }
        changeListeners.add(listener);
        
    }

    public void removeChangeListener(ChangeListener listener) {
       if(changeListeners != null){
           changeListeners.remove(listener);
       }
        
    }
    

}
