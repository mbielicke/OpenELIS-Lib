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

import org.openelis.ui.common.Warning;
import org.openelis.gwt.event.BeforeCloseEvent;
import org.openelis.gwt.event.BeforeCloseHandler;
import org.openelis.gwt.event.HasBeforeCloseHandlers;
import org.openelis.gwt.screen.Screen;
import org.openelis.ui.widget.WindowInt;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.HasCloseHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * ScreenWindow is used to display Screens inside a draggable window.  
 * ScreenWindows are currently only used inside a WindowBrowser 
 * @author tschmidt
 *
 */
public class ScreenWindow extends FocusPanel implements WindowInt, ClickHandler, MouseOverHandler, MouseOutHandler, MouseDownHandler, HasKeyPressHandlers, KeyPressHandler,  HasBeforeCloseHandlers<ScreenWindow>, ResizeHandler {
        /**
         * Inner class used to create the Draggable Caption portion of the Window.
         * @author tschmidt
         *
         */
        private class Caption extends HorizontalPanel implements HasAllMouseHandlers { 

        	public String name;

        	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        		return addDomHandler(handler, MouseDownEvent.getType());
        	}

        	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        		return addDomHandler(handler,MouseUpEvent.getType());
        	}

        	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
        		return addDomHandler(handler,MouseOutEvent.getType());
        	}

        	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
        		return addDomHandler(handler,MouseOverEvent.getType());
        	}

        	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        		return addDomHandler(handler,MouseMoveEvent.getType());
        	}

        	public HandlerRegistration addMouseWheelHandler(
        			MouseWheelHandler handler) {
        		return addDomHandler(handler,MouseWheelEvent.getType());
        	}
    }
        
    private class ProgressBar extends AbsolutePanel {
    		
    		AbsolutePanel prog = new AbsolutePanel();
    		Label pct = new Label();
    		
    		public ProgressBar() {
    			setSize("75px","12px");
    			add(prog,0,0);
    			add(pct,30,0);
    			setStyleName("ProgressBarOuter");
     			prog.setHeight("100%");
    			prog.setWidth("0%");
    			setWidgetPosition(prog, 0, 0);
    			prog.setStyleName("ProgressBar");
    			pct.setStyleName("ProgressBarPct");
    			DOM.setStyleAttribute(pct.getElement(), "zIndex","1000");
    			
    		}
    		
    		public void setProgress(int percent) {
    			prog.setWidth(percent+"%");
    			pct.setText(percent+"%");
    		}
    		
    	}
        
    private Caption cap = new Caption();
    protected VerticalPanel messagePanel;
    protected PopupPanel pop;
    private VerticalPanel outer = new VerticalPanel() {
       public void onBrowserEvent(Event event) {
           switch (DOM.eventGetType(event)) {
               case Event.ONCLICK: {
                   checkZ();
                   break;
               }
           }
           super.onBrowserEvent(event);
       }
    };

    public static final int position=100;
    private HorizontalPanel status = new HorizontalPanel();
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
    private Grid middleGrid = new Grid(1,3);
    private HorizontalPanel bottomRow = new HorizontalPanel();
    /**
     * Reference back to the WindowBrowser that this ScreenWindow is 
     * displayed in.
     */
    private WindowBrowser browser;
    private PopupPanel popupPanel;
    public String key;
    /**
     * Current z-index of the window.
     */
    public int zIndex; 
    /**
     * The Screen or panel that is displayed by this window.
     */
    public Widget content;
    private Label message = new Label();
    private Label winLabel = new Label();
    
    private AbsolutePanel glass;
    private AbsolutePanel modalPanel;
    private AbsolutePanel modalGlass;
    private PickupDragController dragController;
    private AbsolutePositionDropController dropController;
    private HorizontalPanel titleButtonsContainer;
    public enum Mode {SCREEN,DIALOG,LOOK_UP};
    private Mode mode;
    private ProgressBar progressBar = new ProgressBar();
    private DecoratorPanel dp;
    
    
    public ScreenWindow(Mode mode) {
    	init(mode,false);
    }
    
    public ScreenWindow(Mode mode, boolean noBorder) {
    	init(mode,noBorder);
    }
    
    public ScreenWindow(WindowBrowser brws, String key){
    	this.browser = brws;
    	this.key = key;
    	init(Mode.SCREEN,false);
    }
    
    public void init(Mode mode, boolean noBorder) {   
    	this.mode = mode;
        
    	
    	setWidget(outer);
        setVisible(false);
        
        if(browser != null)
            zIndex = browser.index;
        
        if(!noBorder) {
        	tlCorner.addStyleName("WindowTL");
        	trCorner.addStyleName("WindowTR");
        	blCorner.addStyleName("WindowBL");
        	brCorner.addStyleName("WindowBR");
        	leftSide.addStyleName("WindowLeft");
        	rightSide.addStyleName("WindowRight");


        	HorizontalPanel hp = new HorizontalPanel();
        	hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        	titleButtonsContainer = new HorizontalPanel();
        	titleButtonsContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        	hp.setWidth("100%");
        	titleButtonsContainer.addStyleName("Caption");
        	titleButtonsContainer.setWidth("100%");
        	

        	cap.addMouseDownHandler(this);
        	winLabel.setStyleName("ScreenWindowLabel");
        	cap.add(winLabel);
        	cap.setWidth("100%");
        	cap.setHeight("100%");
        	cap.setCellVerticalAlignment(winLabel, HasAlignment.ALIGN_MIDDLE);
        	close.addClickHandler(this);
        	close.setStyleName("CloseButton");
        	collapse.addClickHandler(this);
        	collapse.setStyleName("MinimizeButton");
        	hp.add(tlCorner);
        	titleButtonsContainer.add(cap);
        	titleButtonsContainer.setCellWidth(cap, "100%");
        	hp.add(titleButtonsContainer);        
        	hp.setCellWidth(titleButtonsContainer, "100%");

        	if(mode == Mode.SCREEN || mode == Mode.LOOK_UP){
        		HorizontalPanel hp2 = new HorizontalPanel();
        		hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        		hp2.add(collapse);
        		hp2.add(close);
        		titleButtonsContainer.add(hp2);
        		titleButtonsContainer.setCellHorizontalAlignment(hp2, HasAlignment.ALIGN_RIGHT);
        		hp.setCellWidth(hp2,"32px");
        		hp.setCellHorizontalAlignment(hp2,HasAlignment.ALIGN_RIGHT);
        	}
        	hp.add(trCorner);
        	statusImg.addMouseOverHandler(this);
        	statusImg.addMouseOutHandler(this);
        	status.setStyleName("StatusBar");
        	status.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        	status.add(statusImg);
        	status.add(message);
        	status.add(progressBar);
        	progressBar.setVisible(false);
        	status.setWidth("100%");


        	status.setCellWidth(message, "100%");
        	message.setStyleName("ScreenWindowLabel");
        	outer.add(hp);

        	bottomRow.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

        	bottomRow.setWidth("100%");
        	bottomRow.setSpacing(0);

        	middleGrid.setCellPadding(0);
        	middleGrid.setCellSpacing(0);
        	middleGrid.getCellFormatter().addStyleName(0,0,"WindowLeft");
        	middleGrid.setWidget(0, 1, body);
        	middleGrid.getCellFormatter().addStyleName(0,2,"WindowRight");

        	bottomRow.add(blCorner);
        	bottomRow.add(status);
        	bottomRow.add(brCorner);

        	bottomRow.setCellWidth(status, "100%");

        	body.addStyleName("WindowBody");

        	outer.add(middleGrid);
        	outer.add(bottomRow);
        	outer.addStyleName("WindowPanel");
        	outer.sinkEvents(Event.ONCLICK);
        	outer.setWidth("auto");
        }else{
        	 outer.add(body);
        	 outer.addStyleName("WindowPanel");
             outer.sinkEvents(Event.ONCLICK);
             outer.setWidth("auto");
        }
        
        Window.addResizeHandler(this);
        if(browser != null){
            browser.dragController.makeDraggable(this, cap);
            return;
        }
        if(mode == Mode.LOOK_UP || mode == Mode.DIALOG){
            modalGlass = new AbsolutePanel();
            modalGlass.setStyleName("GlassPanel");
            modalGlass.setHeight(Window.getClientHeight()+"px");
            modalGlass.setWidth(Window.getClientWidth()+"px");
            
            RootPanel.get().add(modalGlass);
            RootPanel.get().setWidgetPosition(modalGlass, 0, 0);
            modalPanel = new AbsolutePanel();
            modalPanel.setStyleName("ModalPanel");
            modalPanel.setHeight(Window.getClientHeight()+"px");
            modalPanel.setWidth(Window.getClientWidth()+"px");
            modalPanel.add(this,position,position);
            RootPanel.get().add(modalPanel); 
            RootPanel.get().setWidgetPosition(modalPanel,0,0);
            setVisible(true);
            dragController = new PickupDragController(modalPanel,true);
            dropController = new AbsolutePositionDropController(modalPanel);
            dragController.registerDropController(dropController);
            dragController.makeDraggable(this,cap);
            dragController.setBehaviorDragProxy(true);
        }else{
        	RootPanel.get().add(this);
        	RootPanel.get().setWidgetPosition(this, position, position);
        	setVisible(true);
        	dragController = new PickupDragController(RootPanel.get(),true);
        	dropController = new AbsolutePositionDropController(RootPanel.get());
        	dragController.registerDropController(dropController);
        	dragController.makeDraggable(this,cap);
        	dragController.setBehaviorDragProxy(true);
        }
    }
    

    
    public void setContent(Widget content, int x, int y) {
        if(modalGlass != null){
            modalPanel.setWidgetPosition(this, x, y);
        }else
        	RootPanel.get().setWidgetPosition(this, x, y);
        setContent(content);
    }
    
    /**
     * Sets the Content of the Window to be displayed. If content is a 
     * ScreenForm, the message widget is linked to the form.
     * @param content
     */
    public void setContent(final Widget content){
    	if(this.content != null) 
    		body.remove(this.content);
        this.content = content;
        body.insert(content, 0);
        if(content instanceof Screen) {
        	((Screen)content).setWindow(this);
        	setName(((Screen)content).getDefinition().getName());
        	setVisible(true);
            RootPanel.get().removeStyleName("ScreenLoad");
            //setStatus(Screen.consts.get("loadCompleteMessage"),"");
            addKeyPressHandler(this);
            
        }
        
        DeferredCommand.addCommand(new Command() {
        	public void execute() {
        		if(titleButtonsContainer != null) {
        			if(content.getOffsetWidth() < titleButtonsContainer.getOffsetWidth())
        				body.setWidth(titleButtonsContainer.getOffsetWidth()+"px");
        		}
        		setFocus(true);
        	}
        });
    }
    
    public void setName(String name) {
        cap.name = name;
        winLabel.setText(name);
    }
    
    private void checkZ() {
        if(browser != null && browser.index != zIndex){
           browser.index++;
           zIndex = browser.index;
           int top = browser.browser.getWidgetTop(this);
           int left = browser.browser.getWidgetLeft(this);
           browser.browser.setWidgetPosition((Widget)this,left,top);
           browser.setFocusedWindow();
        }
    }
    
    public void onClick(ClickEvent event) {
        if(event.getSource() == fp){
            if(browser != null && browser.index != zIndex){
                browser.index++;

               zIndex = browser.index;
            }
        }
        if(event.getSource() == close){
            close();
        }
        if(event.getSource() == collapse){
            if(middleGrid.isVisible())
                outer.setWidth(outer.getOffsetWidth()+"px");
            else
                outer.setWidth("");
            middleGrid.setVisible(!middleGrid.isVisible());
            bottomRow.setVisible(!bottomRow.isVisible());
        }
        
    }
    
    public void close() {
    	if(getHandlerCount(BeforeCloseEvent.getType()) > 0) {
    		BeforeCloseEvent<ScreenWindow> event = BeforeCloseEvent.fire(this, this);
    		if(event != null && event.isCancelled())
    			return;
    	}
        if(getHandlerCount(org.openelis.ui.event.BeforeCloseEvent.getType()) > 0) {
            org.openelis.ui.event.BeforeCloseEvent<WindowInt> event = org.openelis.ui.event.BeforeCloseEvent.fire(this, this);
            if(event != null && event.isCancelled())
                return;
        }
        if(modalGlass != null) {
           removeFromParent();
           RootPanel.get().remove(modalGlass);
           RootPanel.get().remove(modalPanel);
        }else {
        	removeFromParent();
        	if(browser != null){
            browser.browser.remove(this);
            browser.windows.remove(key);
        	}
        	if(popupPanel != null){
        		popupPanel.hide();
        	}
        	destroy();
        	if(browser != null){
        		browser.index--;
        		browser.setFocusedWindow();
        	}
        }
        CloseEvent.fire(this, this);
    }

    
    public void destroy() {
        cap = null;
        outer = null;
        status = null;
        fp = null;
        close = null;
        key = null;
        content = null;
        message = null;

    }
        
    @Override
    public void setMessagePopup(ArrayList<Exception> exceptions, String style) {
        statusImg.setStyleName(style);
        statusImg.sinkEvents(Event.MOUSEEVENTS);
        messagePanel = new VerticalPanel();
        for (Exception exception : exceptions) {
        	HorizontalPanel hp = new HorizontalPanel();
        	if(exception instanceof Warning) {
                hp.add(new Image("Images/bullet_yellow.png"));
                hp.setStyleName("warnPopupLabel");
            }else{
                hp.add(new Image("Images/bullet_red.png"));
                hp.setStyleName("errorPopupLabel");
                style = "InputError";
            }
        	hp.add(new Label(exception.getMessage()));
            messagePanel.add(hp);
        }
    }
    
    public void clearMessagePopup(String style) {
        statusImg.setStyleName(style);
        statusImg.unsinkEvents(Event.MOUSEEVENTS);
    }
    
    public void setStatus(String text, String style){
        if(message != null){
            message.setText(text);
            statusImg.setStyleName(style);
        }
        unlockWindow();
    }
    
    public void lockWindow() {
    	if(glass == null) {
    		glass = new AbsolutePanel();
    		glass.setStyleName("GlassPanel");
    		glass.setHeight(content.getOffsetHeight()+"px");
    		glass.setWidth(content.getOffsetWidth()+"px");
    		if(browser != null)
    			browser.browser.add(glass, content.getAbsoluteLeft() - browser.getAbsoluteLeft(), content.getAbsoluteTop() - browser.getAbsoluteTop());
    		else
    			RootPanel.get().add(glass, content.getAbsoluteLeft(),content.getAbsoluteTop());
    	}
    }
    
    public void unlockWindow() {
    	if(glass != null) {
    		glass.removeFromParent();
    		glass = null;
    	}
    }
    
    public void setBusy() {
        setStatus("","spinnerIcon");
        lockWindow();
        

    }
    
    public void setBusy(String message) {
        setStatus(message,"spinnerIcon");
       	lockWindow();
    }
    
    public void clearStatus() {
        setStatus("","");
        unlockWindow();
        
    }
    
    public void setDone(String message) {
        setStatus(message,"");
        unlockWindow();
    }
    
    public void setError(String message) {
   		clearMessagePopup(message);
       	setStatus(message,"ErrorPanel");
       	unlockWindow();
    }

    public boolean onEventPreview(Event event) {
        DOM.eventPreventDefault(event);
        return true;
    }

	public void onMouseOver(MouseOverEvent event) {
        if(event.getSource() == statusImg){
            if(messagePanel == null){
                return;
            }
            if(pop == null){
                pop = new PopupPanel();
            }
            
            DecoratorPanel dp = new DecoratorPanel();
            dp.setStyleName("ErrorWindow");
            dp.add(messagePanel);
            dp.setVisible(true);
            
            pop.setWidget(dp);
            final int left = ((Widget)event.getSource()).getAbsoluteLeft()+16;
            final int top = ((Widget)event.getSource()).getAbsoluteTop();
            pop.setPopupPositionAndShow(new PopupPanel.PositionCallback(){

                public void setPosition(int offsetWidth, int offsetHeight) {
                    pop.setPopupPosition(left, top-offsetHeight);
                    pop.show();
                }
               
            });
            pop.show();
        }
		
	}

	public void onMouseOut(MouseOutEvent event) {
	       if(event.getSource() == statusImg){
	           if(pop != null){
	               pop.hide();
	           }
	       }
	}

	public void onMouseDown(MouseDownEvent event) {
        if(event.getSource() == cap){
            if(browser != null) {
                if(browser.index != zIndex){
                    checkZ();
                    return;
                }
            }
        }
		
	}

	public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
		return addDomHandler(handler,KeyPressEvent.getType());
	}

	public void onKeyPress(KeyPressEvent event) {
		KeyPressEvent.fireNativeEvent(event.getNativeEvent(), ((Screen)content).getDefinition().getPanel());
		
	}
	
	public void positionGlass() {
		if(glass != null) {
			unlockWindow();
			lockWindow();
		}
	}

	public HandlerRegistration addCloseHandler(
			CloseHandler<WindowInt> handler) {
		return addHandler(handler, CloseEvent.getType());
	}

	public HandlerRegistration addBeforeClosedHandler(
			BeforeCloseHandler<ScreenWindow> handler) {
		return addHandler(handler, BeforeCloseEvent.getType());
	}

	public void onResize(ResizeEvent event) {
		 if(modalGlass != null) {
			 modalGlass.setHeight(Window.getClientHeight()+"px");
			 modalGlass.setWidth(Window.getClientWidth()+"px");
		 }
		 if(modalPanel != null) {
			 modalPanel.setHeight(Window.getClientHeight()+"px");
			 modalPanel.setWidth(Window.getClientWidth()+"px");
		 }
		 if(glass != null) {
			 glass.setHeight(content.getOffsetHeight()+"px");
			 glass.setWidth(content.getOffsetWidth()+"px");
		 }

	}
	
	public void setProgress(int percent) {
		if(percent > 0){
			progressBar.setVisible(true);
			progressBar.setProgress(percent);
		}else
			progressBar.setVisible(false);
	}

    public WindowBrowser getBrowser() {
        return browser;
    }

    @Override
    public HandlerRegistration addBeforeClosedHandler(org.openelis.ui.event.BeforeCloseHandler<WindowInt> handler) {
        return addHandler(handler,org.openelis.ui.event.BeforeCloseEvent.getType());
    }


    @Override
    public void makeDragable(DragController controller) {
        
    }

    @Override
    public Widget getContent() {
        return content;
    }
}
