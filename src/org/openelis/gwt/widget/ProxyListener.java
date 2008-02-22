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

import java.util.Vector;

import org.openelis.gwt.screen.ScreenHorizontal;
import org.openelis.gwt.screen.ScreenWidget;
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

        final ScreenHorizontal proxy = new ScreenHorizontal(((ScreenWidget)sender).getScreen(),"proxy");
        AbsolutePanel dragIndicator = new AbsolutePanel();
        dragIndicator.setStyleName("DragStatus");
        dragIndicator.addStyleName("NoDrop");
        ((HorizontalPanel)proxy.getWidget()).add(dragIndicator);
        ((HorizontalPanel)proxy.getWidget()).add(((ScreenWidget)sender).getInstance());
        proxy.setStyleName(sender.getStyleName());
        proxy.setUserObject(sender);
        proxy.addDragListener(this);
        RootPanel.get().add(proxy);
        MouseDragGestureRecognizer mouse = MouseDragGestureRecognizer.getGestureMouse(proxy);
        dropMap = MouseDragGestureRecognizer.getDropMap();
        MouseDragGestureRecognizer.setDropMap(((ScreenWidget)sender).getDropMap());
        MouseDragGestureRecognizer.setWidgetPosition(proxy,
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
        DOM.removeChild(sender.getParent().getElement(), sender.getElement());
        //MouseDragGestureRecognizer.setDropMap(dropMap);
    }

    public void onDragEnter(Widget sender, Widget target) {
        ((HorizontalPanel)((ScreenHorizontal)sender).getWidget()).getWidget(0).removeStyleName("NoDrop");
        ((HorizontalPanel)((ScreenHorizontal)sender).getWidget()).getWidget(0).addStyleName("Drop");
    }

    public void onDragExit(Widget sender, Widget target) {
        ((HorizontalPanel)((ScreenHorizontal)sender).getWidget()).getWidget(0).removeStyleName("Drop");
        ((HorizontalPanel)((ScreenHorizontal)sender).getWidget()).getWidget(0).addStyleName("NoDrop");

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