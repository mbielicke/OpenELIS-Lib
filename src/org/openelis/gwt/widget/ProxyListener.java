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

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.MouseDragGestureRecognizer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.screen.ScreenWidget;

import java.util.Vector;
/**
 * ProxyListener is a DragListener for dragging widgets around a 
 * screen by proxy.  It must be in the WidgetMap as "ProxyListener" 
 * for it to be available to widgets in the application.
 * 
 * @author tschmidt
 *
 */
public class ProxyListener extends MouseListenerAdapter implements DragListener {

    Vector dropMap;
    
    public void onMouseDown(Widget sender, final int x, final int y) {

        final ScreenWidget proxy = ((ScreenWidget)sender).getInstance();
        AbsolutePanel dragIndicator = new AbsolutePanel();
        dragIndicator.setStyleName("DragStatus");
        dragIndicator.addStyleName("NoDrop");
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(dragIndicator);
        hp.add(proxy);
        hp.setStyleName(sender.getStyleName());
        proxy.setUserObject(sender);
        proxy.addDragListener(this);
        RootPanel.get().add(hp);
        MouseDragGestureRecognizer mouse = MouseDragGestureRecognizer.getGestureMouse(proxy);
        mouse.setDrag(hp);
        dropMap = MouseDragGestureRecognizer.getDropMap();
        MouseDragGestureRecognizer.setDropMap(((ScreenWidget)sender).getDropMap());
        MouseDragGestureRecognizer.setWidgetPosition(hp,
                                                     sender.getAbsoluteLeft(),
                                                     sender.getAbsoluteTop());
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                MouseDragGestureRecognizer.getGestureMouse(proxy)
                                          .onMouseDown(proxy, x, y);
            }
        });
    }

    public void onDragDropEnd(Widget sender, Widget target) {
        // TODO Auto-generated method stub

    }

    public void onDragEnd(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        DOM.removeChild(RootPanel.get().getElement(), sender.getParent().getElement());
        //MouseDragGestureRecognizer.setDropMap(dropMap);
    }

    public void onDragEnter(Widget sender, Widget target) {
        ((HorizontalPanel)sender.getParent()).getWidget(0).removeStyleName("NoDrop");
        ((HorizontalPanel)sender.getParent()).getWidget(0).addStyleName("Drop");
    }

    public void onDragExit(Widget sender, Widget target) {
        ((HorizontalPanel)sender.getParent()).getWidget(0).removeStyleName("Drop");
        ((HorizontalPanel)sender.getParent()).getWidget(0).addStyleName("NoDrop");

    }

    public void onDragMouseMoved(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    public void onDragOver(Widget sender, Widget target) {
        // TODO Auto-generated method stub

    }

    public void onDragStart(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }
}
