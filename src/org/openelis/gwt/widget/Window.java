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

import org.openelis.gwt.common.LocalizedException;
import org.openelis.gwt.common.Warning;
import org.openelis.gwt.event.BeforeCloseEvent;
import org.openelis.gwt.event.BeforeCloseHandler;
import org.openelis.gwt.screen.Screen;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Window extends FocusPanel implements WindowInt {
    
    protected Caption cap = new Caption();
    protected VerticalPanel messagePanel;
    protected PopupPanel pop;
    protected Window source;
    private VerticalPanel outer = new VerticalPanel() {
       public void onBrowserEvent(Event event) {
           switch (DOM.eventGetType(event)) {
               case Event.ONCLICK: {
            	   //FocusEvent.fireNativeEvent(Document.get().createFocusEvent(),source);
                   //setFocus(true);
                   break;
               }
           }
           super.onBrowserEvent(event);
       }
    };

    protected HorizontalPanel status = new HorizontalPanel();
    protected FocusPanel fp = new FocusPanel();
    protected FocusPanel close = new FocusPanel();
    protected FocusPanel collapse = new FocusPanel();
    protected FocusPanel statusImg = new FocusPanel();
    protected FocusPanel trCorner = new FocusPanel();
    protected FocusPanel tlCorner = new FocusPanel();
    protected FocusPanel brCorner = new FocusPanel();
    protected FocusPanel blCorner = new FocusPanel();
    protected FocusPanel leftSide = new FocusPanel();
    protected FocusPanel rightSide = new FocusPanel();
    protected VerticalPanel body = new VerticalPanel();
    protected Grid middleGrid = new Grid(1,3);
    protected HorizontalPanel bottomRow = new HorizontalPanel();

    /**
     * The Screen or panel that is displayed by this window.
     */
    protected Widget content;
    protected Label message = new Label("Loading...");
    protected Label winLabel = new Label();
    
    protected AbsolutePanel glass;
   
    private HorizontalPanel titleButtonsContainer;
    private ProgressBar progressBar = new ProgressBar();
    
    
    
    public Window() {
        setWidget(outer);
        setVisible(false);
                
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
        
        cap.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                setFocus(true);
            }
        });
        
        winLabel.setStyleName("ScreenWindowLabel");
        cap.add(winLabel);
        cap.setWidth("100%");
        close.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                close();
            }
        });
        close.setStyleName("CloseButton");
        collapse.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if(middleGrid.isVisible())
                    outer.setWidth(outer.getOffsetWidth()+"px");
                else
                    outer.setWidth("");
                middleGrid.setVisible(!middleGrid.isVisible());
                bottomRow.setVisible(!bottomRow.isVisible());
            }
        });
        collapse.setStyleName("MinimizeButton");
        hp.add(tlCorner);
        titleButtonsContainer.add(cap);
        titleButtonsContainer.setCellWidth(cap, "100%");
        hp.add(titleButtonsContainer);        
        hp.setCellWidth(titleButtonsContainer, "100%");
        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        hp2.add(collapse);
        hp2.add(close);
        titleButtonsContainer.add(hp2);
        titleButtonsContainer.setCellHorizontalAlignment(hp2, HasAlignment.ALIGN_RIGHT);
        hp.setCellWidth(hp2,"32px");
        hp.setCellHorizontalAlignment(hp2,HasAlignment.ALIGN_RIGHT);

        hp.add(trCorner);
        statusImg.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
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
        });
        statusImg.addMouseOutHandler(new MouseOutHandler() {
           public void onMouseOut(MouseOutEvent event) {
               if(pop != null){
                   pop.hide();
               }
            } 
        });
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
        com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {
            public void onResize(ResizeEvent event) {
                if(glass != null) {
                    glass.setHeight(content.getOffsetHeight()+"px");
                    glass.setWidth(content.getOffsetWidth()+"px");
                }
            }
        });
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
            setStatus("Done","");
            
            /**
             * This handler is added to forward the key press event if received by the window 
             * down to the screen.
             */
            addDomHandler(new KeyDownHandler() {
             	 public void onKeyDown(KeyDownEvent event) {
            		 KeyDownEvent.fireNativeEvent(event.getNativeEvent(), content);   
            	 }
            },KeyDownEvent.getType());
            
        }
        
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
			public void execute() {
                if(content.getOffsetWidth() < titleButtonsContainer.getOffsetWidth())
                    body.setWidth(titleButtonsContainer.getOffsetWidth()+"px");
                setFocus(true);
			}
		});
    }
    
    public void setName(String name) {
        cap.name = name;
        winLabel.setText(name);
    }
    
    public void close() { 
        if(getHandlerCount(BeforeCloseEvent.getType()) > 0) {
            BeforeCloseEvent<WindowInt> event = BeforeCloseEvent.fire(this, this);
            if(event != null && event.isCancelled())
                return;
        }
        removeFromParent();
      
        destroy();
        
        CloseEvent.fire(this, this);
    }

    
    public void destroy() {
        cap = null;
        outer = null;
        status = null;
        fp = null;
        close = null;
        content = null;
        message = null;

    }
        
    public void setMessagePopup(ArrayList<LocalizedException> exceptions, String style) {
        statusImg.setStyleName(style);
        statusImg.sinkEvents(Event.MOUSEEVENTS);
        messagePanel = new VerticalPanel();
        for (LocalizedException exception : exceptions) {
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

    public void positionGlass() {
        if(glass != null) {
            unlockWindow();
            lockWindow();
        }
    }

    public HandlerRegistration addCloseHandler(CloseHandler<WindowInt> handler) {
        return addHandler(handler, CloseEvent.getType());
    }

    public HandlerRegistration addBeforeClosedHandler(BeforeCloseHandler<WindowInt> handler) {
        return addHandler(handler, BeforeCloseEvent.getType());
    }

    public void onResize(ResizeEvent event) {
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
    
    protected void makeDragable(DragController controller) {
    	controller.makeDraggable(this,cap);
    }
    
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
			return addDomHandler(handler, MouseUpEvent.getType());
		}

		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}

		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());
		}

		public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
			return addDomHandler(handler, MouseMoveEvent.getType());
		}

		public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
			return addDomHandler(handler, MouseWheelEvent.getType());
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
    


}
