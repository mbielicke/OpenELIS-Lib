package org.openelis.gwt.client.screen;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.dnd.DragListener;
import com.google.gwt.user.client.dnd.DragListenerCollection;
import com.google.gwt.user.client.dnd.MouseDragGestureRecognizer;
import com.google.gwt.user.client.dnd.SourcesDragEvents;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import org.openelis.gwt.client.widget.FormInt;
import org.openelis.gwt.client.widget.WindowBrowser;

import java.util.Vector;

/**
 * ScreenWindow is used to display Screens inside a draggable window.  
 * ScreenWindows are currently only used inside a WindowBrowser 
 * @author tschmidt
 *
 */
public class ScreenWindow extends Composite implements DragListener, MouseListener, ClickListener {
    	/**
    	 * Inner class used to create the Draggable Caption portion of the Window.
    	 * @author tschmidt
    	 *
    	 */
        private class Caption extends HorizontalPanel implements SourcesMouseEvents, SourcesDragEvents{
        private MouseListenerCollection mouseListeners;
        private DragListenerCollection dragListeners;
        public String name;
        
        public Caption() {
            super();
            sinkEvents(Event.MOUSEEVENTS);
        }
        
        public void addMouseListener(MouseListener listener) {
            if (mouseListeners == null) {
                mouseListeners = new MouseListenerCollection();
            }
            mouseListeners.add(listener);
        }

        public void addDragListener(DragListener listener) {
            if (dragListeners == null) {
                dragListeners = new DragListenerCollection();
            }
            dragListeners.add(listener, this);
        }

        public void onBrowserEvent(Event event) {
            switch (DOM.eventGetType(event)) {
                case Event.ONMOUSEDOWN:
                case Event.ONMOUSEUP:
                case Event.ONMOUSEMOVE:
                case Event.ONMOUSEOVER:
                case Event.ONMOUSEOUT:
                    if (mouseListeners != null) {
                        DOM.eventPreventDefault(event);
                        mouseListeners.fireMouseEvent(this, event);
                    }
            }
        }
    
        public void removeMouseListener(MouseListener listener) {
            if (mouseListeners != null) {
                mouseListeners.remove(listener);
            }
        }

        public void removeDragListener(DragListener listener) {
            if (dragListeners != null) {
                dragListeners.remove(listener);
            }
        }
    }
    private Caption cap = new Caption();
    
    private VerticalPanel outer = new VerticalPanel() {
       public void onBrowserEvent(Event event) {
           switch (DOM.eventGetType(event)) {
               case Event.ONCLICK:
                   checkZ();
           }
           super.onBrowserEvent(event);
       }
    };
    /**
     * DisclosurePanel is used to hold the windows messages to users.
     */
    public DisclosurePanel status = new DisclosurePanel();
    private ScrollPanel sp = new ScrollPanel();
    private FocusPanel fp = new FocusPanel();
    private ScreenImage close = new ScreenImage("Images/close.png");
    /**
     * Reference back to the WindowBrowser that this ScreenWindow is 
     * displayed in.
     */
    private WindowBrowser browser;
    private String name;
    /**
     * Current z-index of the window.
     */
    public int zIndex; 
    /**
     * The Screen or panel that is displayed by this window.
     */
    private Widget content;
    private Vector dropMap;
    private Label message = new Label("Loading...");
    
    public ScreenWindow(WindowBrowser browser, String name, String cat, String loadingText) {      
        initWidget(outer);
        this.browser = browser;
        this.name = name;
        zIndex = browser.index;
        
        //a way to internationalize the loading message
        if(loadingText != null)
        	message.setText(loadingText);
        
        HorizontalPanel hp = new HorizontalPanel();
        hp.setWidth("100%");
        hp.addStyleName("Caption");
        cap.addMouseListener(this);
        Label winLabel = new Label(name);
        winLabel.setStyleName("ScreenWindowLabel");
        cap.add(winLabel);
        cap.setWidth("100%");
        close.addClickListener(this);
        close.getWidget().setStyleName("CloseButton");
        hp.add(cap);
        //hp.setCellWidth(cap,"100%");
        hp.add(close);
        hp.setCellHorizontalAlignment(close,HasAlignment.ALIGN_RIGHT);
        status.setHeader(message);
        status.setStyleName("StatusBar");
        status.setWidth("100%");
        message.setStyleName("ScreenWindowLabel");
        outer.add(hp);
        outer.add(status);
        //outer.setCellWidth(status,"100%");
        outer.addStyleName("WindowPanel");
        outer.sinkEvents(Event.ONCLICK);
        outer.setWidth("auto");
        //fp.add(outer);
        //fp.setWidth("auto");
        //fp.addClickListener(this);
    }
    
    /**
     * Sets the Content of the Window to be displayed. If content is a 
     * ScreenForm, the message widget is linked to the form.
     * @param content
     */
    public void setContent(final Widget content){
        this.content = content;
        outer.insert(content, 1);
        if(content instanceof ScreenForm){
            ((ScreenForm)content).message =  message;
            ((ScreenForm)content).window = this;
        }
    }
    
    private void checkZ() {
        if(browser.index != zIndex){
            browser.index++;
           zIndex = browser.index;
           setKeep(true);
           int top = browser.browser.getWidgetTop(this);
           int left = browser.browser.getWidgetLeft(this);
           browser.browser.add((Widget)this,left,top);
           setKeep(false);
        }
    }
    
    public void onClick(Widget sender) {
        if(sender == fp){
            if(browser.index != zIndex){
                browser.index++;
               // WindowBrowser.setIndex(getElement(), browser.index);
               zIndex = browser.index;
            }
        }
        if(sender == close){
            close();
        }
        
    }
    
    public void close() {
        if(content instanceof FormInt){
            if(((FormInt)content).hasChanges()){
                return;
            }
        }
        removeFromParent();
        browser.browser.remove(this);
        browser.windows.remove(name);
        destroy();
        
    }
    
    public void onDragDropEnd(Widget sender, Widget target) {
        // TODO Auto-generated method stub
        
    }

    public void onDragEnd(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        int X = browser.browser.getWidgetLeft(sender);
        int Y = browser.browser.getWidgetTop(sender);
        if(X < 0)
            X = 0;
        if(Y <  0)
            Y = 0;
        browser.browser.setWidgetPosition(this,X,Y);
        browser.browser.remove(sender);
        browser.removeStyleName("locked");
        MouseDragGestureRecognizer.setDropMap(dropMap);
        
    }

    public void onDragEnter(Widget sender, Widget target) {
        // TODO Auto-generated method stub
        
    }

    public void onDragExit(Widget sender, Widget target) {
        // TODO Auto-generated method stub
        
    }

    public void onDragMouseMoved(Widget sender, int x, int y) {
        // Gets the elements
        
    }

    public void onDragOver(Widget sender, Widget target) {
        // TODO Auto-generated method stub
        
    }

    public void onDragStart(Widget sender, int x, int y) {
        // TODO Auto-generated method stub

    }

    public void onMouseDown(Widget sender, final int x, final int y) {
        if(browser.index != zIndex){
            checkZ();
            return;
        }
        final FocusPanel proxy = new FocusPanel();
        AbsolutePanel ap = new AbsolutePanel();
        proxy.setWidget(ap);
        ap.setWidth(outer.getOffsetWidth()+"px");
        ap.setHeight(outer.getOffsetHeight()+"px");
        ap.addStyleName("WindowDragPanel");
        proxy.addDragListener(this);
        browser.browser.add(proxy,browser.browser.getWidgetLeft(this),browser.browser.getWidgetTop(this));
        //WindowBrowser.setIndex(proxy.getElement(),browser.index);
        dropMap = MouseDragGestureRecognizer.getDropMap();
        MouseDragGestureRecognizer.setDropMap(new Vector());
        browser.addStyleName("locked");
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                MouseDragGestureRecognizer.getGestureMouse(proxy)
                                          .onMouseDown(proxy, x, y);
            }
        });
        
    }

    public void onMouseEnter(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseLeave(Widget sender) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseMove(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        // TODO Auto-generated method stub
        
    }
    
    public void destroy() {
        cap = null;
        outer = null;
        status = null;
        sp = null;
        fp = null;
        close = null;
        name = null;
        content = null;
        dropMap = null;
        message = null;
    }
    
    public void setKeep(boolean keep){
        ((ScreenBase)content).keep = keep;
    }

}
