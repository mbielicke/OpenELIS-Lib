package org.openelis.gwt.widget;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DropListener;
import com.google.gwt.user.client.dnd.MouseDragGestureRecognizer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 * DragSelect widget is a widget where multiple selections can be made by
 * dragging items between two list boxes.  The widget uses an optionfield to
 * recieve and send data to the RemoteServer.
 * @author tschmidt
 *
 */
public class DragSelectWidget extends Composite implements
                                               KeyboardListener,
                                               MouseListener,
                                               FocusListener,
                                               DragListener,
                                               DropListener {
    private final FocusPanel from = new FocusPanel();
    private final FocusPanel to = new FocusPanel();
    private final ScrollPanel fromScroll = new ScrollPanel();
    private final ScrollPanel toScroll = new ScrollPanel();
    private final VerticalPanel fromPanel = new VerticalPanel();
    private final VerticalPanel toPanel = new VerticalPanel();
    private DragWidget dragWidgets[];
    private final DockPanel outer = new DockPanel();
    private PopupPanel dragPanel;
    private DragWidget dragWidget;
    private int active = -1;
    public boolean changed;

    public DragSelectWidget() {
        //outer.setStyleName("DragSelect");
        from.addStyleName("locked");
        to.addStyleName("locked");
        draw();
    }

    public DragSelectWidget(DragWidget[] widgets) {
        draw();
        setDragWidgets(widgets);
    }

    public void setAccessKey(char key) {
        from.setAccessKey(key);
    }

    public void addFocusListener(FocusListener listener) {
        from.addFocusListener(listener);
    }

    public void setFocus(boolean focus) {
        from.setFocus(focus);
    }

    private void draw() {
        from.add(fromScroll);
        to.add(toScroll);
        from.addDropListener(this);
        to.addDropListener(this);
        fromScroll.add(fromPanel);
        toScroll.add(toPanel);
        outer.add(from, DockPanel.WEST);
        outer.add(to, DockPanel.EAST);
        fromScroll.setStyleName("DragContainer");
        toScroll.setStyleName("DragContainer");
        outer.setCellHorizontalAlignment(from, HasAlignment.ALIGN_LEFT);
        outer.setCellHorizontalAlignment(to, HasAlignment.ALIGN_RIGHT);
        toPanel.setWidth("100%");
        fromPanel.setWidth("100%");
        initWidget(outer);
        from.addKeyboardListener(this);
        // to.addFocusListener(this);
    }

    public DragWidget[] getDragWidgets() {
        return dragWidgets;
    }

    public void setDragWidgets(DragWidget[] dragWidgets) {
        this.dragWidgets = dragWidgets;
        fromPanel.clear();
        toPanel.clear();
        for (int i = 0; i < dragWidgets.length; i++) {
            fromPanel.add(dragWidgets[i]);
            if (dragWidgets[i].selected) {
                DragWidget toWidget = new DragWidget(dragWidgets[i]);
                toPanel.insert(toWidget, 0);
                dragWidgets[i].addStyleName("Inactive");
            }
        }
    }

    public void setEnabled(boolean enabled) {
        if (!enabled) {
            for (int i = 0; i < dragWidgets.length; i++) {
                if (!dragWidgets[i].selected)
                    dragWidgets[i].removeMouseListener(this);
            }
            for (int i = 0; i < toPanel.getWidgetCount(); i++) {
                if (toPanel.getWidget(i) instanceof DragWidget)
                    ((DragWidget)toPanel.getWidget(i)).removeMouseListener(this);
            }
        } else {
            for (int i = 0; i < dragWidgets.length; i++) {
                if (!dragWidgets[i].selected)
                    dragWidgets[i].addMouseListener(this);
            }
            for (int i = 0; i < toPanel.getWidgetCount(); i++) {
                if (toPanel.getWidget(i) instanceof DragWidget)
                    ((DragWidget)toPanel.getWidget(i)).addMouseListener(this);
            }
        }
    }

    public void onMouseDown(Widget sender, final int x, final int y) {
        dragWidget = (DragWidget)sender;
        final Label proxy = new Label(dragWidget.getText());
        proxy.addDragListener(this);
        RootPanel.get().add(proxy);
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

    public void onMouseUp(Widget sender, int x, int y) {
    }

    public void onMouseMove(Widget sender, int x, int y) {
    }

    public void onKeyUp(Widget sender, char key, int modifier) {
        if ((int)key == KeyboardListener.KEY_DOWN) {
            if (active >= 0)
                dragWidgets[active].removeStyleName("selected");
            active++;
            if (active == dragWidgets.length)
                active = 0;
            dragWidgets[active].addStyleName("selected");
        }
        if ((int)key == KeyboardListener.KEY_UP) {
            if (active >= 0)
                dragWidgets[active].removeStyleName("selected");
            active--;
            if (active < 0)
                active = dragWidgets.length - 1;
            dragWidgets[active].addStyleName("selected");
        }
        if ((int)key == KeyboardListener.KEY_RIGHT) {
            if (active >= 0) {
                if (!dragWidgets[active].selected) {
                    DragWidget dropWidget = new DragWidget(dragWidgets[active]);
                    dropWidget.addMouseListener(this);
                    dropWidget.selected = true;
                    toPanel.add(dropWidget);
                    dragWidgets[active].removeMouseListener(this);
                    dragWidgets[active].removeStyleName("selected");
                    dragWidgets[active].addStyleName("Inactive");
                    dragWidgets[active].addStyleName("selected");
                    dragWidgets[active].selected = true;
                }
                changed = true;
            }
        }
        if ((int)key == KeyboardListener.KEY_LEFT) {
            if (active >= 0) {
                if (dragWidgets[active].selected) {
                    for (int i = 0; i < toPanel.getWidgetCount(); i++) {
                        DragWidget toWidget = (DragWidget)toPanel.getWidget(i);
                        if (toWidget.value == dragWidgets[active].value) {
                            toPanel.remove(toWidget);
                            dragWidgets[active].addMouseListener(this);
                            dragWidgets[active].removeStyleName("selected");
                            dragWidgets[active].removeStyleName("Inactive");
                            dragWidgets[active].addStyleName("selected");
                            dragWidgets[active].selected = false;
                            break;
                        }
                    }
                }
                changed = true;
            }
        }
    }

    public void onKeyPress(Widget sender, char key, int modifier) {
    }

    public void onKeyDown(Widget sender, char key, int modifier) {
    }

    public void onFocus(Widget sender) {
        from.setFocus(true);
    }

    public void onLostFocus(Widget sender) {
        if (active >= 0) {
            dragWidgets[active].removeStyleName("selected");
            active = -1;
        }
    }

    public void reset() {
        for (int i = 0; i < dragWidgets.length; i++) {
            dragWidgets[i].selected = false;
        }
        setDragWidgets(dragWidgets);
    }

    public void onDragDropEnd(Widget sender, Widget target) {
        // TODO Auto-generated method stub
    }

    public void onDragEnd(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        DOM.removeChild(RootPanel.getBodyElement(), sender.getElement());

    }

    public void onDragEnter(Widget sender, Widget target) {
        // TODO Auto-generated method stub

    }

    public void onDragExit(Widget sender, Widget target) {
        // TODO Auto-generated method stub

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

    public void onDrop(Widget sender, Widget source) {
        // TODO Auto-generated method stub
        if (sender == to && !dragWidget.selected) {
            DragWidget dropWidget = new DragWidget(dragWidget);
            dropWidget.addMouseListener(this);
            dropWidget.selected = true;
            toPanel.add(dropWidget);
            dragWidget.removeMouseListener(this);
            dragWidget.addStyleName("Inactive");
            dragWidget.selected = true;
        }
        if (sender == from && dragWidget.selected) {
            for (int i = 0; i < fromPanel.getWidgetCount(); i++) {
                DragWidget fromWidget = (DragWidget)fromPanel.getWidget(i);
                if (fromWidget.value == dragWidget.value) {
                    toPanel.remove(dragWidget);
                    fromWidget.addMouseListener(this);
                    fromWidget.removeStyleName("Inactive");
                    fromWidget.selected = false;
                    break;
                }
            }
        }
        changed = true;
    }

    public void onDropEnter(Widget sender, Widget source) {
        // TODO Auto-generated method stub

    }

    public void onDropExit(Widget sender, Widget source) {
        // TODO Auto-generated method stub

    }

    public void onDropOver(Widget sender, Widget source) {
        // TODO Auto-generated method stub

    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub

    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub

    }

    public void setHeight(String height) {
        fromScroll.setHeight(height);
        toScroll.setHeight(height);
    }

    public void setWidth(String width) {
        fromScroll.setWidth(width);
        toScroll.setWidth(width);
    }
}