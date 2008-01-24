package org.openelis.gwt.client.screen;

import java.util.Vector;

import org.openelis.gwt.client.widget.FormInt;
import org.openelis.gwt.client.widget.WindowBrowser;

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
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

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
    protected VerticalPanel messagePanel;
    protected PopupPanel pop;
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
    public HorizontalPanel status = new HorizontalPanel();
    private FocusPanel fp = new FocusPanel();
    private FocusPanel close = new FocusPanel();
    private FocusPanel collapse = new FocusPanel();
    private FocusPanel statusImg = new FocusPanel();
    private FocusPanel trCorner = new FocusPanel();
    private FocusPanel tlCorner = new FocusPanel();
    private FocusPanel brCorner = new FocusPanel();
    private FocusPanel blCorner = new FocusPanel();
    private FocusPanel leftSide = new FocusPanel();
    private FocusPanel rightSide = new FocusPanel();
    private VerticalPanel body = new VerticalPanel();
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
        
        tlCorner.addStyleName("WindowTL");
        trCorner.addStyleName("WindowTR");
        blCorner.addStyleName("WindowBL");
        brCorner.addStyleName("WindowBR");
        leftSide.addStyleName("WindowLeft");
        rightSide.addStyleName("WindowRight");
        
        HorizontalPanel hp = new HorizontalPanel();
        hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        HorizontalPanel titleButtonsContainer = new HorizontalPanel();
        titleButtonsContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        
        hp.setWidth("100%");
        titleButtonsContainer.addStyleName("Caption");
        titleButtonsContainer.setWidth("100%");
        
        cap.addMouseListener(this);
        Label winLabel = new Label(name);
        winLabel.setStyleName("ScreenWindowLabel");
        cap.add(winLabel);
        cap.setWidth("100%");
        close.addClickListener(this);
        close.setStyleName("CloseButton");
        collapse.addClickListener(this);
        collapse.setStyleName("MinimizeButton");
        hp.add(tlCorner);
        titleButtonsContainer.add(cap);
        hp.add(titleButtonsContainer);        
        hp.setCellWidth(titleButtonsContainer, "100%");
        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        hp2.add(collapse);
        hp2.add(close);
        titleButtonsContainer.add(hp2);
        hp.add(trCorner);
        hp.setCellWidth(hp2,"32px");
        hp.setCellHorizontalAlignment(hp2,HasAlignment.ALIGN_RIGHT);
        statusImg.addStyleName("StatusImage");
        statusImg.addMouseListener(this);
        status.setStyleName("StatusBar");
        status.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        
        //status.add(blCorner);
        status.add(statusImg);
        status.add(message);
       // status.add(brCorner);
        
        status.setWidth("100%");
        status.setCellWidth(message, "100%");
        status.setCellHeight(statusImg, "16px");
        status.setCellWidth(statusImg, "16px");
        message.setStyleName("ScreenWindowLabel");
        outer.add(hp);
        HorizontalPanel middleRow = new HorizontalPanel();
        HorizontalPanel bottomRow = new HorizontalPanel();
        bottomRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        
        bottomRow.setWidth("100%");
        bottomRow.setSpacing(0);
        
        middleRow.add(leftSide);
        middleRow.add(body);
        middleRow.add(rightSide);
        
        bottomRow.add(blCorner);
        bottomRow.add(status);
        bottomRow.add(brCorner);
        
        bottomRow.setCellWidth(status, "100%");
        
        body.addStyleName("WindowBody");
        
        outer.add(middleRow);
        outer.add(bottomRow);
        outer.addStyleName("WindowPanel");
        outer.sinkEvents(Event.ONCLICK);
        outer.setWidth("auto");
    }
    
    /**
     * Sets the Content of the Window to be displayed. If content is a 
     * ScreenForm, the message widget is linked to the form.
     * @param content
     */
    public void setContent(final Widget content){
        this.content = content;
        body.insert(content, 0);
        if(content instanceof ScreenForm){
            ((ScreenForm)content).message =  message;
            ((ScreenForm)content).window = this;
        }else if(content instanceof AppScreenForm){
        	((AppScreenForm)content).message =  message;
            ((AppScreenForm)content).window = this;
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
        if(sender == collapse){
            outer.setWidth(outer.getOffsetWidth()+"px");
            body.setVisible(!body.isVisible());
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
        if(sender == cap){
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
        //  WindowBrowser.setIndex(proxy.getElement(),browser.index);
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
    }

    public void onMouseEnter(Widget sender) {
        if(sender == statusImg){
            if(pop == null){
                pop = new PopupPanel();
                pop.setStyleName("MessagePopup");
            }
            if(messagePanel == null){
                messagePanel = new VerticalPanel();
                messagePanel.add(new Label("Testing Mouse Over"));
                messagePanel.setStyleName("MessagePopup");
            }
            pop.setWidget(messagePanel);
            //pop.setPopupPosition(sender.getAbsoluteLeft()+16, sender.getAbsoluteTop());
            final int left = sender.getAbsoluteLeft()+16;
            final int top = sender.getAbsoluteTop();
            pop.setPopupPositionAndShow(new PopupPanel.PositionCallback(){

                public void setPosition(int offsetWidth, int offsetHeight) {
                    pop.setPopupPosition(left, top-offsetHeight);
                    pop.show();
                }
               
            });
            
            //pop.show();
            
        }
    }

    public void onMouseLeave(Widget sender) {
       if(sender == statusImg){
           if(pop != null){
               pop.hide();
           }
       }   
    }

    public void onMouseMove(Widget sender, int x, int y) {
        
    }

    public void onMouseUp(Widget sender, int x, int y) {
        
    }
    
    public void destroy() {
        cap = null;
        outer = null;
        status = null;
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
    
    public void setMessagePopup(String[] messages) {
        messagePanel = new VerticalPanel();
        for(int i = 0; i < messages.length; i++){
            Label msg = new Label(messages[i]);
            messagePanel.add(msg);
        }
    }

}
