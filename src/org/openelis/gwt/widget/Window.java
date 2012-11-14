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
import org.openelis.gwt.constants.Constants;
import org.openelis.gwt.event.BeforeCloseEvent;
import org.openelis.gwt.event.BeforeCloseHandler;
import org.openelis.gwt.resources.OpenELISResources;
import org.openelis.gwt.resources.WindowCSS;
import org.openelis.gwt.screen.ViewPanel;

import com.allen_sauer.gwt.dnd.client.DragController;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Window extends FocusPanel implements WindowInt {
	
	@UiTemplate("Window.ui.xml")
	interface WindowUiBinder extends UiBinder<Widget, Window>{};
	public static final WindowUiBinder uiBinder = GWT.create(WindowUiBinder.class);
    
    @UiField 
    protected Caption                               cap;
    @UiField 
    protected VerticalPanel                         outer;
    protected VerticalPanel                         messagePanel;
    protected PopupPanel                            pop;
    @UiField 
    protected Grid                                  top,bottom;
    @UiField 
    protected FocusPanel                            statusImg, close,collapse;
    @UiField 
    protected AbsolutePanel                         body;
    protected AbsolutePanel                         glass;
    protected HTML                                  label;
 
   // protected ResizeDragController  dragController = new ResizeDragController(RootPanel.get(),this);

    /**
     * The Screen or panel that is displayed by this window.
     */
    protected Widget                                content;
           
    protected WindowCSS                             css;
    
    protected Window                                source = this;
    
    
    public Window() {
        
    	setWidget(uiBinder.createAndBindUi(this));
    	
        setVisible(false);
        
        cap.addMouseDownHandler(new MouseDownHandler() {
            public void onMouseDown(MouseDownEvent event) {
                setFocus(true);
            }
        });
        
        label = new HTML();
        label.setText(" ");
       
        cap.add(label);
                
             
        close.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                close();
            }
        });
        
         
        collapse.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                if(body.isVisible())
                    outer.setWidth(outer.getOffsetWidth()+"px");
                else
                    outer.setWidth("");
                body.setVisible(!body.isVisible());
                bottom.setVisible(!bottom.isVisible());
            }
        });
     
        
        statusImg.addMouseOverHandler(new MouseOverHandler() {
            public void onMouseOver(MouseOverEvent event) {
                if(messagePanel == null){
                    return;
                }
                if(pop == null){
                    pop = new PopupPanel();
                }
                
                Window errorWin = new Window();
                errorWin.setCSS(OpenELISResources.INSTANCE.dialog());
                errorWin.setContent(messagePanel);
                
                pop.setWidget(errorWin);
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
        
        top.getCellFormatter().setWidth(0, 1, "16px");
        top.getCellFormatter().setWidth(0, 2, "16px");
        
        bottom.setText(0,1,Constants.get().loading());
        
        /* Sink events and add resize Handler */
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
        
        /* Apply style to the window elements */
        setCSS(OpenELISResources.INSTANCE.window());
    }

    /**
     * Sets the Content of the Window to be displayed. If content is a 
     * ScreenForm, the message widget is linked to the form.
     * @param content
     */
    public void setContent(final Widget content){
        this.content = content;
        body.clear();
        body.add(content);
       	setVisible(true);
       	setKeyHandling();
       	setDone(Constants.get().done());
       	RootPanel.get().removeStyleName(css.ScreenLoad());
    }
    
    public void setKeyHandling() {
        /**
         * This handler is added to forward the key press event if received by the window 
         * down to the screen.
         */
        addDomHandler(new KeyDownHandler() {
         	 public void onKeyDown(KeyDownEvent event) {
         		 if(content instanceof ViewPanel)
         			 KeyDownEvent.fireNativeEvent(event.getNativeEvent(), ((ViewPanel)content));
        	 }
        },KeyDownEvent.getType());
    }
    
    public void setName(String name) {
        label.setText(name);
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
        bottom = null;
        close = null;
        content = null;
    }
        
    public void setMessagePopup(ArrayList<LocalizedException> exceptions, String style) {
        statusImg.setStyleName(style);
        statusImg.sinkEvents(Event.MOUSEEVENTS);
        messagePanel = new VerticalPanel();
        for (LocalizedException exception : exceptions) {
            HorizontalPanel hp = new HorizontalPanel();
            if(exception instanceof Warning) {
                hp.add(new Image(OpenELISResources.INSTANCE.warn()));
                hp.setStyleName(css.warnPopupLabel());
            }else{
                hp.add(new Image(OpenELISResources.INSTANCE.error()));
                hp.setStyleName(css.errorPopupLabel());
                style = css.InputError();
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
    	bottom.setText(0,1,text);
        statusImg.setStyleName(style);
        unlockWindow();
    }
    
    public void lockWindow() {
        if(glass == null) {
            glass = new AbsolutePanel();
            glass.setStyleName(css.GlassPanel());
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
        setStatus("",css.spinnerIcon());
        lockWindow();

    }
    
    public void setBusy(String message) {
        setStatus(message,css.spinnerIcon());
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
        setStatus(message,css.ErrorPanel());
        unlockWindow();
    }

    public void positionGlass() {
        if(glass != null) {
            unlockWindow();
            lockWindow();
        }
    }
    
    public void setCSS(WindowCSS css) {
    	this.css = css;
    	css.ensureInjected();
    	top.setStyleName(css.top());
    	cap.setStyleName(css.Caption());
    	label.setStyleName(css.ScreenWindowLabel());
    	close.setStyleName(css.CloseButton());
    	collapse.setStyleName(css.MinimizeButton());
    	bottom.setStyleName(css.StatusBar());
    	bottom.getCellFormatter().setStyleName(0,1,css.ScreenWindowLabel());
    	body.setStyleName(css.WindowBody());
    	outer.setStyleName(css.WindowPanel());
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
        
    public void makeDragable(DragController controller) {
    	controller.makeDraggable(this,cap);
    }
    
    /**
     * Inner class used to create the Draggable Caption portion of the Window.
     * @author tschmidt
     *
     */
    protected static class Caption extends AbsolutePanel implements HasAllMouseHandlers { 

    	
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

}
